package com.example.securebankingapp.presentation.sendMoney

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.TransactionRepository
import com.example.securebankingapp.data.services.UsersService
import com.example.securebankingapp.domain.EmailValidationError
import com.example.securebankingapp.domain.SendMoneyRequest
import com.example.securebankingapp.domain.TitleValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendMoneyViewModel @Inject constructor(
    private val usersService: UsersService,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : BaseViewModel<SendMoneyViewState, SendMoneyEvent>(initialState = SendMoneyViewState()) {

    init {
        refreshBalance()
    }

    override fun handleEvent(event: SendMoneyEvent) {
        when(event) {
             is SendMoneyEvent.SendMoneyClicked -> viewModelScope.launch{
                 updateState { it.copy(isTransactionInProgress = true) }
                 transactionRepository.sendMoney(
                     SendMoneyRequest(
                         recipientEmail = state.value.email,
                         amount = event.amount,
                         title = state.value.transactionTitle
                     )
                 )
                 refreshBalance()
                 updateState { it.copy(isTransactionInProgress = false) }
             }
            is SendMoneyEvent.EmailChanged -> {
                val emailValidationError = validateEmail(event.newEmail)
                val invalidEmailCharacters = filterInvalidEmailCharacters(event.newEmail)
                updateState {
                    it.copy(
                        emailValidationError = emailValidationError,
                        emailInvalidCharacters = invalidEmailCharacters,
                        email = event.newEmail
                    )
                }
                if (state.value.emailValidationError == null) {
                    updateState {
                        it.copy(
                            repeatEmailValidationError = if (state.value.email != state.value.repeatEmail) EmailValidationError.EMAILS_DONT_MATCH else null
                        )
                    }
                }

            }
            is SendMoneyEvent.RepeatEmailChanged -> {
                val emailValidationError = validateEmail(event.newEmail)
                val invalidEmailCharacters = filterInvalidEmailCharacters(event.newEmail)
                updateState {
                    it.copy(
                        repeatEmailValidationError = emailValidationError,
                        repeatEmailInvalidCharacters = invalidEmailCharacters,
                        repeatEmail = event.newEmail
                    )
                }
                if (state.value.repeatEmailValidationError == null) {
                    updateState {
                        it.copy(
                            repeatEmailValidationError = if (state.value.email != state.value.repeatEmail) EmailValidationError.EMAILS_DONT_MATCH else null
                        )
                    }
                }
            }

            is SendMoneyEvent.OnTitleChanged -> {
                val titleValidationError = validateTitle(event.newTitle)
                val invalidTitleCharacters = filterInvalidTitleCharacters(event.newTitle)

                updateState {
                    it.copy(
                        transactionTitle = event.newTitle,
                        titleValidationErrors = titleValidationError,
                        invalidTitleCharacters = invalidTitleCharacters
                    )
                }
            }
        }
    }

    private fun refreshBalance() {
        viewModelScope.launch {
            val account = accountRepository.getAccountModel()
            if (account != null) {
                updateState {
                    it.copy(
                        senderBalance = account.balance,
                        senderEmail = account.email
                    )
                }
            }
        }
    }

    private fun validateEmail(email: String) = when {
        email.isEmpty() -> EmailValidationError.EMPTY_FIELD
        !isValidEmailFormat(email) -> EmailValidationError.INVALID_FORMAT
        email.length < 8 -> EmailValidationError.TOO_SHORT
        email.length >= 32 -> EmailValidationError.TOO_LONG
        containsSqlInjection(email) -> EmailValidationError.SQL_INJECTION
        containsComma(email) -> EmailValidationError.CONTAINS_COMMA
        else -> null
    }

    private fun validateTitle(title: String) = when {
        title.isEmpty() -> TitleValidationError.EMPTY_FIELD
        !isValidTitleFormat(title) -> TitleValidationError.INVALID_FORMAT
        title.length >= 64 -> TitleValidationError.TOO_LONG
        containsSqlInjection(title) -> TitleValidationError.SQL_INJECTION
        containsComma(title) -> TitleValidationError.CONTAINS_COMMA
        else -> null
    }


    private fun filterInvalidEmailCharacters(email: String): List<Char> {
        return email.filter { android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()}.toList()
    }

    private fun filterInvalidTitleCharacters(email: String): List<Char> {
        val transactionTitlePattern = Regex("^[a-zA-Z0-9\\s\\-_,.!@#\$%^&*()+=?<>:;{}\\[\\]]+\$")

        return email.filter { transactionTitlePattern.matches(it.toString()) }.toList()
    }


    private fun containsComma(text: String): Boolean {
        return text.contains(",")
    }

    private fun containsSqlInjection(text: String): Boolean {
        return text.contains(";") || text.contains("--") || text.contains("DROP", ignoreCase = true)
    }

    private fun isValidEmailFormat(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidTitleFormat(title: String): Boolean {
        val transactionTitlePattern = Regex("^[a-zA-Z0-9\\s\\-_,.!@#\$%^&*()+=?<>:;{}\\[\\]]+\$")
        return transactionTitlePattern.matches(title)
    }

}

sealed class SendMoneyEvent: ViewModelEvent {
    data class SendMoneyClicked(val amount: Float): SendMoneyEvent()
    data class EmailChanged(val newEmail: String) : SendMoneyEvent()
    data class OnTitleChanged(val newTitle: String) : SendMoneyEvent()
    data class RepeatEmailChanged(val newEmail: String) : SendMoneyEvent()
}

data class SendMoneyViewState(
    val senderEmail: String = "",
    val senderBalance: Float = 0f,
    val isTransactionInProgress: Boolean = false,
    val transactionTitle: String = "",
    val email: String = "",
    val repeatEmail: String = "",
    val titleValidationErrors: TitleValidationError? = TitleValidationError.EMPTY_FIELD,
    val invalidTitleCharacters: List<Char> = emptyList(),
    val emailValidationError: EmailValidationError? = EmailValidationError.EMPTY_FIELD,
    val emailInvalidCharacters: List<Char> = emptyList(),
    val repeatEmailValidationError: EmailValidationError? = EmailValidationError.EMPTY_FIELD,
    val repeatEmailInvalidCharacters: List<Char> = emptyList(),

    ): ViewModelState {
    val canSend = repeatEmailValidationError == null
            && emailValidationError == null
            && titleValidationErrors == null
    }

