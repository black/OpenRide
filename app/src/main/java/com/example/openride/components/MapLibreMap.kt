package com.example.openride.components

import android.graphics.BitmapFactory
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.openride.RideLocation
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.geojson.Point

@Composable
fun MapLibreMap(
    modifier: Modifier = Modifier,
    location: RideLocation
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                onCreate(null)

                getMapAsync { map ->
                    map.setStyle(
                        "https://tiles.openfreemap.org/styles/liberty"
                    ) {
                        map.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(location.latitude, location.longitude))
                            .zoom(15.0) // Add zoom level (1-22, 15 is street level)
                            .build()

                        // Add marker here
                        val pointAnnotationManager = this.annotations.createPointAnnotationManager()

                        val pointAnnotationOptions = PointAnnotationOptions()
                            .withPoint(Point.fromLngLat(location.longitude, location.latitude))
                            .withIconImage(
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    android.R.drawable.ic_menu_mylocation
                                )
                            )
                        pointAnnotationManager.create(pointAnnotationOptions)
                    }
                }
            }
        },
        update = { mapView ->
            mapView.getMapAsync { map ->
                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(location.latitude, location.longitude))
                    .zoom(15.0)
                    .build()

            }
        }
    )
}