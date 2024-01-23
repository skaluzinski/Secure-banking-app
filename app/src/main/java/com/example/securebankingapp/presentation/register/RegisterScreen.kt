package com.example.securebankingapp.presentation.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.securebankingapp.presentation.common.GenericInput

@Composable
fun RegisterScreen(
    viewState: RegisterViewState,
    onEvent: (RegisterScreenEvent) -> Unit
) {
    val onSurnameChanged: (String) -> Unit = remember {
        { onEvent(RegisterScreenEvent.SurnameChanged(it)) }
    }
    val onNameChanged: (String) -> Unit = remember {
        { onEvent(RegisterScreenEvent.NameChanged(it)) }
    }
    val onEmailChanged: (String) -> Unit = remember {
        { onEvent(RegisterScreenEvent.EmailChanged(it)) }
    }
    val onRepeatEmailChanged: (String) -> Unit = remember {
        { onEvent(RegisterScreenEvent.RepeatEmailChanged(it)) }
    }
    val onPasswordChanged: (String) -> Unit = remember {
        { onEvent(RegisterScreenEvent.PasswordChanged(it)) }
    }
    val onRepeatPasswordChanged: (String) -> Unit = remember {
        { onEvent(RegisterScreenEvent.RepeatPasswordChanged(it)) }
    }

    val onRegisterClicked:() -> Unit = remember {
        {onEvent(RegisterScreenEvent.TryToRegiste)}
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(vertical = paddingValues.calculateLeftPadding(LayoutDirection.Ltr)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GenericInput(
                title = "Name",
                value = viewState.name,
                onInputChanged = onNameChanged,
                validationErrorResId = viewState.nameValidationErrors?.errorMessageResId
            )
            GenericInput(
                title = "Password",
                value = viewState.password,
                onInputChanged = onPasswordChanged,
                validationErrorResId = viewState.passwordValidationErrors?.errorMessageResId
            )
            GenericInput(
                title = "Password repeat",
                value = viewState.repeatPassword,
                onInputChanged = onRepeatPasswordChanged,
                validationErrorResId = viewState.repeatPasswordValidationErrors?.errorMessageResId
            )
            GenericInput(
                title = "Email",
                value = viewState.email,
                onInputChanged = onEmailChanged,
                validationErrorResId = viewState.emailValidationError?.errorMessageResId
            )
            GenericInput(
                title = "Email repeat",
                value = viewState.repeatEmail,
                onInputChanged = onRepeatEmailChanged,
                validationErrorResId = viewState.repeatEmailValidationError?.errorMessageResId
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().imePadding().padding(end = 30.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AnimatedVisibility(visible = viewState.canRegister) {
            Button(onClick = onRegisterClicked) {
                Text(text = "Register")
            }
        }
    }

}
