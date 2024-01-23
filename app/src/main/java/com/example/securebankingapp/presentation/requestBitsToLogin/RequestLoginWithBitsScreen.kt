package com.example.securebankingapp.presentation.requestBitsToLogin

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
import androidx.compose.ui.unit.dp


@Composable
fun RequestLoginWithBitsScreen(
    viewState: RequestLoginWithBitViewState,
    onEvent: (RequestLoginWithBitsScreenEvent) -> Unit
) {

    val onRequestBitsClicked: () -> Unit = remember {
        { onEvent(RequestLoginWithBitsScreenEvent.AskForBits) }
    }


    val onEmailChanged: (String) -> Unit = remember {
        { newEmail -> onEvent(RequestLoginWithBitsScreenEvent.EmailChanged(newEmail)) }
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
                        if (viewState.emailValidationError?.errorMessageResId != null) {
                            Text(text = stringResource(id = viewState.emailValidationError.errorMessageResId))
                        }
                    }
                    AnimatedVisibility(visible = viewState.emailInvalidCharacters.isNotEmpty()) {
                        Text(text = "Invalid email characters: ${viewState.emailInvalidCharacters}")
                    }
                }
            }


            Button(
                enabled = viewState.canAskForBits,
                onClick = onRequestBitsClicked
            ) {
                Text(text = "Request bits")
            }

        }
    }
}