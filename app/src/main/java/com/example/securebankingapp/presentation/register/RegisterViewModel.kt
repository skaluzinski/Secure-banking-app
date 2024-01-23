package com.example.securebankingapp.presentation.register

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.domain.EmailValidationError
import com.example.securebankingapp.domain.PasswordValidationErrors
import com.example.securebankingapp.domain.NameValidationErrors
import com.example.securebankingapp.navigation.Destinations
import com.example.securebankingapp.navigation.DestinationsRelay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val destinationsRelay: DestinationsRelay
) : BaseViewModel<RegisterViewState, RegisterScreenEvent>(initialState = RegisterViewState()) {

    private fun isValidEmailFormat(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private val validCharacters =
        setOf("A".."Z", "a".."z", '@','0'..'9', '#', '?', '!', '@', '$', '%', '^', '&', '*', '.', )

    val validPasswordCharacters = listOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '!', '@', '#', '$', '%', '^', '&', '*'
    )

    val specialLetters = listOf('!', '@', '#', '$', '%', '^', '&', '*')

    private fun validatePassword(password: String) =          when {
        password.isEmpty() -> PasswordValidationErrors.PASSWORD_EMPTY
        containsSqlInjection(password) -> PasswordValidationErrors.SQL_INJECTION
        password.any { !validPasswordCharacters.contains(it) } -> PasswordValidationErrors.INVALID_CHARACTERS
        password.length >= 32 -> PasswordValidationErrors.PASSWORD_TOO_LONG
        password.length < 8 -> PasswordValidationErrors.PASSWORD_TOO_SHORT
        password.none { it.isUpperCase() } -> PasswordValidationErrors.PASSWORD_NO_BIG_LETTER
        password.none { it.isLowerCase() } -> PasswordValidationErrors.PASSWORD_NO_SMALL_LETTER
        password.none { it.isLowerCase() } -> PasswordValidationErrors.PASSWORD_NO_SMALL_LETTER
        password.none { it.isDigit() } -> PasswordValidationErrors.PASSWORD_NO_Digit
        password.none { it in specialLetters} -> PasswordValidationErrors.PASSWORD_NO_SPECIAL_LETTER
        else -> null
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

    private fun containsSqlInjection(text: String): Boolean {
        return text.contains(";") || text.contains("--") || text.contains("DROP", ignoreCase = true)
    }

    private fun containsComma(text: String): Boolean {
        return text.contains(",")
    }

    private fun filterInvalidEmailCharacters(email: String): List<Char> {
        return email.filter { android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()}.toList()
    }

    private fun filterInvalidPasswordCharacters(password: String): List<Char> {
        return password.filter { !validPasswordCharacters.contains(it) }.toList()
    }

    fun validateName(surname: String): NameValidationErrors? = when {
        surname.isEmpty() -> NameValidationErrors.SURNAME_EMPTY
        surname.length < 2 -> NameValidationErrors.SURNAME_TOO_SHORT
        Regex("[^a-zA-Z]").containsMatchIn(surname) -> NameValidationErrors.SURNAME_CONTAINS_SPECIAL_CHARACTERS
        surname.any { it.isDigit() } -> NameValidationErrors.SURNAME_CONTAINS_DIGITS
        else -> null
    }


    override fun handleEvent(event: RegisterScreenEvent) {
        when(event) {
            is RegisterScreenEvent.EmailChanged -> {
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
            is RegisterScreenEvent.PasswordChanged -> {
                val passwordValidationErrors = validatePassword(event.newPassword)
                val passwordInvalidCharacters = filterInvalidPasswordCharacters(event.newPassword)
                updateState {
                    it.copy(
                        passwordValidationErrors = passwordValidationErrors,
                        passwordInvalidCharacters = passwordInvalidCharacters,
                        password = event.newPassword
                    )
                }
                if (state.value.passwordValidationErrors == null) {
                    updateState {
                        it.copy(
                            repeatPasswordValidationErrors = if (state.value.repeatPassword != state.value.password) PasswordValidationErrors.PASSWORD_DONT_MATCH else null
                        )
                    }
                }

            }
            is RegisterScreenEvent.RepeatEmailChanged -> {
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
            is RegisterScreenEvent.RepeatPasswordChanged -> {
                val repeatPasswordValidationErrors = validatePassword(event.newPassword)
                val repeatPasswordInvalidCharacters = filterInvalidPasswordCharacters(event.newPassword)
                updateState {
                    it.copy(
                        repeatPasswordInvalidCharacters = repeatPasswordInvalidCharacters,
                        repeatPasswordValidationErrors = repeatPasswordValidationErrors,
                        repeatPassword = event.newPassword
                    )
                }
                if (state.value.repeatPasswordValidationErrors == null) {
                    updateState {
                        it.copy(
                            repeatPasswordValidationErrors = if (state.value.repeatPassword != state.value.password) PasswordValidationErrors.PASSWORD_DONT_MATCH else null
                        )
                    }
                }
            }
            RegisterScreenEvent.TryToRegiste -> {
                viewModelScope.launch {
                    val registerSuccess = accountRepository.registerUser(
                        email = state.value.email,
                        password = state.value.password,
                        name = state.value.name
                    )

                    if (registerSuccess) {
                        destinationsRelay.navigateTo(Destinations.Login)
                    }
                }
            }
            is RegisterScreenEvent.NameChanged -> {
                val nameValidationErrors = validateName(event.newName)
                updateState {
                    it.copy(
                        name = event.newName,
                        nameValidationErrors = nameValidationErrors
                    )
                }
            }
            is RegisterScreenEvent.SurnameChanged ->{
                val surnameValidationErrors = validateName(event.newSurname)
                updateState {
                    it.copy(
                        surname = event.newSurname,
                        surnameValidationErrors = surnameValidationErrors
                    )
                }
            }
        }
    }
}

sealed class RegisterScreenEvent: ViewModelEvent {
    data object TryToRegiste : RegisterScreenEvent()
    data class RepeatEmailChanged(val newEmail: String) : RegisterScreenEvent()
    data class EmailChanged(val newEmail: String) : RegisterScreenEvent()
    data class RepeatPasswordChanged(val newPassword: String) : RegisterScreenEvent()
    data class PasswordChanged(val newPassword: String) : RegisterScreenEvent()
    data class SurnameChanged(val newSurname: String) : RegisterScreenEvent()
    data class NameChanged(val newName: String) : RegisterScreenEvent()
}

data class RegisterViewState(
    val name: String = "",
    val surname: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val email: String = "",
    val repeatEmail: String = "",
    val emailValidationError: EmailValidationError? = EmailValidationError.EMPTY_FIELD,
    val emailInvalidCharacters: List<Char> = emptyList(),
    val passwordValidationErrors: PasswordValidationErrors? = PasswordValidationErrors.PASSWORD_EMPTY,
    val passwordInvalidCharacters: List<Char> = emptyList(),
    val repeatEmailValidationError: EmailValidationError? = EmailValidationError.EMPTY_FIELD,
    val repeatEmailInvalidCharacters: List<Char> = emptyList(),
    val repeatPasswordValidationErrors: PasswordValidationErrors? = PasswordValidationErrors.PASSWORD_EMPTY,
    val repeatPasswordInvalidCharacters: List<Char> = emptyList(),
    val surnameValidationErrors: NameValidationErrors? = null,
    val nameValidationErrors: NameValidationErrors?= null
) : ViewModelState {
    val canRegister = repeatEmailValidationError == null
            && emailValidationError == null
            && surnameValidationErrors == null
            && nameValidationErrors  == null
            && passwordValidationErrors == null
            && repeatPasswordValidationErrors == null
}