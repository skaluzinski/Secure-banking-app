package com.example.securebankingapp.presentation.home

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.api.RevisedTransaction
import com.example.securebankingapp.data.services.UsersService
import com.example.securebankingapp.navigation.Destinations
import com.example.securebankingapp.navigation.DestinationsRelay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val destinationsRelay: DestinationsRelay,
    private val accountRepository: AccountRepository,
    private val usersService: UsersService
) : BaseViewModel<HomeScreenViewState, HomeScreenEvent>(initialState = HomeScreenViewState()) {
    override fun handleEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.ToUsersList -> {
                viewModelScope.launch {
                    val viewer = usersService.getUserDataWithId(event.userId) ?: return@launch
                    destinationsRelay.navigateTo(Destinations.UserList(viewer))
                }
            }
            is HomeScreenEvent.ToUserProfile -> destinationsRelay.navigateTo(Destinations.Profile)
            HomeScreenEvent.Logout -> {
                viewModelScope.launch {
                    accountRepository.logout()
                    destinationsRelay.navigateTo(Destinations.Login)
                }
            }

            HomeScreenEvent.ToDeposit -> viewModelScope.launch {
                destinationsRelay.navigateTo(Destinations.DepositMoneyScreen)
            }

            HomeScreenEvent.ToSendMoney -> viewModelScope.launch {
                destinationsRelay.navigateTo(Destinations.SendMoneyScreen)
            }

            HomeScreenEvent.ToWithdraw -> viewModelScope.launch {
                destinationsRelay.navigateTo(Destinations.WithdrawMoneyScreen)
            }

            HomeScreenEvent.OnEnter -> viewModelScope.launch {
                val account = accountRepository.getAccountModel()
                if (account != null) {
                    updateState {
                        it.copy(
                            name = account.name,
                            balance = account.balance,
                            cardNumber = "111211137",
                            transactions = account.transactions
                        )
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            val account = accountRepository.getAccountModel()
            if (account != null) {
                updateState {
                    it.copy(
                        name = account.name,
                        balance = account.balance,
                        cardNumber = "111211137",
                        transactions = account.transactions
                    )
                }
            }
        }
    }

}

sealed class HomeScreenEvent : ViewModelEvent {
    data class ToUsersList(val userId: Int): HomeScreenEvent()
    data object ToUserProfile : HomeScreenEvent()
    data object OnEnter: HomeScreenEvent()
    data object Logout : HomeScreenEvent()
    data object ToWithdraw: HomeScreenEvent()
    data object ToDeposit: HomeScreenEvent()
    data object ToSendMoney: HomeScreenEvent()
}

data class HomeScreenViewState(
    val placeholder: Boolean = false,
    val name: String = "",
    val cardNumber: String = "",
    val balance: Float = 1337.21f,
    val transactions: List<RevisedTransaction> = emptyList()
) : ViewModelState {
}
