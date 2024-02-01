package com.example.securebankingapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import com.example.securebankingapp.R
import com.example.securebankingapp.navigation.Destinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewState: HomeScreenViewState,
    onEvent: (HomeScreenEvent) -> Unit
) {
    val onNavigateToProfileScreen: () -> Unit = remember {
        { onEvent(HomeScreenEvent.ToUserProfile)}
    }
    val onLogoutClicked: () -> Unit = remember {
        { onEvent(HomeScreenEvent.Logout) }
    }
    val onNavigateToWithdrawScreen: () -> Unit = remember {
        { onEvent(HomeScreenEvent.ToWithdraw) }
    }
    val onNavigateToDepositScreen: () -> Unit = remember {
        { onEvent(HomeScreenEvent.ToDeposit) }
    }
    val onNavigateToSendMoneyScreen: () -> Unit = remember {
        { onEvent(HomeScreenEvent.ToSendMoney) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        text = "Welcome ${viewState.name}",
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = "",
                        modifier = Modifier.clickable { onLogoutClicked() }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                actions = { Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "",
                    modifier = Modifier.clickable { onNavigateToProfileScreen() }
                ) })
        },
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar({
                BottomBar(
                    onNavigateToDepositScreen = onNavigateToDepositScreen,
                    onNavigateToWithdrawScreen = onNavigateToWithdrawScreen,
                    onNavigateToSendMoneyScreen = onNavigateToSendMoneyScreen
                )
            })
        }
    ) { paddingValues ->
        TransactionList(transactions = viewState.transactions, modifier = Modifier.padding(paddingValues))
    }
}

@Composable
private fun BottomBar(
    onNavigateToDepositScreen: () -> Unit,
    onNavigateToWithdrawScreen: () -> Unit,
    onNavigateToSendMoneyScreen: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        IconButton(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f),
            onClick = { /*TODO*/ }) {
            Icon(
                painterResource(id = R.drawable.ic_deposit),
                modifier = Modifier.clickable { onNavigateToDepositScreen() },
                contentDescription = ""
            )
        }
        IconButton(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f),
            onClick = { /*TODO*/ }) {
            Icon(
                painterResource(id = R.drawable.ic_withdraw),
                contentDescription = "",
                modifier = Modifier.clickable { onNavigateToWithdrawScreen() },
            )
        }
        IconButton(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f),
            onClick = { /*TODO*/ }) {
            Icon(
                painterResource(id = R.drawable.ic_send_money), 
                contentDescription = "",
                modifier = Modifier.clickable { onNavigateToSendMoneyScreen() },
            )
        }
    }

}