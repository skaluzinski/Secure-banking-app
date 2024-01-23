package com.example.securebankingapp.presentation.home

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
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
            is HomeScreenEvent.ToUserProfile -> destinationsRelay.navigateTo(Destinations.Profile(event.userId))
        }
    }

}

sealed class HomeScreenEvent : ViewModelEvent {
    data class ToUsersList(val userId: Int): HomeScreenEvent()
    data class ToUserProfile(val userId: Int): HomeScreenEvent()
}

data class HomeScreenViewState(
    val placeholder: Boolean = false
) : ViewModelState {
}
