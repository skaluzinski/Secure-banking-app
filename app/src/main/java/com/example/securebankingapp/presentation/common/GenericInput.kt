package com.example.securebankingapp.presentation.common

import android.content.res.Resources.Theme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplicationtocopy.ui.theme.SecureBankingAppTheme

@Composable
fun GenericInput(
    title: String,
    value: String,
    onInputChanged: (String) -> Unit,
    validationErrorResId: Int?,
    invalidCharacters: List<Char> = emptyList()
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "$title:", modifier = Modifier.width(70.dp))
            GenericTextField(
                value = value,
                onValueChange = {
                    onInputChanged(
                        it.replace("\\s".toRegex(), "")
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                ),
            )
        }

        ValidationErrors(validationErrorResId, invalidCharacters)
    }
}

@Composable
fun GenericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (passwordVisible) VisualTransformation.None else VisualTransformation.None,
    )
}

@Composable
fun ValidationErrors(
    validationErrorResId: Int?,
    invalidCharacters: List<Char>
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        AnimatedVisibility(
            visible = validationErrorResId != null,
            enter = fadeIn(animationSpec = tween(300))
        ) {
            validationErrorResId?.let {
                Text(text = stringResource(id = it), color = MaterialTheme.colorScheme.error)
            }
        }
        AnimatedVisibility(
            visible = invalidCharacters.isNotEmpty(),
            enter = fadeIn(animationSpec = tween(300))
        ) {
            Text(
                text = "Invalid characters: ${invalidCharacters.joinToString()}",
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}