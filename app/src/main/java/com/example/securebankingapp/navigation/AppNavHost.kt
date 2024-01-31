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
import com.example.securebankingapp.presentation.depositMoney.DepositMoneyEvent
import com.example.securebankingapp.presentation.depositMoney.DepositMoneyScreen
import com.example.securebankingapp.presentation.depositMoney.DepositMoneyViewModel
import com.example.securebankingapp.presentation.home.HomeScreen
import com.example.securebankingapp.presentation.home.HomeScreenEvent
import com.example.securebankingapp.presentation.home.HomeScreenViewModel
import com.kiwi.navigationcompose.typed.composable
import kotlinx.serialization.ExperimentalSerializationApi
import com.example.securebankingapp.presentation.login.LoginScreen
import com.example.securebankingapp.presentation.login.LoginViewModel
import com.example.securebankingapp.presentation.loginWithBits.InputLoginBitsScreen
import com.example.securebankingapp.presentation.loginWithBits.InputLoginBitsScreenEvent
import com.example.securebankingapp.presentation.loginWithBits.InputLoginBitsViewModel
import com.example.securebankingapp.presentation.profile.ProfileEvent
import com.example.securebankingapp.presentation.profile.ProfileScreen
import com.example.securebankingapp.presentation.profile.ProfileViewModel
import com.example.securebankingapp.presentation.register.RegisterScreen
import com.example.securebankingapp.presentation.register.RegisterViewModel
import com.example.securebankingapp.presentation.requestBitsToLogin.RequestBitsToLoginViewModel
import com.example.securebankingapp.presentation.requestBitsToLogin.RequestLoginWithBitsScreen
import com.example.securebankingapp.presentation.usersList.UsersListEvent
import com.example.securebankingapp.presentation.usersList.UsersListScreen
import com.example.securebankingapp.presentation.usersList.UsersListViewModel
import com.example.securebankingapp.presentation.withdraw.WithdrawMoneyScreen
import com.example.securebankingapp.presentation.withdraw.WithdrawMoneyViewModel

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
            val viewModel: HomeScreenViewModel by activity().viewModels()
            val viewState by viewModel.state.collectAsState()

            viewModel.onEvent(HomeScreenEvent.OnEnter)

            HomeScreen(
                viewState = viewState,
                onEvent = viewModel::onEvent
            )
        }
        composable<Destinations.InputLoginBits> {
            val viewModel: InputLoginBitsViewModel by activity().viewModels()
            val viewState by viewModel.state.collectAsState()
            val bits = this.loginBits
            val email = this.email

            LaunchedEffect(key1 = Unit) {
                viewModel.onEvent(InputLoginBitsScreenEvent.LoadInitialBits(bits))
                viewModel.setAssociatedEmail(email)
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
            val viewModel: ProfileViewModel by activity().viewModels()
            val viewState by viewModel.state.collectAsState()
            val userId = this.userId

            LaunchedEffect(Unit) {
                viewModel.onEvent(ProfileEvent.FetchInitialData(userId))
            }

            ProfileScreen(
                viewState = viewState,
                onEvent = viewModel::onEvent
            )
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
            val viewModel: UsersListViewModel by activity().viewModels()
            val viewState by viewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.onEvent(UsersListEvent.FetchInitialUsers)
            }

            UsersListScreen(
                viewState = viewState,
                senderInfo = this.viewerInfo,
                onEvent = viewModel::onEvent
            )
        }

        composable<Destinations.DepositMoneyScreen> {
            val viewModel: DepositMoneyViewModel by activity().viewModels()
            val viewState by viewModel.state.collectAsState()

            DepositMoneyScreen(
                viewState = viewState,
                onEvent = viewModel::onEvent
            )
        }

        composable<Destinations.WithdrawMoneyScreen> {
            val viewModel: WithdrawMoneyViewModel by activity().viewModels()
            val viewState by viewModel.state.collectAsState()

            WithdrawMoneyScreen(
                viewState = viewState,
                onEvent = viewModel::onEvent
            )
        }
    }
}
