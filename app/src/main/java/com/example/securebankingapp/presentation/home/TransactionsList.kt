package com.example.securebankingapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.securebankingapp.data.api.RevisedTransaction

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TransactionList(transactions: List<RevisedTransaction>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(transactions.size) { transactionIndex ->
            TransactionCard(transaction = transactions[transactionIndex])
        }
    }
}

@Composable
fun TransactionCard(transaction: RevisedTransaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Transaction ID: ${transaction.transactionId}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Date: ${transaction.transactionDate}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sender: ${transaction.senderEmail}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Recipient: ${transaction.recipientEmail ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Balance Before: ${transaction.balanceBefore}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Balance After: ${transaction.balanceAfter}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Transaction Amount: ${transaction.transactionAmount}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}