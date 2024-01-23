package com.example.securebankingapp.di

import android.content.Context
import com.example.securebankingapp.R
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.api.ApiService
import com.example.securebankingapp.data.api.AuthInterceptor
import com.example.securebankingapp.data.services.AccountService
import com.example.securebankingapp.data.services.AuthTokenService
import com.example.securebankingapp.data.services.UsersService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
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
    fun provideRetrofit(authTokenService: AuthTokenService, @ApplicationContext context: Context): Retrofit {
        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val original: Request = chain.request()

                val request: Request = original.newBuilder()
                    .header("Authorization", "Bearer ${authTokenService.authToken.value}")
                    .method(original.method(), original.body())
                    .build()
                val response = chain.proceed(request)
                if (response.code() == 401) {
                    authTokenService.updateToken(null)
                }

                response
            }
            val certInputStream: InputStream = context.resources.openRawResource(R.raw.cert)
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val certificate: X509Certificate = certificateFactory.generateCertificate(certInputStream) as X509Certificate

            val certificatePinner = CertificatePinner.Builder()
                .add(
                    "localhost",
                    CertificatePinner.pin(certificate)
                )
                .build()

            certificatePinner(certificatePinner)
        }


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideAuthInterceptor(authTokenService: AuthTokenService): AuthInterceptor {
        return AuthInterceptor(authTokenService)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    fun provideAccountRepository(accountService: AccountService, authTokenService: AuthTokenService, usersService: UsersService): AccountRepository {
        return AccountRepository(accountService, authTokenService, usersService)
    }

    @Provides
    @Singleton
    fun provideAuthTokenService(): AuthTokenService {
        return AuthTokenService()
    }
}