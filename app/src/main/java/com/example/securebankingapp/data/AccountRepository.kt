package com.example.securebankingapp.data

import com.example.securebankingapp.data.services.AccountService
import com.example.securebankingapp.domain.EmailRequest
import com.example.securebankingapp.domain.LoginRequest
import com.example.securebankingapp.domain.LoginWithBitsRequest
import javax.inject.Inject
import javax.inject.Singleton

enum class AccountState {
    LOGGED_IN, LOGGED_OUT
}

@Singleton
class AccountRepository @Inject constructor(
    private val accountService: AccountService
) {

    private var _token = ""
    var currentBitsToLogin = emptyList<Int>()
        private set

    var associatedEmailWithBits = ""
        private set

    fun saveUserToken(token: String) {
        _token = token
    }

    fun getUserToken(): String {
        return _token
    }

    private fun tryToRefreshUserToken() {

    }

    suspend fun loginUser(email:String, password: String) {
        _token = accountService.loginAndGetToken(
            LoginRequest(
                email,
                password
            )
        ) ?: ""
    }

    suspend fun requestBitsToLogin(email: String): List<Int> {
        currentBitsToLogin = accountService.requestBitsToLogin(EmailRequest(email)) ?: emptyList()
        associatedEmailWithBits = email
        println("### $currentBitsToLogin")
        return currentBitsToLogin
    }

    suspend fun loginWithBitsAndReturnSuccess(email: String, indexedBits: Map<Int, Char>) : Boolean {
        _token = accountService.loginWithBits(
            request = LoginWithBitsRequest(
                email = email,
                bits = indexedBits
            )
        ) ?: ""
        return _token.isNotEmpty()
    }

    fun logout() {

    }
}