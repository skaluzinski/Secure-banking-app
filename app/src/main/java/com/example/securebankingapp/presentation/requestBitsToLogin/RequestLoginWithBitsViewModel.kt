package com.example.securebankingapp.presentation.requestBitsToLogin

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.domain.EmailValidationError
import com.example.securebankingapp.navigation.Destinations
import com.example.securebankingapp.navigation.DestinationsRelay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestBitsToLoginViewModel @Inject constructor(
    private val destinationsRelay: DestinationsRelay,
    private val accountRepository: AccountRepository
) : BaseViewModel<RequestLoginWithBitViewState, RequestLoginWithBitsScreenEvent>(initialState = RequestLoginWithBitViewState()) {

    override fun handleEvent(event: RequestLoginWithBitsScreenEvent) {
        when (event) {
            RequestLoginWithBitsScreenEvent.AskForBits -> viewModelScope.launch {
                val bitsResponse = accountRepository.requestBitsToLogin(state.value.email)
                if (bitsResponse.isNotEmpty()) {
                    destinationsRelay.navigateTo(Destinations.InputLoginBits(bitsResponse))
                }
            }
            is RequestLoginWithBitsScreenEvent.EmailChanged -> validateEmail(event.email)
        }

    }

    private fun isValidEmailFormat(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private val validCharacters =
        setOf("A".."Z", "a".."z", '@', '0'..'9', '#', '?', '!', '@', '$', '%', '^', '&', '*', '.')

    private fun validateEmail(email: String) {
        val emailValidationError = when {
            email.isEmpty() -> EmailValidationError.EMPTY_FIELD
            !isValidEmailFormat(email) -> EmailValidationError.INVALID_FORMAT
            email.length < 8 -> EmailValidationError.TOO_SHORT
            email.length >= 32 -> EmailValidationError.TOO_LONG
            containsSqlInjection(email) -> EmailValidationError.SQL_INJECTION
            containsComma(email) -> EmailValidationError.CONTAINS_COMMA
            else -> null
        }
        updateState {
            it.copy(
                email = email,
                emailValidationError = emailValidationError,
                emailInvalidCharacters = email.filter {
                    android.util.Patterns.EMAIL_ADDRESS.matcher(
                        it.toString()
                    ).matches()
                }.toList()
            )
        }
    }

    private fun containsSqlInjection(text: String): Boolean {
        return text.contains(";") || text.contains("--") || text.contains("DROP", ignoreCase = true)
    }

    private fun containsComma(text: String): Boolean {
        return text.contains(",")
    }
}

sealed class RequestLoginWithBitsScreenEvent : ViewModelEvent {
    data object AskForBits : RequestLoginWithBitsScreenEvent()
    data class EmailChanged(val email: String) : RequestLoginWithBitsScreenEvent()
}

data class RequestLoginWithBitViewState(
    val email: String = "",
    val emailValidationError: EmailValidationError? = EmailValidationError.EMPTY_FIELD,
    val emailInvalidCharacters: List<Char> = emptyList(),
    val loginSuccess: Boolean = false
) : ViewModelState {
    val canAskForBits = emailValidationError == null
}