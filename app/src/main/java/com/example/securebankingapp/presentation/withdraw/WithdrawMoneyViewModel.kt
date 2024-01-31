package com.example.securebankingapp.presentation.withdraw

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.TransactionRepository
import com.example.securebankingapp.data.api.UserModel
import com.example.securebankingapp.data.services.UsersService
import com.example.securebankingapp.domain.DepositRequest
import com.example.securebankingapp.domain.WithdrawRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawMoneyViewModel @Inject constructor(
    private val usersService: UsersService,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : BaseViewModel<WithdrawMoneyViewState, WithdrawMoneyEvent>(initialState = WithdrawMoneyViewState()) {

    init {
        refreshBalance()
    }

    override fun handleEvent(event: WithdrawMoneyEvent) {
        when(event) {
             is WithdrawMoneyEvent.WithdrawMoneyClicked -> viewModelScope.launch{
                 updateState { it.copy(isTransactionInProgress = true) }
                 println("### amount to withdraw : ${event.amount}")
                 transactionRepository.withdrawMoney(WithdrawRequest(event.amount))
                 refreshBalance()
                 updateState { it.copy(isTransactionInProgress = false) }
             }
        }
    }

    private fun refreshBalance() {
        viewModelScope.launch {
            val account  = accountRepository.getAccountModel()
            if (account != null) {
                updateState { it.copy(senderInfo = account) }
            }
        }
    }
}

sealed class WithdrawMoneyEvent: ViewModelEvent {
    data class WithdrawMoneyClicked(val amount: Float): WithdrawMoneyEvent()
}

data class WithdrawMoneyViewState(
    val senderInfo: UserModel = UserModel(
        email = "",
        name = "",
        balance = 0f,
        transactions = emptyList()
    ),
    val isTransactionInProgress : Boolean = false
): ViewModelState

