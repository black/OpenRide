package com.example.openride.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.openride.LocationParser
import com.example.openride.R
import com.example.openride.UrlResolver
import com.example.openride.applauncher.NammaYatriLauncher
import com.example.openride.applauncher.OlaLauncher
import com.example.openride.applauncher.RapidoLauncher
import com.example.openride.applauncher.UberLauncher
import com.example.openride.components.LaunchButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun HomeScreen(
    sharedText: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var expandedUrl by remember {
        mutableStateOf("")
    }

    val location = remember(expandedUrl) {
        LocationParser.parse(expandedUrl)
    }

    LaunchedEffect(sharedText) {
        if (sharedText.isNotBlank()) {
            expandedUrl = withContext(Dispatchers.IO) {
                UrlResolver.resolve(sharedText) ?: ""
            }
        }
    }
    
    Column(
        modifier = modifier.padding(8.dp),
    ) {
        Text("Copied Location", style = MaterialTheme.typography.bodyLarge)
        Text(expandedUrl, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Location", style = MaterialTheme.typography.bodyLarge)
        location?.let {
            Text("Lat : ${location.longitude} , Long : ${location.latitude}", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))
        LaunchButton(
            "Uber",
            R.drawable.logo_uber
        ) {
            UberLauncher.launch(context, location!!)
        }

        Spacer(modifier = Modifier.height(16.dp))
        LaunchButton(
            "Rapido",
            R.drawable.logo_rapido
        ) {
            RapidoLauncher.launch(context, location!!)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LaunchButton(
            "Ola",
            R.drawable.logo_ola
        ) {
            OlaLauncher.launch(context, location!!)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LaunchButton(
            "Namma Yatri",
            R.drawable.logo_namma
        ) {
            NammaYatriLauncher.launch(context, location!!)
        }
    }



}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(
        "https://maps.app.goo.gl/test"
    )
}
