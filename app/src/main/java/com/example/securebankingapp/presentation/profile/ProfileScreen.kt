package com.example.securebankingapp.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    viewState: ProfileViewState,
    onEvent: (ProfileEvent) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "User name: ${viewState.user.name}")
            Text(text = "User email: ${viewState.user.email}")
            Text(text = "User balance: ${viewState.user.balance}")
            Text(text = "User account number: ${viewState.user.cardNumber}")
        }
    }
}