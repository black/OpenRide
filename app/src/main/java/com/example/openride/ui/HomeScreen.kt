package com.example.openride.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.openride.LocationParser
import com.example.openride.R
import com.example.openride.RideLocation
import com.example.openride.UrlResolver
import com.example.openride.applauncher.NammaYatriLauncher
import com.example.openride.applauncher.OlaLauncher
import com.example.openride.applauncher.RapidoLauncher
import com.example.openride.applauncher.UberLauncher
import com.example.openride.components.LaunchButton
import com.example.openride.components.MapLibreMap
import com.example.openride.ui.theme.Flurocent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun HomeScreen(
    sharedText: String,
    originLocation: RideLocation?,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var expandedUrl by remember {
        mutableStateOf("")
    }

    val destinationLocation = remember(expandedUrl) {
        LocationParser.parse(expandedUrl)
    }

    LaunchedEffect(sharedText) {
        if (sharedText.isNotBlank()) {
            expandedUrl = withContext(Dispatchers.IO) {
                UrlResolver.resolve(sharedText) ?: ""
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
       /* destinationLocation?.apply {
            MapLibreMap(
                modifier = Modifier.fillMaxSize(),
                originLocation!!,
                destinationLocation
            )
        }*/

        key(originLocation, destinationLocation) {
            MapLibreMap(
                modifier = Modifier.fillMaxSize(),
                origin = originLocation,
                destination = destinationLocation
            )
        }

        Column(
            modifier = modifier
                .align(Alignment.BottomCenter).padding(8.dp)
        ) {
            // Layer 2 (Foreground)
            Card(
                Modifier.padding(8.dp).fillMaxWidth().background(Flurocent)
            ) {
                Text(sharedText, style = MaterialTheme.typography.bodyLarge,color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                destinationLocation?.let {
                    Text("Lat : ${destinationLocation.longitude} , Long : ${destinationLocation.latitude}", style = MaterialTheme.typography.bodyLarge,color = Color.Black)
                }
            }
            LazyRow(
                modifier = Modifier
                    .padding(start=8.dp,end=8.dp,bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    LaunchButton(
                        "Uber",
                        R.drawable.logo_uber
                    ) {
                        UberLauncher.launch(context, destinationLocation!!)
                    }
                }
                item {
                    LaunchButton(
                        "Rapido",
                        R.drawable.logo_rapido
                    ) {
                        RapidoLauncher.launch(context, destinationLocation!!)
                    }
                }
                item {
                    LaunchButton(
                        "Ola",
                        R.drawable.logo_ola
                    ) {
                        OlaLauncher.launch(context, destinationLocation!!)
                    }
                }
                item {
                    LaunchButton(
                        "Namma Yatri",
                        R.drawable.logo_namma
                    ) {
                        NammaYatriLauncher.launch(context, destinationLocation!!)
                    }
                }
            }
        }
    }
}

