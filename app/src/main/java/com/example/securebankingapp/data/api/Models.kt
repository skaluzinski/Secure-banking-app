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