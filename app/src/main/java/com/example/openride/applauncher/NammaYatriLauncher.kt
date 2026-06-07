package com.example.openride.applauncher

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.openride.RideLocation

object NammaYatriLauncher {
    fun launch(context: Context, location: RideLocation) {
        val geoUri = Uri.parse(
            "geo:${location.latitude},${location.longitude}" +
                    "?q=${location.latitude},${location.longitude}(${Uri.encode( "Destination")})"
        )

        val intent = Intent(Intent.ACTION_VIEW, geoUri).apply {
            setPackage("in.juspay.nammayatri")
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            context.startActivity(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=in.juspay.nammayatri"))
            )
        }
    }
}