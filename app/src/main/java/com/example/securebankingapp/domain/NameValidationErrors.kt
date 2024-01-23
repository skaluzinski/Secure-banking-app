package com.example.securebankingapp.domain

import com.example.securebankingapp.R

enum class NameValidationErrors(val errorMessageResId: Int) {
    SURNAME_EMPTY(R.string.surname_error_empty),
    SURNAME_TOO_SHORT(R.string.surname_error_too_short),
    SURNAME_CONTAINS_SPECIAL_CHARACTERS(R.string.surname_error_contains_special_characters),
    SURNAME_CONTAINS_DIGITS(R.string.surname_error_contains_digits),
}

