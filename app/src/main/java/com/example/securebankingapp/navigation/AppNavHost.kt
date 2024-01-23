package com.example.securebankingapp.navigation

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kiwi.navigationcompose.typed.composable
import kotlinx.serialization.ExperimentalSerializationApi
import com.example.securebankingapp.presentation.login.LoginScreen
import com.example.securebankingapp.presentation.login.LoginViewModel
import com.example.securebankingapp.presentation.loginWithBits.InputLoginBitsScreen
import com.example.securebankingapp.presentation.loginWithBits.InputLoginBitsScreenEvent
import com.example.securebankingapp.presentation.loginWithBits.InputLoginBitsViewModel
import com.example.securebankingapp.presentation.register.RegisterScreen
import com.example.securebankingapp.presentation.register.RegisterViewModel
import com.example.securebankingapp.presentation.requestBitsToLogin.RequestBitsToLoginViewModel
import com.example.securebankingapp.presentation.requestBitsToLogin.RequestLoginWithBitsScreen

@OptIn(ExperimentalSerializationApi::class)
@Composable
internal fun AppNavHost(
    startDestination: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    activity: () -> AppCompatActivity,
    ) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<Destinations.Login> {
            val viewModel: LoginViewModel by activity().viewModels()
            val viewState by viewModel.state.collectAsState()

            LoginScreen(
                viewState = viewState,
                onLoginScreenEvent = viewModel::onEvent
            )
        }
        composable<Destinations.Home> {
        }
        composable<Destinations.InputLoginBits> {
            val viewModel: InputLoginBitsViewModel by activity().viewModels()
            val viewState by viewModel.state.collectAsState()
            val bits = this.loginBits

            LaunchedEffect(key1 = Unit) {
                viewModel.onEvent(InputLoginBitsScreenEvent.LoadInitialBits(bits))
            }

            InputLoginBitsScreen(
                viewState = viewState,
                onEvent = viewModel::onEvent
            )
        }

        composable<Destinations.RequestBits> {
            val viewModel: RequestBitsToLoginViewModel by activity().viewModels()
            val viewState by viewModel.state.collectAsState()

            RequestLoginWithBitsScreen(
                viewState = viewState,
                onEvent =viewModel::onEvent
            )
        }

        composable<Destinations.Profile> {

        }
        composable<Destinations.Register> {
            val viewModel: RegisterViewModel by activity().viewModels()
            val viewState by viewModel.state.collectAsState()

            RegisterScreen(
                viewState = viewState,
                onEvent = viewModel::onEvent
            )
        }
        composable<Destinations.UserList> {

        }
    }
}
