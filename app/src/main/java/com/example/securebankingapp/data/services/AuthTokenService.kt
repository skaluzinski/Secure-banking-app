package com.example.securebankingapp.data.services

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenService @Inject constructor() {
    private val _authToken:MutableStateFlow<String?> = MutableStateFlow(null)
    val authToken = _authToken.asStateFlow()

    fun updateToken(newToken: String?) {
        MainScope().launch {
            _authToken.emit(newToken)
        }
    }
}