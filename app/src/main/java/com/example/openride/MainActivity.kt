package com.example.openride

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.openride.ui.theme.OpenRideTheme
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.openride.ui.HomeScreen
import com.example.openride.ui.OpenRideTopBar

class MainActivity : ComponentActivity() {

    private lateinit var locationHelper: LocationHelper
    private var location by mutableStateOf<RideLocation?>(null)
    // Register the permission launcher
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) fetchLocation()
            else Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        locationHelper = LocationHelper(this)
        // Request permission then get location
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        val sharedText =
            intent?.getStringExtra(Intent.EXTRA_TEXT) ?: ""

        setContent {
            OpenRideTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        OpenRideTopBar()
                    }
                ) { innerPadding ->
                    HomeScreen(
                        sharedText = sharedText,
                        originLocation=location,
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun fetchLocation() {
        locationHelper.getCurrentLocation(
            onSuccess = { lat, lng ->
                location = RideLocation(lat, lng)
                Log.d("Location", "Lat: $lat, Lng: $lng")
                Toast.makeText(this, "Lat: $lat\nLng: $lng", Toast.LENGTH_LONG).show()
            },
            onFailure = { e ->
                Log.e("Location", "Error: ${e.message}")
            }
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    OpenRideTheme {
//        Scaffold { innerPadding ->
//            HomeScreen(
//                sharedText = "https://maps.app.goo.gl/test",
//                modifier = Modifier.padding(innerPadding)
//            )
//        }
//    }
//}