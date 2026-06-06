package com.example.openride.rides

import android.content.Context
import android.util.Log

object RideAppDetector {

    private const val TAG = "RideAppDetector"

    private val supportedApps = listOf(

        RideApp(
            "Uber",
            "com.ubercab"
        ),

        RideApp(
            "Ola",
            "com.olacabs.customer"
        ),

        RideApp(
            "Rapido",
            "com.rapido.passenger"
        ),

        RideApp(
            "Namma Yatri",
            "in.juspay.nammayatri"
        )
    )

    fun installedApps(
        context: Context
    ): List<RideApp> {
        val pm = context.packageManager
        val foundApps = supportedApps.filter { app ->
            try {
                pm.getPackageInfo(
                    app.packageName,
                    0
                )
                Log.d(
                    TAG,
                    "FOUND: ${app.packageName}"
                )
                true
            } catch (e: Exception) {
                Log.d(
                    TAG,
                    "NOT FOUND: ${app.packageName}"
                )
                false
            }
        }
        Log.d(
            TAG,
            "Total apps found: ${foundApps.size}"
        )
        return foundApps
    }
}