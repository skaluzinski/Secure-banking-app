package com.example.securebankingapp.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    viewState: HomeScreenViewState,
    homeScreenUserId: Int,
    onEvent: (HomeScreenEvent) -> Unit
) {
    val onNavigateToUsersList: () -> Unit = remember {
        { onEvent(HomeScreenEvent.ToUsersList(homeScreenUserId)) }
    }
    val onNavigateToProfileScreen: () -> Unit = remember {
        { onEvent(HomeScreenEvent.ToUserProfile(homeScreenUserId))}
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            IconButton(onClick = onNavigateToProfileScreen) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    modifier = Modifier.align(Alignment.TopEnd),
                    contentDescription = ""
                )
            }
            Text(text = "logged in ", Modifier.align(Alignment.TopCenter))
            Column(modifier = Modifier.align(Alignment.Center)) {
                Button(onClick = onNavigateToUsersList) {
                    Text(text = "Send mooney to other users")
                }
            }
        }
    }
}