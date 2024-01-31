package com.example.securebankingapp.data.services

import com.example.securebankingapp.data.api.ApiService
import com.example.securebankingapp.data.api.PrivateUserModel
import com.example.securebankingapp.data.api.SecureUserModelWithId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersService @Inject constructor(
    private val apiService: ApiService,
    private val accountStateService: AccountStateService
) {
    suspend fun getUsers():List<SecureUserModelWithId> {
        try {
            return emptyList()
        } catch (e : Exception) {
            return emptyList()
        }
    }
    suspend fun getUserDataWithId(id: Int): PrivateUserModel? {
        try {
//            val response = apiService.getUser(id)
//            return response.data
            return null

        } catch (e : Exception) {
            return null
        }
    }

    suspend fun getUserIdWithEmail(token: String, email: String): Int? {
        try {
//            val response = apiService.getUserIdWithEmail(token, email)
//            return response.data
            return null
        } catch (e : Exception) {
            return null
        }

    }
}