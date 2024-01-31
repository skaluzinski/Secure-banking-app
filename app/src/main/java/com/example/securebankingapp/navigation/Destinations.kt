package com.example.securebankingapp.navigation

import com.example.securebankingapp.data.api.PrivateUserModel
import com.example.securebankingapp.presentation.depositMoney.DepositMoneyEvent
import com.kiwi.navigationcompose.typed.Destination
import kotlinx.serialization.Serializable
import java.util.jar.Attributes.Name

sealed interface Destinations: Destination {
    @Serializable
    data object Home : Destinations

    @Serializable
    data class Profile(val userId: Int) : Destinations

    @Serializable
    data class UserList(val viewerInfo: PrivateUserModel) : Destinations

    @Serializable
    data object Login : Destinations

    @Serializable
    data object Register : Destinations

    @Serializable
    data object RequestBits : Destinations

    @Serializable
    data class InputLoginBits(val email: String, val loginBits: List<Int>) : Destinations

    @Serializable
    data object DepositMoneyScreen : Destinations

    @Serializable
    data object WithdrawMoneyScreen : Destinations
}