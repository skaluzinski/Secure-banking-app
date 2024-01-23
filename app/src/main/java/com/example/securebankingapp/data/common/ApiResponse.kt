package com.example.securebankingapp.data.common

interface

data class ApiResponse<T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): ApiResponse<T> {
            return ApiResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String): ApiResponse<T> {
            return ApiResponse(Status.ERROR, null, message)
        }

        fun <T> loading(): ApiResponse<T> {
            return ApiResponse(Status.LOADING, null, null)
        }
    }
}
