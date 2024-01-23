package com.example.securebankingapp.presentation.home

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.navigation.Destinations
import com.example.securebankingapp.navigation.DestinationsRelay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val destinationsRelay: DestinationsRelay,
    private val accountRepository: AccountRepository
) : BaseViewModel<HomeScreenViewState, HomeScreenEvent>(initialState = HomeScreenViewState()) {
    override fun handleEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.ToUsersList -> destinationsRelay.navigateTo(Destinations.UserList)
        }
    }

}

sealed class HomeScreenEvent : ViewModelEvent {
    data object ToUsersList: HomeScreenEvent()
}

data class HomeScreenViewState(
    val placeholder: Boolean = false
) : ViewModelState {
}
