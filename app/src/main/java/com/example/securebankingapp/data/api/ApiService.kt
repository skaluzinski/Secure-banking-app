package com.example.securebankingapp.data.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.securebankingapp.domain.DepositRequest
import com.example.securebankingapp.domain.EmailRequest
import com.example.securebankingapp.domain.LoginRequest
import com.example.securebankingapp.domain.LoginWithBitsRequest
import com.example.securebankingapp.domain.RegisterRequest
import com.example.securebankingapp.domain.SendMoneyRequest
import com.example.securebankingapp.domain.WithdrawRequest
import com.example.securebankingapp.navigation.DestinationsRelay
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class ApiResponse<T>(val errorMessage: String?, val data: T)

@Serializable
data class ApiDataResponse<T>(val errorMessage: String?, val data: T)

@Serializable
data class OperationData(@SerialName("operation_success") val operationSuccess: Boolean?)

@Serializable
data class ApiOperationResponse(
    val errorMessage: String?,
    val data: OperationData?
)

@OptIn(InternalAPI::class)
@Singleton
class ApiService @Inject constructor(
    private val destinationsRelay: DestinationsRelay,
    private val client: HttpClient
) {
    companion object {
        const val BASE_URL = "http://10.0.2.2:8080"
    }

    suspend fun login(loginRequest: LoginRequest): ApiOperationResponse {
        val request = client.post("$BASE_URL/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }

        println("### req ${request.bodyAsText()}")

        return request.body()
    }

    suspend fun logout(): ApiOperationResponse {
        return client.get("$BASE_URL/logout") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun requestLoginWithBits(emailRequest: EmailRequest): ApiResponse<Map<String, List<Int>>> {
        return client.post("$BASE_URL/request_login_with_bits") {
            contentType(ContentType.Application.Json)
            body = emailRequest
        }.body()
    }

    suspend fun loginWithBits(loginWithBitsRequest: LoginWithBitsRequest): HttpResponse {
        return client.post("$BASE_URL/login_with_bits") {
            contentType(ContentType.Application.Json)
            body = loginWithBitsRequest
        }.body()
    }

    suspend fun createUser(registerRequest: RegisterRequest): ApiOperationResponse {
        return client.post("$BASE_URL/create_user") {
            contentType(ContentType.Application.Json)
            body = registerRequest
        }.body()
    }

    suspend fun depositMoney(depositRequest: DepositRequest): ApiOperationResponse {
        return client.post("$BASE_URL/transactions/deposit") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(depositRequest)
        }.body()
    }

    suspend fun userInfo(): ApiDataResponse<UserModel> {
        return client.get("$BASE_URL/user").body()
    }

    suspend fun withdrawMoney(withdrawRequest: WithdrawRequest): ApiOperationResponse {
        return client.post("$BASE_URL/transactions/withdraw") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(withdrawRequest)
        }.body()
    }

    suspend fun sendMoney(sendMoneyRequest: SendMoneyRequest): ApiOperationResponse {
        return client.post("$BASE_URL/transactions/send_money") {
            body = sendMoneyRequest
        }.body()
    }

    suspend fun getUserGeneralInformation() {
        return client.get("$BASE_URL/user").body()
    }
}