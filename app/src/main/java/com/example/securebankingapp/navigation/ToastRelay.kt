package com.example.securebankingapp.navigation

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToastRelay @Inject constructor(
    private val destinationsRelay: DestinationsRelay
) {
    private val _toastEvents = MutableSharedFlow<ToastModel>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val toastEvents: SharedFlow<ToastModel> = _toastEvents.asSharedFlow()

    fun showToast(toastModel: ToastModel) {
        //bad, to change
        MainScope().launch {
            if (toastModel.message.contains("OPERATION_NEEDS_RECENT_LOGIN")) {
                destinationsRelay.navigateTo(Destinations.Login)
            }
            _toastEvents.emit(toastModel)
        }
    }
}

data class ToastModel(
    val message: String,
    val isError: Boolean = true
)