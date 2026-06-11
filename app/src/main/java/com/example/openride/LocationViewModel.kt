package com.example.openride

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationViewModel : ViewModel() {

    private val _location = MutableStateFlow<RideLocation?>(null)
    val location: StateFlow<RideLocation?> = _location.asStateFlow()

    fun setLocation(latitude: Double, longitude: Double) {
        _location.value = RideLocation(latitude, longitude)
    }
}