package com.example.securebankingapp.presentation.depositMoney

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.TransactionRepository
import com.example.securebankingapp.data.api.PrivateUserModel
import com.example.securebankingapp.data.api.UserModel
import com.example.securebankingapp.data.services.AccountService
import com.example.securebankingapp.data.services.UsersService
import com.example.securebankingapp.domain.DepositRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositMoneyViewModel @Inject constructor(
    private val usersService: UsersService,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : BaseViewModel<DepositMoneyViewState, DepositMoneyEvent>(initialState = DepositMoneyViewState()) {

    init {
        refreshBalance()
    }

    override fun handleEvent(event: DepositMoneyEvent) {
        when(event) {
             is DepositMoneyEvent.DepositMoneyClicked -> viewModelScope.launch{
                 updateState { it.copy(isTransactionInProgress = true) }
                 transactionRepository.depositMoney(DepositRequest(event.amount))
                 refreshBalance()
                 updateState { it.copy(isTransactionInProgress = false) }
             }
        }
    }

    private fun refreshBalance() {
        viewModelScope.launch {
            val account  = accountRepository.getAccountModel()
            if (account != null) {
                println("### ${account.balance}")
                updateState { it.copy(senderInfo = account) }
            }
        }
    }
}

sealed class DepositMoneyEvent: ViewModelEvent {
    data class DepositMoneyClicked(val amount: Float): DepositMoneyEvent()
}

data class DepositMoneyViewState(
    val senderInfo: UserModel = UserModel(
        email = "",
        name = "",
        balance = 0f,
        transactions = emptyList(),
        cardNumber = ""
    ),
    val isTransactionInProgress : Boolean = false
): ViewModelState

