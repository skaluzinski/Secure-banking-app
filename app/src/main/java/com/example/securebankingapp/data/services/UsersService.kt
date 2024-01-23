package com.example.securebankingapp.data.services

import com.example.securebankingapp.data.api.ApiService
import com.example.securebankingapp.data.api.PrivateUserModel
import com.example.securebankingapp.data.api.SecureUserModelWithId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersService @Inject constructor(
    private val apiService: ApiService,
    private val authTokenService: AuthTokenService
) {
    suspend fun getUsers():List<SecureUserModelWithId> {
        try {
            println("### authToken: ${authTokenService.authToken.value}")
            val response = apiService.getUsers(authTokenService.authToken.value ?: "")
            println("### responseUsers: $response")
            val users = response.data
            return  users
        } catch (e : Exception) {
            println("### coulnd't get users: $e")
            println("### coulnd't get users: ${e.cause}")
            println("### coulnd't get users: ${e.message}")
            return emptyList()
        }
    }
    suspend fun getUserDataWithId(id: Int): PrivateUserModel? {
        try {
            val response = apiService.getUser(id)
            return response.data
        } catch (e : Exception) {
            return null
        }
    }

    suspend fun getUserIdWithEmail(email: String): Int? {
        try {
            val response = apiService.getUserIdWithEmail(email)
            return response.data
        } catch (e : Exception) {
            return null
        }

    }
}