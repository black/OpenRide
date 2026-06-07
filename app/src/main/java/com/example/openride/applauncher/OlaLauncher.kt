package com.example.openride.applauncher

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.openride.RideLocation

object OlaLauncher {
    fun launch(context: Context, location: RideLocation) {
        val uri = Uri.parse(
            "olacabs://app/launch?" +
                    "lat=${location.latitude}&lng=${location.longitude}" +   // Ola uses drop as pickup fallback
                    "&drop_lat=${location.latitude}&drop_lng=${location.longitude}" +
                    "&category=auto"
        )

        val intent = Intent(Intent.ACTION_VIEW, uri)

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // Universal fallback — opens app or Ola website
            context.startActivity(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://olawebcdn.com/assets/ola-universal-link.html?" +
                                "drop_lat=${location.latitude}&drop_lng=${location.longitude}" +
                                "&category=auto&landing_page=bk"
                    ))
            )
        }
    }
}