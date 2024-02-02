package com.example.securebankingapp

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationtocopy.ui.theme.SecureBankingAppTheme
import com.example.securebankingapp.data.services.AccountStateService
import com.example.securebankingapp.navigation.AppNavHost
import com.example.securebankingapp.navigation.Destinations
import com.example.securebankingapp.navigation.DestinationsRelay
import com.example.securebankingapp.navigation.ToastRelay
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.Duration
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
    lateinit var accountStateService: AccountStateService

    @Inject
    lateinit var toastRelay: ToastRelay

    private var lastUserInteraction: LocalDateTime = LocalDateTime.now()
    private val secondSinceLastUserInteraction: MutableStateFlow<Int> = MutableStateFlow(0)

    override fun onPause() {
        destinationsRelay.navigateTo(Destinations.Login)
        accountStateService.updateState(false)
        super.onPause()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContent {
            SecureBankingAppTheme {
                val navController = rememberNavController()
                val activityProvider: () -> AppCompatActivity = remember { { this } }

                val snackbarHostState = remember { SnackbarHostState() }

                val coroutineScope = rememberCoroutineScope()

                var dialogText by remember { mutableStateOf("") }
                
                LaunchedEffect(key1 = Unit) {
                    coroutineScope.launch {
                        while (isActive) {
                            // Perform your tasks here
                            delay(1000)
                            val secondsBetween =
                                calculateSecondsBetween(lastUserInteraction, LocalDateTime.now())
                            when (secondsBetween) {
                                in 0..30 -> {
                                    dialogText = ""
                                }

                                in 31..60 -> {
                                    dialogText =
                                        "App will be closed if there's no user interaction in ${60 - secondsBetween} seconds"
                                }

                                else -> finish()
                            }
                        }
                    }
                }
                
                if (dialogText.isNotEmpty()) {
                    Dialog(onDismissRequest = {
                        lastUserInteraction = LocalDateTime.now()

                    }) {
                        Card() {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp), text = dialogText
                            )
                        }
                    }
                }

                HandleToastsEvents(
                    toastRelay = toastRelay,
                )

                val isUserLoggedIn by accountStateService.accountState.collectAsState()

                HandleNavigationEvents(
                    destinationsRelay = destinationsRelay,
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn
                )

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
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

@Composable
fun HandleToastsEvents(
    toastRelay: ToastRelay,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        toastRelay.toastEvents.collect { toast ->
            Toast.makeText(context, toast.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun HandleNavigationEvents(
    destinationsRelay: DestinationsRelay,
    navController: NavController,
    isUserLoggedIn: Boolean
) {
    LaunchedEffect(key1 = Unit, key2 = isUserLoggedIn) {
        destinationsRelay.navigationEvents.collect { destination ->
            if (!isUserLoggedIn) {
                val shouldPopUpToLogin = when(destination) {
                    Destinations.Login, Destinations.Register, Destinations.RequestBits -> false
                    is Destinations.InputLoginBits -> false
                    else -> true
                }

                if (shouldPopUpToLogin) {
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

fun calculateSecondsBetween(dateTime1: LocalDateTime, dateTime2: LocalDateTime): Long {
    val duration = Duration.between(dateTime1, dateTime2)
    return duration.seconds
}