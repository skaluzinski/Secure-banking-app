package com.example.securebankingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationtocopy.ui.theme.SecureBankingAppTheme
import com.example.securebankingapp.data.services.AuthTokenService
import com.example.securebankingapp.navigation.AppNavHost
import com.example.securebankingapp.navigation.Destinations
import com.example.securebankingapp.navigation.DestinationsRelay
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    @Singleton
    lateinit var destinationsRelay: DestinationsRelay

    @Inject
    @Singleton
    lateinit var authTokenService: AuthTokenService

    private var lastUserInteraction: LocalDateTime = LocalDateTime.now()
    private val secondSinceLastUserInteraction: MutableStateFlow<Int> = MutableStateFlow(0)

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SecureBankingAppTheme {
                val navController = rememberNavController()
                val activityProvider: () -> AppCompatActivity = remember { { this } }

                val authTokenState by authTokenService.authToken.collectAsState()

                HandleNavigationEvents(destinationsRelay = destinationsRelay, navController = navController, authTokenState = authTokenState)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        startDestination = createRoutePattern<Destinations.Login>(),
                        navController = navController,
                        activity = activityProvider,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onUserInteraction() {
        MainScope().launch {
            val secondsBetween = ChronoUnit.SECONDS.between(lastUserInteraction, LocalDateTime.now())
            lastUserInteraction = LocalDateTime.now()

            secondSinceLastUserInteraction.emit(secondsBetween.toInt())
        }

        super.onUserInteraction()
    }
}


@OptIn(ExperimentalSerializationApi::class)
@Composable
fun HandleNavigationEvents(
    destinationsRelay: DestinationsRelay,
    navController: NavController,
    authTokenState: String?
) {
    LaunchedEffect(key1 = Unit, key2 = authTokenState) {
        destinationsRelay.navigationEvents.collect { destination ->
            if (authTokenState == null) {
                val shouldPopUpToLogin = when(destination) {
                    Destinations.Login, Destinations.Register, Destinations.RequestBits -> false
                    is Destinations.InputLoginBits -> false
                    else -> true
                }

                if (shouldPopUpToLogin) {
                    println("### should pop up $authTokenState")
                    navController.navigate(Destinations.Login) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                    return@collect
                }
            }

            if (destination == Destinations.Login) {
                navController.navigate(destination) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            } else {
                navController.navigate(destination)
            }
        }
    }
}