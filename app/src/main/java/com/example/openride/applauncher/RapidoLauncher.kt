package com.example.openride.applauncher

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.openride.RideLocation

object RapidoLauncher {
    fun launch(
        context: Context,
        location: RideLocation
    ) {

        val uri = Uri.parse(
            "uber://?action=setPickup" +
                    "&pickup=my_location" +
                    "&dropoff[latitude]=${location.latitude}" +
                    "&dropoff[longitude]=${location.longitude}"
        )

        val intent = Intent(
            Intent.ACTION_VIEW,
            uri
        )

        context.startActivity(intent)
    }
}