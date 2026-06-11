package com.example.openride.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.openride.RideLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.LineString
import org.maplibre.geojson.Point
@Composable
fun MapLibreMap(
    modifier: Modifier = Modifier,
    origin: RideLocation?,
    destination: RideLocation?
) {
    val scope = rememberCoroutineScope()

    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                onCreate(null)

                getMapAsync { map ->
                    map.setStyle("https://tiles.openfreemap.org/styles/liberty") { style ->

                        // Camera position
                        when {
                            origin != null && destination != null -> {
                                val bounds = LatLngBounds.Builder()
                                    .include(LatLng(origin.latitude, origin.longitude))
                                    .include(LatLng(destination.latitude, destination.longitude))
                                    .build()

                                map.animateCamera(
                                    CameraUpdateFactory.newLatLngBounds(bounds, 100)
                                )
                            }

                            origin != null -> {
                                map.animateCamera(
                                    CameraUpdateFactory.newCameraPosition(
                                        CameraPosition.Builder()
                                            .target(
                                                LatLng(
                                                    origin.latitude,
                                                    origin.longitude
                                                )
                                            )
                                            .zoom(14.0)
                                            .build()
                                    )
                                )
                            }

                            destination != null -> {
                                map.animateCamera(
                                    CameraUpdateFactory.newCameraPosition(
                                        CameraPosition.Builder()
                                            .target(
                                                LatLng(
                                                    destination.latitude,
                                                    destination.longitude
                                                )
                                            )
                                            .zoom(14.0)
                                            .build()
                                    )
                                )
                            }

                            else -> {
                                // Default location (Bangalore)
                                map.animateCamera(
                                    CameraUpdateFactory.newCameraPosition(
                                        CameraPosition.Builder()
                                            .target(LatLng(12.9716, 77.5946))
                                            .zoom(10.0)
                                            .build()
                                    )
                                )
                            }
                        }

                        // Marker icon
                        val markerDrawable = ResourcesCompat.getDrawable(
                            context.resources,
                            org.maplibre.android.R.drawable.maplibre_user_icon_shadow,
                            null
                        )!!

                        style.addImage(
                            "marker-icon",
                            markerDrawable.toBitmap()
                        )

                        // Origin marker
                        origin?.let {
                            style.addSource(
                                GeoJsonSource(
                                    "origin-source",
                                    Feature.fromGeometry(
                                        Point.fromLngLat(
                                            it.longitude,
                                            it.latitude
                                        )
                                    )
                                )
                            )

                            style.addLayer(
                                SymbolLayer(
                                    "origin-layer",
                                    "origin-source"
                                ).withProperties(
                                    PropertyFactory.iconImage("marker-icon"),
                                    PropertyFactory.iconAllowOverlap(true)
                                )
                            )
                        }

                        // Destination marker
                        destination?.let {
                            style.addSource(
                                GeoJsonSource(
                                    "destination-source",
                                    Feature.fromGeometry(
                                        Point.fromLngLat(
                                            it.longitude,
                                            it.latitude
                                        )
                                    )
                                )
                            )

                            style.addLayer(
                                SymbolLayer(
                                    "destination-layer",
                                    "destination-source"
                                ).withProperties(
                                    PropertyFactory.iconImage("marker-icon"),
                                    PropertyFactory.iconAllowOverlap(true)
                                )
                            )
                        }

                        // Route layer
                        style.addSource(GeoJsonSource("route-source"))

                        style.addLayer(
                            LineLayer(
                                "route-layer",
                                "route-source"
                            ).withProperties(
                                PropertyFactory.lineColor("#3b82f6"),
                                PropertyFactory.lineWidth(5f),
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
                            )
                        )

                        // Fetch route only if both locations exist
                        if (origin != null && destination != null) {
                            scope.launch(Dispatchers.IO) {
                                val points = fetchRoute(origin, destination)

                                if (points != null) {
                                    withContext(Dispatchers.Main) {
                                        style.getSourceAs<GeoJsonSource>("route-source")
                                            ?.setGeoJson(
                                                Feature.fromGeometry(
                                                    LineString.fromLngLats(points)
                                                )
                                            )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        update = {}
    )
}

// Fetch driving route from OSRM (free, no API key)
suspend fun fetchRoute(
    origin: RideLocation,
    destination: RideLocation
): List<Point>? {
    return withContext(Dispatchers.IO) {
        try {
            val url = "https://router.project-osrm.org/route/v1/driving/" +
                    "${origin.longitude},${origin.latitude};" +
                    "${destination.longitude},${destination.latitude}" +
                    "?overview=full&geometries=geojson"

            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: return@withContext null

            val json = JSONObject(body)
            val coords = json
                .getJSONArray("routes")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONArray("coordinates")

            (0 until coords.length()).map { i ->
                val coord = coords.getJSONArray(i)
                Point.fromLngLat(coord.getDouble(0), coord.getDouble(1))
            }
        } catch (e: Exception) {
            null
        }
    }
}