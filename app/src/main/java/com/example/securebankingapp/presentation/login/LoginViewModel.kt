package com.example.securebankingapp.presentation.login

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.data.services.UsersService
import com.example.securebankingapp.domain.EmailValidationError
import com.example.securebankingapp.domain.PasswordValidationErrors
import com.example.securebankingapp.domain.debounce
import com.example.securebankingapp.navigation.Destinations
import com.example.securebankingapp.navigation.DestinationsRelay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOGIN_DEBOUNCE_DURATION = 300L

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val destinationsRelay: DestinationsRelay,
    private val accountRepository: AccountRepository,
    private val usersService: UsersService
) : BaseViewModel<LoginScreenViewState, LoginScreenEvent>(initialState = LoginScreenViewState()) {

    override fun handleEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.OnEmailChange -> validateEmail(event.newEmail)
            is LoginScreenEvent.OnPasswordChange -> validatePassword(event.newPassword)
            LoginScreenEvent.TryToRequest -> {
                viewModelScope.launch {
                    val userId = accountRepository.loginUser(
                        email = state.value.email,
                        password = state.value.password
                    )

                    if (userId != null) {
                        println("### userID $userId")
                        destinationsRelay.navigateTo(Destinations.Home(userId))
                    }
                }

                debounce<Unit>(LOGIN_DEBOUNCE_DURATION, viewModelScope.coroutineContext) {
                }
            }
            LoginScreenEvent.ProceedToRegister -> destinationsRelay.navigateTo(Destinations.Register)
            LoginScreenEvent.ProceedToLoginWithBits -> destinationsRelay.navigateTo(Destinations.RequestBits)
        }

    }

    private fun isValidEmailFormat(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private val validCharacters =
        setOf("A".."Z", "a".."z", '@','0'..'9', '#', '?', '!', '@', '$', '%', '^', '&', '*', '.', )

    private fun containsInvalidCharacters(password: String): Boolean {
        return password.any { it !in validCharacters }
    }

    val validPasswordCharacters = listOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '!', '@', '#', '$', '%', '^', '&', '*'
    )

    val specialLetters = listOf('!', '@', '#', '$', '%', '^', '&', '*')

    private fun validatePassword(password: String) {
        val passwordValidationErrors = when {
            password.isEmpty() -> PasswordValidationErrors.PASSWORD_EMPTY
            containsSqlInjection(password) -> PasswordValidationErrors.SQL_INJECTION
            password.any { !validPasswordCharacters.contains(it) } -> PasswordValidationErrors.INVALID_CHARACTERS
            password.length >= 32 -> PasswordValidationErrors.PASSWORD_TOO_LONG
            password.length < 8 -> PasswordValidationErrors.PASSWORD_TOO_SHORT
            password.none { it.isUpperCase() } -> PasswordValidationErrors.PASSWORD_NO_BIG_LETTER
            password.none { it.isLowerCase() } -> PasswordValidationErrors.PASSWORD_NO_SMALL_LETTER
            password.none { it in specialLetters} -> PasswordValidationErrors.PASSWORD_NO_SPECIAL_LETTER
            else -> null
        }
        updateState { viewState ->
            viewState.copy(
                password = password,
                passwordValidationErrors = passwordValidationErrors,
                passwordInvalidCharacters = password.filter { !validPasswordCharacters.contains(it) }.toList()
            )
        }
    }

    private fun validateEmail(email: String) {
        val emailValidationError = when {
            email.isEmpty() -> EmailValidationError.EMPTY_FIELD
//            !isValidEmailFormat(email) } -> EmailValidationError.INVALID_FORMAT
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
                emailInvalidCharacters = email.filter { android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()}.toList()
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

sealed class LoginScreenEvent : ViewModelEvent {
    data object TryToRequest : LoginScreenEvent()
    data object ProceedToRegister : LoginScreenEvent()
    data object ProceedToLoginWithBits : LoginScreenEvent()
    data class OnPasswordChange(val newPassword: String) : LoginScreenEvent()
    data class OnEmailChange(val newEmail: String) : LoginScreenEvent()
}

data class LoginScreenViewState(
    val email: String = "",
    val emailValidationError: EmailValidationError? = EmailValidationError.EMPTY_FIELD,
    val emailInvalidCharacters: List<Char> = emptyList(),
    val password: String = "",
    val passwordValidationErrors: PasswordValidationErrors? = PasswordValidationErrors.PASSWORD_EMPTY,
    val passwordInvalidCharacters: List<Char> = emptyList(),
) : ViewModelState {
    val canUserLogin = emailValidationError == null && passwordValidationErrors == null
}