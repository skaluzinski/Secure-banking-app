package com.example.securebankingapp.di

import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.services.AccountService
import com.example.securebankingapp.data.services.AccountStateService
import com.example.securebankingapp.data.services.UsersService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:8080"
    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideHttpClient(
        accountStateService: AccountStateService
    ): HttpClient {
        return HttpClient(CIO) {
            install(HttpCookies)
            install(ContentNegotiation) {
                json()
            }
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            HttpResponseValidator {
                validateResponse { response: HttpResponse ->
                    println("### response$response")
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideAccountRepository(accountService: AccountService, accountStateService: AccountStateService, usersService: UsersService): AccountRepository {
        return AccountRepository(accountService, accountStateService, usersService)
    }

    @Provides
    @Singleton
    fun provideAuthTokenService(): AccountStateService {
        return AccountStateService()
    }

//    @Provides
//    fun provideEncryptedDataStorage() {
//
//    }
}