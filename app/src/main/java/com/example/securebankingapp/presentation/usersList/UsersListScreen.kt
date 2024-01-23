package com.example.securebankingapp.presentation.usersList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.securebankingapp.data.api.PrivateUserModel
import com.example.securebankingapp.presentation.home.HomeScreenViewState

@Composable
fun UsersListScreen(
    viewState: UsersListViewState,
    senderInfo: PrivateUserModel,
    onEvent: (UsersListEvent) -> Unit
) {
    val onUserClicked:(Int, String) -> Unit = remember {
        { id, name ->
            onEvent(
                UsersListEvent.OnUserClicked(
                    recipentId = id,
                    recipentName = name,
                    senderInfo = senderInfo
                )
            )}
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn {
                if (viewState.users.isEmpty()) {
                    item {
                        Text(text = "No users found")
                    }
                }
                items(viewState.users) { item ->
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable { onUserClicked(item.id, item.name) },
                        verticalArrangement = Arrangement.SpaceBetween
                        ) {
                        Text(text = "User: ${item.name}")
                        Text(text = "Email: ${item.email}")
                    }
                }
            }
        }
    }
}