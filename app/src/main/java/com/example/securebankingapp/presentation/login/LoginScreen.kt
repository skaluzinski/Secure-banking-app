package com.example.securebankingapp.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    viewState: LoginScreenViewState,
    onLoginScreenEvent: (LoginScreenEvent) -> Unit
) {

    val onLoginClicked: () -> Unit = remember {
        { onLoginScreenEvent(LoginScreenEvent.TryToRequest)}
    }
    val onProceedToRegisterClicked: () -> Unit = remember {
        { onLoginScreenEvent(LoginScreenEvent.ProceedToRegister)}
    }
    val onAskForBitsToLogin: () -> Unit = remember {
        { onLoginScreenEvent(LoginScreenEvent.ProceedToLoginWithBits)}
    }


    val onPasswordChanged: (String) -> Unit = remember {
        { newPassword -> onLoginScreenEvent(LoginScreenEvent.OnPasswordChange(newPassword)) }
    }

    val onEmailChanged: (String) -> Unit = remember {
        { newEmail -> onLoginScreenEvent(LoginScreenEvent.OnEmailChange(newEmail)) }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Email", modifier = Modifier.width(70.dp))
                TextField(
                    value = viewState.email,
                    onValueChange = { onEmailChanged(it) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                )
            }

            Row {
                Column {
                    AnimatedVisibility(visible = viewState.emailValidationError?.errorMessageResId != null) {
                        if(viewState.emailValidationError?.errorMessageResId != null) {
                            Text(text = stringResource(id = viewState.emailValidationError.errorMessageResId))
                        }
                    }
                    AnimatedVisibility(visible = viewState.emailInvalidCharacters.isNotEmpty()) {
                        Text(text = "Invalid email characters: ${viewState.emailInvalidCharacters}")
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Password:", modifier = Modifier.width(70.dp))
                TextField(
                    value = viewState.password,
                    onValueChange = { onPasswordChanged(it) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                )
            }

            Row {
                Column {
                    AnimatedVisibility(visible = viewState.passwordValidationErrors?.errorMessageResId != null) {
                        if (viewState.passwordValidationErrors?.errorMessageResId != null) {
                            Text(text = stringResource(id = viewState.passwordValidationErrors.errorMessageResId))
                        }
                    }
                    AnimatedVisibility(visible = viewState.passwordInvalidCharacters.size != 0) {
                        Text(text = "Invalid password characters: ${viewState.passwordInvalidCharacters} ${viewState.passwordInvalidCharacters.size}")
                    }
                }
            }

            Button(
                enabled = viewState.canUserLogin,
                onClick = onLoginClicked
            ) {
                Text(text = "Login")
            }

            Button(
                onClick = onProceedToRegisterClicked
            ) {
                Text(text = "Proceed to register form")
            }


            Button(
                onClick = onAskForBitsToLogin
            ) {
                Text(text = "Ask for bits to login")
            }
        }
    }
}