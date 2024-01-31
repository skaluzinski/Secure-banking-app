package com.example.securebankingapp.data

import com.example.securebankingapp.data.api.ApiService
import com.example.securebankingapp.domain.DepositRequest
import com.example.securebankingapp.domain.SendMoneyRequest
import com.example.securebankingapp.domain.WithdrawRequest
import com.example.securebankingapp.navigation.ToastModel
import com.example.securebankingapp.navigation.ToastRelay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val apiService: ApiService,
    private val toastRelay: ToastRelay
) {

    suspend fun depositMoney(depositRequest: DepositRequest): Boolean {
        try {
            val response = apiService.depositMoney(depositRequest)

            if (response.errorMessage != null) {
                toastRelay.showToast(
                    ToastModel(
                        message = response.errorMessage.split(":").getOrNull(1) ?: "error"
                    )
                )
            }
            return response.data?.operationSuccess == true
        } catch (e: Exception) {
            toastRelay.showToast(ToastModel(message = e.message.takeIf { it?.isNotEmpty() ?: false } ?: "Unknown Error"))
            return false
        }
    }

    suspend fun withdrawMoney(withdrawRequest: WithdrawRequest): Boolean {
        try {
            val response = apiService.withdrawMoney(withdrawRequest)


            if (response.errorMessage != null) {
                toastRelay.showToast(
                    ToastModel(
                        message = response.errorMessage.split(":").getOrNull(1) ?: "error"
                    )
                )
            }
            return response.data?.operationSuccess == true
        } catch (e: Exception) {
            toastRelay.showToast(ToastModel(message = e.message.takeIf { it?.isNotEmpty() ?: false } ?: "Unknown Error"))
            return false
        }
    }

    suspend fun sendMoney(sendMoneyRequest: SendMoneyRequest): Boolean {
        try {
            val response = apiService.sendMoney(sendMoneyRequest)


            if (response.errorMessage != null) {
                toastRelay.showToast(
                    ToastModel(
                        message = response.errorMessage.split(":").getOrNull(1) ?: "error"
                    )
                )
            }
            return response.data?.operationSuccess == true
        } catch (e: Exception) {
            toastRelay.showToast(ToastModel(message = e.message.takeIf { it?.isNotEmpty() ?: false } ?: "Unknown Error"))
            return false
        }
    }


}