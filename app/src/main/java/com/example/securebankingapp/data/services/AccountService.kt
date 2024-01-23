package com.example.securebankingapp.data.services

import com.example.securebankingapp.data.api.ApiService
import com.example.securebankingapp.data.common.ApiResponse
import com.example.securebankingapp.domain.EmailRequest
import com.example.securebankingapp.domain.LoginRequest
import com.example.securebankingapp.domain.LoginWithBitsRequest
import com.example.securebankingapp.domain.RegisterRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountService @Inject constructor(
    private val apiService: ApiService,
) {

    suspend fun loginAndGetToken(loginRequest: LoginRequest): String? {
        try {
            val response = apiService.login(loginRequest)
            val token = response.data["token"]
            return token
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun requestBitsToLogin(emailRequest: EmailRequest): List<Int>? {
        try {
            val response = apiService.requestLoginWithBits(emailRequest)
            val bits = response.data["bits"] ?: throw IllegalArgumentException("Didn't find any bits to login")

            if (bits.isEmpty()) {
                throw Exception("Empty bits")
            }

            return bits

        } catch (e: Exception) {
            return null
        }
    }

    suspend fun loginWithBits(request: LoginWithBitsRequest): String? {
        try {
            val response = apiService.loginWithBits(request)

//            println("message = ${response.message}")
//            val token = response?.data["token"] ?: return  null
//            return token
            return null

        } catch (e: Exception) {
            return null
        }

    }

    suspend fun register(registerRequest: RegisterRequest): Boolean {
        try {
            val response = apiService.createUser(registerRequest)

            return  response.errorMessage == null && response.data.first == "success" && response.data.second
        } catch (e: Exception) {
            return false
        }
    }

    // ... other functions ...

}
