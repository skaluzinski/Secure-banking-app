package com.example.securebankingapp.presentation.depositMoney

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.api.PrivateUserModel
import com.example.securebankingapp.data.services.UsersService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositMoneyViewModel @Inject constructor(
    private val usersService: UsersService
) : BaseViewModel<DepositMoneyViewState, DepositMoneyEvent>(initialState = DepositMoneyViewState()) {

    override fun handleEvent(event: DepositMoneyEvent) {
        when(event) {
            is DepositMoneyEvent.SetInitialData -> {
                viewModelScope.launch {
                    updateState { it.copy(senderInfo = event.sender) }
                }
            }

            is DepositMoneyEvent.SetRecipent ->
                updateState { it.copy(recipentId = event.recipentId, recipentName = event.recipentName) }
        }
    }
}

sealed class DepositMoneyEvent: ViewModelEvent {
    data class SetInitialData(val sender: PrivateUserModel): DepositMoneyEvent()
    data class SetRecipent(val recipentId: Int, val recipentName: String): DepositMoneyEvent()
}

data class DepositMoneyViewState(
    val senderInfo: PrivateUserModel = PrivateUserModel(
        email = "",
        name = "",
        balance = 0f,
        accountNumber = ""
    ),
    val recipentId: Int = 0,
    val recipentName:String = ""
): ViewModelState

