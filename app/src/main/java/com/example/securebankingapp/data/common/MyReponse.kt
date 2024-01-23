package com.example.securebankingapp.data.common

data class MyResponse(
    val message: String,
    val status: Int,
    val data: Any? // Cursed, change before uploading to CV
)