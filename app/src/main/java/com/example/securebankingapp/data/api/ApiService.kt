package com.example.securebankingapp.data.api

import com.example.securebankingapp.data.common.MyResponse
import com.example.securebankingapp.domain.DepositRequest
import com.example.securebankingapp.domain.EmailRequest
import com.example.securebankingapp.domain.LoginRequest
import com.example.securebankingapp.domain.LoginWithBitsRequest
import com.example.securebankingapp.domain.RegisterRequest
import com.example.securebankingapp.domain.SendMoneyRequest
import com.example.securebankingapp.domain.WithdrawRequest
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


@Serializable
data class ApiResponse<T>( val message: String?, val data: T)

interface  ApiService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<Map<String, String>>

    @POST("/request_login_with_bits")
    suspend fun requestLoginWithBits(@Body emailRequest: EmailRequest): ApiResponse<Map<String, List<Int>>>

    @POST("/login_with_bits")
    suspend fun loginWithBits(@Body loginWithBitsRequest: LoginWithBitsRequest): Response<Any>

    @POST("/create_user")
    suspend fun createUser(@Body registerRequest: RegisterRequest): Response<Unit>

    @POST("/transactions/deposit")
    suspend fun depositMoney(@Body depositRequest: DepositRequest): Response<MyResponse>

    @POST("/transactions/withdraw")
    suspend fun withdrawMoney(@Body withdrawRequest: WithdrawRequest): Response<MyResponse>

    @POST("/transactions/send_money")
    suspend fun sendMoney(@Body sendMoneyRequest: SendMoneyRequest): Response<MyResponse>
}