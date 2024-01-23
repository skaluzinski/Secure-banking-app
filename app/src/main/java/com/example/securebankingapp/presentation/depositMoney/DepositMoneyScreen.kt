package com.example.securebankingapp.presentation.depositMoney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DepositMoneyScreen(
    viewState: DepositMoneyViewState,
    onEvent: (DepositMoneyEvent) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Sender name: ${viewState.senderInfo.name}")
            Text(text = "Sender email: ${viewState.senderInfo.email}")
            Text(text = "Sender balance: ${viewState.senderInfo.balance}")
            Text(text = "Sender account number: ${viewState.senderInfo.accountNumber}")

            Text(text = "Recipient name: ${viewState.recipentName}")
            Text(text = "Recipient id: ${viewState.recipentId}")

            TextField(value = "not implemented", onValueChange = {   })
            Button(onClick = { /*TODO*/ }, enabled = false) {
                Text(text = "SendMoney")
            }
        }
    }
}