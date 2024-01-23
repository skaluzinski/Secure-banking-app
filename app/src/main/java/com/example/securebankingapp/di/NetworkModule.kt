package com.example.securebankingapp.di

import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.api.ApiService
import com.example.securebankingapp.data.api.AuthInterceptor
import com.example.securebankingapp.data.services.AccountService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:8080"
    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    fun provideAuthInterceptor(accountRepository: AccountRepository): AuthInterceptor {
        return AuthInterceptor(accountRepository)
    }

    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    fun provideAccountRepository(accountService: AccountService): AccountRepository {
        return AccountRepository(accountService) // Assuming AccountRepositoryImpl implements AccountRepository
    }
}