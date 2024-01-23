package com.example.securebankingapp.data.services

import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.api.ApiService
import com.example.securebankingapp.data.common.ApiResponse
import com.example.securebankingapp.data.common.MyResponse
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
            println("### e $e ${e.stackTraceToString()}")
            println("### ${e.cause}")
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
            println("### e $e ${e.stackTraceToString()}")
            println("### ${e.cause}")
            return null
        }
    }

    suspend fun loginWithBits(request: LoginWithBitsRequest): String? {
        try {
            val response = apiService.loginWithBits(request)

            println("### ${response.body()}")
//            println("message = ${response.message}")
//            val token = response?.data["token"] ?: return  null
//            return token
            return null

        } catch (e: Exception) {
            println("### e $e ${e.stackTraceToString()}")
            println("### ${e.cause}")
            return null
        }

    }

//    suspend fun register(registerRequest: RegisterRequest): Boolean {
//        return try {
//            val response = apiService.createUser(registerRequest)
//            when {
//                response.isSuccessful -> ApiResponse.Success("Registration successful")
//                else -> {
//                    val errorBody = response.errorBody()?.string()
//                    ApiResponse.Error(errorBody ?: "Registration failed")
//                }
//            }
//        } catch (e: Exception) {
//            ApiResponse.Error("Failed to connect to the server")
//        }
//    }

    // ... other functions ...

}
