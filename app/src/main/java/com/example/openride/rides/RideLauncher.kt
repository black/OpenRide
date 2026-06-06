package com.example.openride.rides

import android.content.Context
import android.widget.Toast

object RideLauncher {

    fun launchApp(
        context: Context,
        packageName: String
    ) {

        val intent =
            context.packageManager
                .getLaunchIntentForPackage(packageName)

        if (intent != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(
                context,
                "App not installed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}