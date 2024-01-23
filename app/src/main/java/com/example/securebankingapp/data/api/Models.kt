package com.example.securebankingapp.data.api

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserModel(
    val email: String?,
    val password: String,
)

@Serializable
data class RegisterModel(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)

@Serializable
data class SecureUserModelWithId(
    val name: String,
    val email: String,
    val id: Int
)

@Serializable
data class PrivateUserModel(
    val name: String,
    val email: String,
    val balance: Float,
    val accountNumber: String
)