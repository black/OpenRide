package com.example.openride

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.openride.ui.theme.OpenRideTheme
import androidx.compose.ui.unit.dp
import com.example.openride.ui.HomeScreen
import com.example.openride.ui.OpenRideTopBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        HomeScreen(
                            sharedText = sharedText,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }

                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    OpenRideTheme {
        Scaffold { innerPadding ->
            HomeScreen(
                sharedText = "https://maps.app.goo.gl/test",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}