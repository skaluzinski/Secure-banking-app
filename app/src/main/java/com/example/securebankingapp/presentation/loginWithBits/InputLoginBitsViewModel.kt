package com.example.securebankingapp.presentation.loginWithBits

import androidx.lifecycle.viewModelScope
import com.example.securebankingapp.core.BaseViewModel
import com.example.securebankingapp.core.ViewModelEvent
import com.example.securebankingapp.core.ViewModelState
import com.example.securebankingapp.data.AccountRepository
import com.example.securebankingapp.navigation.Destinations
import com.example.securebankingapp.navigation.DestinationsRelay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputLoginBitsViewModel @Inject constructor(
    private val destinationsRelay: DestinationsRelay,
    private val accountRepository: AccountRepository
) : BaseViewModel<InputLoginBitsViewState,InputLoginBitsScreenEvent>(initialState = InputLoginBitsViewState()) {
    private var email = ""

    fun setAssociatedEmail(emailString: String) {
        email = emailString
    }

    override fun handleEvent(event: InputLoginBitsScreenEvent) {
        when (event) {
            is InputLoginBitsScreenEvent.LoadInitialBits -> {
                updateState {
                    it.copy(
                        visibleBits = event.bits.associateWith { "" }
                    )
                }
            }
            is InputLoginBitsScreenEvent.BitChanged -> updateState {
                val newBitMap = state.value.visibleBits.toMutableMap()
                newBitMap[event.index] = event.bitString
                it.copy(visibleBits = newBitMap)
            }

            InputLoginBitsScreenEvent.TryToLogin -> viewModelScope.launch {
                val loggedIn = accountRepository.loginWithBitsAndReturnSuccess(
                    email,
                    state.value.visibleBits.mapValues { it.value.first() }
                )

                if (loggedIn) {
//                    destinationsRelay.navigateTo(Destinations.Home)
                }
            }
        }
    }

}

sealed class InputLoginBitsScreenEvent : ViewModelEvent {
    data class BitChanged(val index: Int, val bitString: String): InputLoginBitsScreenEvent()
    data object TryToLogin: InputLoginBitsScreenEvent()
    data class LoadInitialBits(val bits: List<Int>): InputLoginBitsScreenEvent()
}

data class InputLoginBitsViewState(
    val visibleBits: Map<Int, String> = emptyMap()
) : ViewModelState {
    val canTryToLogin = visibleBits.values.none { it.isEmpty() }
}
