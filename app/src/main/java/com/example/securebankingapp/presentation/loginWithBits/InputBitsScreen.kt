package com.example.securebankingapp.presentation.loginWithBits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun InputLoginBitsScreen(
    viewState: InputLoginBitsViewState,
    onEvent: (InputLoginBitsScreenEvent) -> Unit
) {
    val onLoginClicked: () -> Unit = remember {
        { onEvent(InputLoginBitsScreenEvent.TryToLogin)}
    }


    val onCharChange: (Int, String) -> Unit = remember {
        { bitIndex, bitString ->
            onEvent(
                InputLoginBitsScreenEvent.BitChanged(
                    bitIndex,
                    bitString
                )
            )
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            viewState.visibleBits.forEach { (index, value) ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Char at: ${index + 1}", modifier = Modifier.wrapContentSize())
                    TextField(
                        value = value,
                        onValueChange = { onCharChange(index, if(it.length >1) it.take(1) else it) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.width(50.dp)
                    )
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Button(onClick = onLoginClicked, enabled = viewState.canTryToLogin) {
                Text(text = "Login")
            }
        }
    }
}

@Composable
fun ParametrizedSingleCharTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        BasicTextField(
            value = value.take(1),
            onValueChange = {
                if (it.length <= 1) {
                    onValueChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
                .clip(MaterialTheme.shapes.medium),
        )
    }
}