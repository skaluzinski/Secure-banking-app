package com.example.securebankingapp.data

import com.example.securebankingapp.data.api.UserModel
import com.example.securebankingapp.data.services.AccountService
import com.example.securebankingapp.data.services.AccountStateService
import com.example.securebankingapp.data.services.UsersService
import com.example.securebankingapp.domain.EmailRequest
import com.example.securebankingapp.domain.LoginRequest
import com.example.securebankingapp.domain.LoginWithBitsRequest
import com.example.securebankingapp.domain.RegisterRequest
import javax.inject.Inject
import javax.inject.Singleton

enum class AccountState {
    LOGGED_IN, LOGGED_OUT
}

@Singleton
class AccountRepository @Inject constructor(
    private val accountService: AccountService,
    private val accountStateService: AccountStateService,
    private val usersService: UsersService
) {
    var currentBitsToLogin = emptyList<Int>()
        private set

    var associatedEmailWithBits = ""
        private set

    suspend fun tryToLogin(email:String, password: String): Boolean {
        val loginSuccess = accountService.tryToLogin(LoginRequest(email, password)) == true
        accountStateService.updateState(loginSuccess)
        return loginSuccess
    }

    suspend fun logout(): Boolean {
        val logoutSuccess = accountService.logout()
        accountStateService.updateState(false)
        return logoutSuccess
    }

    suspend fun getAccountModel(): UserModel? {
        return accountService.getAccountModel()
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
        accountStateService.updateState(true)
        return token.isNotEmpty()
    }
}