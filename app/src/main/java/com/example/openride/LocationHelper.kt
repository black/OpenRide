package com.example.openride

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val hasPermission get() = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        onSuccess: (lat: Double, lng: Double) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (!hasPermission) {
            onFailure(SecurityException("Location permission not granted"))
            return
        }

        // Try last known location first (fast, no battery drain)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    onSuccess(location.latitude, location.longitude)
                } else {
                    // Fall back to a fresh location request
                    fetchFreshLocation(onSuccess, onFailure)
                }
            }
            .addOnFailureListener(onFailure)
    }

    @SuppressLint("MissingPermission")
    private fun fetchFreshLocation(
        onSuccess: (Double, Double) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val cts = CancellationTokenSource()
        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
            .addOnSuccessListener { location ->
                if (location != null) onSuccess(location.latitude, location.longitude)
                else onFailure(Exception("Location unavailable"))
            }
            .addOnFailureListener(onFailure)
    }
}