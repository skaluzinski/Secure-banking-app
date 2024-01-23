package com.example.securebankingapp.domain

import androidx.annotation.StringRes
import com.example.securebankingapp.R

enum class PasswordValidationErrors(@StringRes val errorMessageResId: Int) {
    PASSWORD_EMPTY(R.string.password_error_empty),
    PASSWORD_NO_BIG_LETTER(R.string.password_error_no_big_letter),
    PASSWORD_NO_SMALL_LETTER(R.string.password_error_no_small_letter),
    PASSWORD_NO_SPECIAL_LETTER(R.string.password_error_no_special_sign),
    PASSWORD_NO_Digit(R.string.password_error_no_digit),
    PASSWORD_TOO_LONG(R.string.password_error_too_long),
    PASSWORD_TOO_SHORT(R.string.password_error_too_short),
    INVALID_CHARACTERS(R.string.password_error_invalid_characters),
    SQL_INJECTION(R.string.email_error_sql_injection),
    PASSWORD_DONT_MATCH(R.string.password_error_dont_match),

}

