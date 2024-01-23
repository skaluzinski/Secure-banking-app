package com.example.securebankingapp.presentation.usersList

import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
) : BaseViewModel<UserListViewState, UsersListEvent>(initialState = UserListViewState) {

    override fun handleEvent(event: UsersListEvent) {
        TODO("Not yet implemented")
    }
}

sealed class UsersListEvent: ViewModelEvent {
    data object TryToLogin: UsersListEvent()
}

data object UserListViewState : ViewModelState