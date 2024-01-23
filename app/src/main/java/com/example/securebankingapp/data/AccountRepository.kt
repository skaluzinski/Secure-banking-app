package com.example.securebankingapp.data

import com.example.securebankingapp.data.services.AccountService
import com.example.securebankingapp.data.services.AuthTokenService
import com.example.securebankingapp.data.services.UsersService
import com.example.securebankingapp.domain.EmailRequest
import com.example.securebankingapp.domain.LoginRequest
import com.example.securebankingapp.domain.LoginWithBitsRequest
import com.example.securebankingapp.domain.RegisterRequest
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

enum class AccountState {
    LOGGED_IN, LOGGED_OUT
}

@Singleton
class AccountRepository @Inject constructor(
    private val accountService: AccountService,
    private val authTokenService: AuthTokenService,
    private val usersService: UsersService
) {

    var currentBitsToLogin = emptyList<Int>()
        private set

    var associatedEmailWithBits = ""
        private set

    suspend fun loginUser(email:String, password: String): Int? {
        val token = accountService.loginAndGetToken(
            LoginRequest(
                email,
                password
            )
        ) ?: ""

        authTokenService.updateToken(token)
        delay(100)
        return usersService.getUserIdWithEmail(token, email)
    }

    suspend fun registerUser(password: String, email: String, name: String): Boolean {
        return accountService.register(
            RegisterRequest(
                name = name,
                password = password,
                email = email
            )
        )
    }

    suspend fun requestBitsToLogin(email: String): List<Int> {
        currentBitsToLogin = accountService.requestBitsToLogin(EmailRequest(email)) ?: emptyList()
        associatedEmailWithBits = email
        return currentBitsToLogin
    }

    suspend fun loginWithBitsAndReturnSuccess(email: String, indexedBits: Map<Int, Char>) : Boolean {
        val token = accountService.loginWithBits(
            request = LoginWithBitsRequest(
                email = email,
                bits = indexedBits
            )
        ) ?: ""
        authTokenService.updateToken(token)
        return token.isNotEmpty()
    }

    fun logout() {

    }
}