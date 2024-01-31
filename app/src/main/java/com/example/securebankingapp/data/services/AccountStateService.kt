package com.example.securebankingapp.data.services

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountStateService @Inject constructor() {
    private val _accountState:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val accountState = _accountState.asStateFlow()

    fun updateState(newState: Boolean) {
        runBlocking {
            _accountState.emit(newState)
        }
    }
}