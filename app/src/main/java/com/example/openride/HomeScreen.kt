package com.example.openride

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.openride.applauncher.NammaYatriLauncher
import com.example.openride.applauncher.OlaLauncher
import com.example.openride.applauncher.RapidoLauncher
import com.example.openride.applauncher.UberLauncher
import com.example.openride.components.LaunchButton
import com.example.openride.rides.RideAppDetector
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

    if (location == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Location not found")
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            LaunchButton(
                "Uber",
                R.drawable.logo_uber
            ) {
                UberLauncher.launch(context, location)
            }
        }

        item {
            LaunchButton(
                "Rapido",
                R.drawable.logo_rapido
            ) {
                RapidoLauncher.launch(context, location)
            }
        }

        item {
            LaunchButton(
                "Ola",
                R.drawable.logo_ola
            ) {
                OlaLauncher.launch(context, location)
            }
        }

        item {
            LaunchButton(
                "Namma Yatri",
                R.drawable.logo_namma
            ) {
                NammaYatriLauncher.launch(context, location)
            }
        }
    }
}