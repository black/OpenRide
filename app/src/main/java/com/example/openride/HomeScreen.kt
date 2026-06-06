package com.example.openride

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.openride.applauncher.NammaYatriLauncher
import com.example.openride.applauncher.OlaLauncher
import com.example.openride.applauncher.RapidoLauncher
import com.example.openride.applauncher.UberLauncher
import com.example.openride.rides.RideAppDetector
import com.example.openride.rides.RideLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    sharedText: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val apps = remember {
        RideAppDetector.installedApps(context)
    }

    var expandedUrl by remember {
        mutableStateOf("")
    }

    //    /* Parsing Location */
    val location = remember(expandedUrl) {
        LocationParser.parse(expandedUrl)
    }

    LaunchedEffect(sharedText) {
        if (sharedText.isNotBlank()) {
            expandedUrl = withContext(
                Dispatchers.IO
            ) {
                UrlResolver.resolve(sharedText) ?: ""
            }
            Log.d(
                "OpenRide",
                expandedUrl
            )
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text("Original URL")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(sharedText)

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text("Expanded URL")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(expandedUrl)

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text("Ride Apps")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text("Location")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text("${location}")

        LazyColumn {

            items(apps) { app ->

                Button(
                    onClick = {

                        if (location == null) {

                            RideLauncher.launchApp(
                                context,
                                app.packageName
                            )

                            return@Button
                        }

                        when (app.packageName) {

                            "com.ubercab" -> {
                                UberLauncher.launch(
                                    context,
                                    location
                                )
                            }

                            "com.rapido.passenger" -> {
                                RapidoLauncher.launch(
                                    context,
                                    location
                                )
                            }

                            "com.olacabs.customer" -> {
                                OlaLauncher.launch(
                                    context,
                                    location
                                )
                            }

                            "in.juspay.nammayatri" -> {
                                NammaYatriLauncher.launch(
                                    context,
                                    location
                                )
                            }

                            else -> {
                                RideLauncher.launchApp(
                                    context,
                                    app.packageName
                                )
                            }
                        }
                    }
                ) {
                    Text(app.name)
                }
            }
        }
    }
}


//@Composable
//fun HomeScreen(
//    sharedText: String,
//    modifier: Modifier = Modifier
//) {
//
//    val context = LocalContext.current
//    var clipboardText: String? by remember {
//        mutableStateOf("")
//    }
//
//    /* Getting the clipboard text */
//    val parser = ClipboardParser()
//    LaunchedEffect(Unit) {
//        clipboardText = parser.getClipboardText(context)
//    }
//
//    /* Parsing Location */
//    val location = clipboardText?.let {
//        LocationParser.parse(it)
//    }
//
//    /* show apps Location */
//    val appsPkg = remember {
//        RideAppDetector.installedApps(context)
//    }
//
//    Column(
//        modifier = Modifier.padding(16.dp)
//    ) {
//        Text("Copied Location")
//        Spacer(Modifier.height(8.dp))
//        Text(text = clipboardText ?: "")
//        Spacer(Modifier.height(8.dp))
//        Text(text = "${location}")
//
//        LazyColumn {
//            items(appsPkg) { app ->
//                Button(
//                    onClick = {
//                        RideLauncher.launchApp(
//                            context,
//                            app.packageName
//                        )
//                    }
//                ) {
//                    Text(app.name)
//                }
//            }
//        }
//
//        Column(
//            modifier = Modifier.padding(32.dp)
//        ) {
//            Text("Received Text")
//            Text(sharedText)
//        }
//    }
//}