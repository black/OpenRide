package com.example.openride.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun HomeScreen(
    sharedText: String,
    originLocation : RideLocation?,
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

    destinationLocation?.let {
        MapLibreMap(
            modifier= Modifier.fillMaxWidth().height(200.dp),
            originLocation!!,
            destinationLocation
        )
    }
    
    Column(
        modifier = modifier.padding(8.dp),
    ) {
        Text("Copied Location", style = MaterialTheme.typography.bodyLarge)
        Text(sharedText, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Location", style = MaterialTheme.typography.bodyLarge)
        destinationLocation?.let {
            Text("Lat : ${destinationLocation.longitude} , Long : ${destinationLocation.latitude}", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))
        LaunchButton(
            "Uber",
            R.drawable.logo_uber
        ) {
            UberLauncher.launch(context, destinationLocation!!)
        }

        Spacer(modifier = Modifier.height(16.dp))
        LaunchButton(
            "Rapido",
            R.drawable.logo_rapido
        ) {
            RapidoLauncher.launch(context, destinationLocation!!)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LaunchButton(
            "Ola",
            R.drawable.logo_ola
        ) {
            OlaLauncher.launch(context, destinationLocation!!)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LaunchButton(
            "Namma Yatri",
            R.drawable.logo_namma
        ) {
            NammaYatriLauncher.launch(context, destinationLocation!!)
        }
    }

}

