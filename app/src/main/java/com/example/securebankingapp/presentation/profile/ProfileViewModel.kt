package com.example.securebankingapp.presentation.profile

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.api.PrivateUserModel
import com.example.securebankingapp.data.services.UsersService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val usersService: UsersService
) : BaseViewModel<ProfileViewState, ProfileEvent>(initialState = ProfileViewState()) {

    override fun handleEvent(event: ProfileEvent) {
        when(event) {
            is ProfileEvent.FetchInitialData -> viewModelScope.launch {
                val initialData = usersService.getUserDataWithId(event.userId) ?: state.value.user
                updateState { it.copy(user = initialData) }
            }
        }
    }
}

sealed class ProfileEvent: ViewModelEvent {
    data class FetchInitialData(val userId: Int): ProfileEvent()
}

data class ProfileViewState(
    val user: PrivateUserModel = PrivateUserModel(
        email = "",
        name = "",
        balance = 0f,
        accountNumber = ""
    )
): ViewModelState

