package com.example.openride.applauncher

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.openride.RideLocation

object NammaYatriLauncher {
    fun launch(context: Context, location: RideLocation) {
        // Namma Yatri doesn't handle geo: intents
        // Launch the app directly via its launcher activity
        val intent = context.packageManager
            .getLaunchIntentForPackage("in.juspay.nammayatri")

        if (intent != null) {
            context.startActivity(intent)
        } else {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=in.juspay.nammayatri")
                )
            )
        }
    }
}