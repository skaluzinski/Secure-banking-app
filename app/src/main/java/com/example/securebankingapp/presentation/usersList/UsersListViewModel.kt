package com.example.securebankingapp.presentation.usersList

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.api.PrivateUserModel
import com.example.securebankingapp.data.api.SecureUserModelWithId
import com.example.securebankingapp.data.services.UsersService
import com.example.securebankingapp.navigation.Destinations
import com.example.securebankingapp.navigation.DestinationsRelay
import com.example.securebankingapp.presentation.depositMoney.DepositMoneyEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val usersService: UsersService,
    private val destinationsRelay: DestinationsRelay
) : BaseViewModel<UsersListViewState, UsersListEvent>(initialState = UsersListViewState()) {

    override fun handleEvent(event: UsersListEvent) {
        when(event) {
            UsersListEvent.FetchInitialUsers -> viewModelScope.launch {
                val initialData = usersService.getUsers()
                updateState { it.copy(users = initialData) }
            }

            is UsersListEvent.OnUserClicked -> destinationsRelay.navigateTo(Destinations.DepositMoneyScreen)
        }
    }
}

sealed class UsersListEvent: ViewModelEvent {
    data object FetchInitialUsers: UsersListEvent()
    data class OnUserClicked(
        val senderInfo: PrivateUserModel,
        val recipentId: Int,
        val recipentName: String
    ):UsersListEvent()
}

data class UsersListViewState(
    val users: List<SecureUserModelWithId> = emptyList()
) : ViewModelState

