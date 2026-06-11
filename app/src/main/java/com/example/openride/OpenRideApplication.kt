package com.example.openride

import android.app.Application
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class OpenRideApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(
            this,
            null,
            WellKnownTileServer.MapLibre
        )
    }
}