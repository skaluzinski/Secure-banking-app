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
class DestinationsRelay @Inject constructor() {
    private val _navigationEvents = MutableSharedFlow<Destinations>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val navigationEvents: SharedFlow<Destinations> = _navigationEvents.asSharedFlow()

    fun navigateTo(destination: Destinations) {
        //bad, to change
        MainScope().launch {
            _navigationEvents.emit(destination)
        }
    }
}