package com.example.securebankingapp.data.services

import com.example.securebankingapp.data.api.ApiService
import com.example.securebankingapp.data.api.UserModel
import com.example.securebankingapp.domain.EmailRequest
import com.example.securebankingapp.domain.LoginRequest
import com.example.securebankingapp.domain.LoginWithBitsRequest
import com.example.securebankingapp.domain.RegisterRequest
import com.example.securebankingapp.navigation.Destinations
import com.example.securebankingapp.navigation.DestinationsRelay
import com.example.securebankingapp.navigation.ToastModel
import com.example.securebankingapp.navigation.ToastRelay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountService @Inject constructor(
    private val apiService: ApiService,
    private val toastRelay: ToastRelay,
    private val destinationsRelay: DestinationsRelay
) {

    suspend fun tryToLogin(loginRequest: LoginRequest): Boolean? {
        try {
            val response = apiService.login(loginRequest)

            if (response.errorMessage != null) {
                if (response.errorMessage.contains("OPERATION_NEEDS_RECENT_LOGIN")) {
                    destinationsRelay.navigateTo(Destinations.Login)
                }
                toastRelay.showToast(ToastModel(message = response.errorMessage.split(":").getOrNull(1) ?: "error"))
            }

            return response.data?.operationSuccess
        } catch (e: Exception) {
            toastRelay.showToast(ToastModel(message = e.message.takeIf { it?.isNotEmpty() ?: false } ?: "Unknown Error"))
            return null
        }
    }

    suspend fun logout(): Boolean {
        try {
            val response = apiService.logout()

            if (response.errorMessage != null) {
                if (response.errorMessage.contains("OPERATION_NEEDS_RECENT_LOGIN")) {
                    destinationsRelay.navigateTo(Destinations.Login)
                }
                toastRelay.showToast(ToastModel(message = response.errorMessage.split(":").getOrNull(1) ?: "error"))
            }

            return response.data?.operationSuccess == true
        } catch (e: Exception) {
            toastRelay.showToast(ToastModel(message = e.message.takeIf { it?.isNotEmpty() ?: false } ?: "Unknown Error"))
            return false
        }
    }

    suspend fun getAccountModel(): UserModel? {
        try {
            val response = apiService.userInfo()

            if (response.errorMessage != null) {
                if (response.errorMessage.contains("OPERATION_NEEDS_RECENT_LOGIN")) {
                    destinationsRelay.navigateTo(Destinations.Login)
                }

                toastRelay.showToast(ToastModel(message = response.errorMessage.split(":").getOrNull(1) ?: "error"))
            }

            println("### acount model : ${response.data}")

            return response.data
        } catch (e: Exception) {
            toastRelay.showToast(ToastModel(message = e.message.takeIf { it?.isNotEmpty() ?: false } ?: "Unknown Error"))
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

            if (response.errorMessage != null) {
                if (response.errorMessage.contains("OPERATION_NEEDS_RECENT_LOGIN")) {
                    destinationsRelay.navigateTo(Destinations.Login)
                }

                toastRelay.showToast(ToastModel(message = response.errorMessage.split(":").getOrNull(1) ?: "error"))
            }

            return bits

        } catch (e: Exception) {
            toastRelay.showToast(ToastModel(message = e.message.takeIf { it?.isNotEmpty() ?: false } ?: "Unknown Error"))
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
            toastRelay.showToast(ToastModel(message = e.message.takeIf { it?.isNotEmpty() ?: false } ?: "Unknown Error"))
            return null
        }

    }

    suspend fun register(registerRequest: RegisterRequest): Boolean {
        try {
            val response = apiService.createUser(registerRequest)

            if (response.errorMessage != null) {
                if (response.errorMessage.contains("OPERATION_NEEDS_RECENT_LOGIN")) {
                    destinationsRelay.navigateTo(Destinations.Login)
                }

                toastRelay.showToast(ToastModel(message = response.errorMessage.split(":").getOrNull(1) ?: "error"))
            }

            return response.data?.operationSuccess == true
        } catch (e: Exception) {
            toastRelay.showToast(ToastModel(message = e.message.takeIf { it?.isNotEmpty() ?: false } ?: "Unknown Error"))
            return false
        }
    }
}
