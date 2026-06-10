package com.example.openride.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.openride.R
import com.example.openride.ui.theme.BlueBrand
import com.example.openride.ui.theme.Flurocent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenRideTopBar() {
    TopAppBar( 
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BlueBrand,
            titleContentColor = Flurocent
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ride_logo),
                    contentDescription = "Open Ride",
                    Modifier.height(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("")
            }
        }
    )
}