package com.example.securebankingapp.presentation.sendMoney

import android.icu.text.DecimalFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.example.securebankingapp.presentation.common.GenericInput
import kotlin.math.max

@Composable
fun SendMoneyScreen(
    viewState: SendMoneyViewState,
    onEvent: (SendMoneyEvent) -> Unit
) {
    var amountToSend by remember { mutableStateOf("") }

    val onSendClicked: () -> Unit =
        remember {
            {
                onEvent(
                    SendMoneyEvent.SendMoneyClicked(
                        amount = amountToSend.toFloat().div(100f)
                    )
                )
            }
        }
    val onEmailChanged: (String) -> Unit = remember {
        { onEvent(SendMoneyEvent.EmailChanged(it)) }
    }
    val onTitleChanged: (String) -> Unit = remember {
        { onEvent(SendMoneyEvent.OnTitleChanged(it)) }
    }
    val onRepeatEmailChanged: (String) -> Unit = remember {
        { onEvent(SendMoneyEvent.RepeatEmailChanged(it)) }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Current balance: ${viewState.senderBalance}")
            TextField(
                value = amountToSend,
                enabled = !viewState.isTransactionInProgress,
                onValueChange = {
                    val textToCheck = if (it.startsWith("0") ) {
                        ""
                    } else {
                        it
                    }
                    val textWithRedundantDots = textToCheck.filter { it.isDigit() }
                    amountToSend = replaceDotsExceptFirst(textWithRedundantDots)
                },
                visualTransformation = CurrencyAmountInputVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                )
            )
            GenericInput(
                title = "Title",
                value = viewState.transactionTitle,
                isEnabled = !viewState.isTransactionInProgress,
                onInputChanged = onTitleChanged,
                validationErrorResId = viewState.titleValidationErrors?.errorMessageResId
            )

            GenericInput(
                title = "Email",
                value = viewState.email,
                isEnabled = !viewState.isTransactionInProgress,
                onInputChanged = onEmailChanged,
                validationErrorResId = viewState.emailValidationError?.errorMessageResId
            )
            GenericInput(
                title = "Email repeat",
                value = viewState.repeatEmail,
                isEnabled = !viewState.isTransactionInProgress,
                onInputChanged = onRepeatEmailChanged,
                validationErrorResId = viewState.repeatEmailValidationError?.errorMessageResId
            )

            Button(onClick = onSendClicked, enabled = viewState.canSend && amountToSend.isNotEmpty() && !viewState.isTransactionInProgress) {
                Text(text = "Send")
            }
        }
    }
    AnimatedVisibility(visible = viewState.isTransactionInProgress) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

class CurrencyAmountInputVisualTransformation(
    private val fixedCursorAtTheEnd: Boolean = true,
    private val numberOfDecimals: Int = 2
) : VisualTransformation {

    private val symbols = DecimalFormat().decimalFormatSymbols

    override fun filter(text: AnnotatedString): TransformedText {
        val thousandsSeparator = symbols.groupingSeparator
        val decimalSeparator = symbols.decimalSeparator
        val zero = symbols.zeroDigit

        val inputText = text.text

        val intPart = inputText
            .dropLast(numberOfDecimals)
            .reversed()
            .chunked(3)
            .joinToString(thousandsSeparator.toString())
            .reversed()
            .ifEmpty {
                zero.toString()
            }

        val fractionPart = inputText.takeLast(numberOfDecimals).let {
            if (it.length != numberOfDecimals) {
                List(numberOfDecimals - it.length) {
                    zero
                }.joinToString("") + it
            } else {
                it
            }
        }

        val formattedNumber = intPart + decimalSeparator + fractionPart

        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        val offsetMapping = if (fixedCursorAtTheEnd) {
            FixedCursorOffsetMapping(
                contentLength = inputText.length,
                formattedContentLength = formattedNumber.length
            )
        } else {
            MovableCursorOffsetMapping(
                unmaskedText = text.toString(),
                maskedText = newText.toString(),
                decimalDigits = numberOfDecimals
            )
        }

        return TransformedText(newText, offsetMapping)
    }

    private class FixedCursorOffsetMapping(
        private val contentLength: Int,
        private val formattedContentLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = formattedContentLength
        override fun transformedToOriginal(offset: Int): Int = contentLength
    }

    private class MovableCursorOffsetMapping(
        private val unmaskedText: String,
        private val maskedText: String,
        private val decimalDigits: Int
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            when {
                unmaskedText.length <= decimalDigits -> {
                    maskedText.length - (unmaskedText.length - offset)
                }
                else -> {
                    offset + offsetMaskCount(offset, maskedText)
                }
            }

        override fun transformedToOriginal(offset: Int): Int =
            when {
                unmaskedText.length <= decimalDigits -> {
                    max(unmaskedText.length - (maskedText.length - offset), 0)
                }
                else -> {
                    offset - maskedText.take(offset).count { !it.isDigit() }
                }
            }

        private fun offsetMaskCount(offset: Int, maskedText: String): Int {
            var maskOffsetCount = 0
            var dataCount = 0
            for (maskChar in maskedText) {
                if (!maskChar.isDigit()) {
                    maskOffsetCount++
                } else if (++dataCount > offset) {
                    break
                }
            }
            return maskOffsetCount
        }
    }
}

fun replaceDotsExceptFirst(input: String): String {
    return input.replace(Regex("\\.(?!$)"), "")
}
