package com.example.securebankingapp.navigation

import com.kiwi.navigationcompose.typed.Destination
import kotlinx.serialization.Serializable

sealed interface Destinations: Destination {
    @Serializable
    data object Home: Destinations

    @Serializable
    data object Profile: Destinations

    @Serializable
    data object UserList: Destinations

    @Serializable
    data object Login: Destinations

    @Serializable
    data object Register: Destinations

    @Serializable
    data object RequestBits: Destinations

    @Serializable
    data class InputLoginBits(val loginBits : List<Int>): Destinations
}