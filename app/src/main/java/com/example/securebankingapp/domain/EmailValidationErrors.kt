package com.example.securebankingapp.domain

import androidx.annotation.StringRes
import com.example.securebankingapp.R

enum class EmailValidationError(@StringRes val errorMessageResId: Int) {
    EMPTY_FIELD(R.string.email_error_empty),
    INVALID_FORMAT(R.string.email_error_invalid_format),
    TOO_SHORT(R.string.email_error_too_short),
    TOO_LONG(R.string.email_error_too_long),
    SQL_INJECTION(R.string.email_error_sql_injection),
    CONTAINS_COMMA(R.string.email_error_contains_comma),
    EMAILS_DONT_MATCH(R.string.email_error_dont_match)
}