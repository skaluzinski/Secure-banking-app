package com.example.securebankingapp.data.api

import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.services.AuthTokenService
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(private val authTokenService: AuthTokenService) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .build()

        return chain.proceed(request)
    }
}