package com.example.securebankingapp.presentation.profile

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.api.PrivateUserModel
import com.example.securebankingapp.data.api.UserModel
import com.example.securebankingapp.data.services.UsersService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseViewModel<ProfileViewState, ProfileEvent>(initialState = ProfileViewState()) {

    override fun handleEvent(event: ProfileEvent) {
        when(event) {
            is ProfileEvent.FetchInitialData -> viewModelScope.launch {
                val initialData = accountRepository.getAccountModel()
                if (initialData != null) {
                    updateState { it.copy(user = initialData) }
                }
            }
        }
    }
}

sealed class ProfileEvent: ViewModelEvent {
    data object FetchInitialData: ProfileEvent()
}

data class ProfileViewState(
    val user: UserModel = UserModel(
        email = "",
        name = "",
        balance = 0f,
        cardNumber = "",
        transactions = emptyList()
    )
): ViewModelState

