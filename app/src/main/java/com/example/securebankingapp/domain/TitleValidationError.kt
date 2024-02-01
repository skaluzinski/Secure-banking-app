package com.example.securebankingapp.domain

import androidx.annotation.StringRes
import com.example.securebankingapp.R

enum class TitleValidationError(@StringRes val errorMessageResId: Int) {
    EMPTY_FIELD(R.string.title_error_empty),
    INVALID_FORMAT(R.string.title_error_invalid_format),
    TOO_LONG(R.string.title_error_too_long),
    SQL_INJECTION(R.string.email_error_sql_injection),
    CONTAINS_COMMA(R.string.title_error_contains_comma),
}