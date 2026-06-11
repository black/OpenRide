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
    origin: RideLocation,
    destination: RideLocation
) {
    val scope = rememberCoroutineScope()

    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                onCreate(null)

                getMapAsync { map ->
                    map.setStyle("https://tiles.openfreemap.org/styles/liberty") { style ->

                        // Fit camera to show both points
                        val bounds = LatLngBounds.Builder()
                            .include(LatLng(origin.latitude, origin.longitude))
                            .include(LatLng(destination.latitude, destination.longitude))
                            .build()
                        map.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(bounds, 100)
                        )

                        // Add marker icons
                        val markerDrawable = ResourcesCompat.getDrawable(
                            context.resources,
                            org.maplibre.android.R.drawable.maplibre_user_icon_shadow,
                            null
                        )!!
                        style.addImage("marker-icon", markerDrawable.toBitmap())

                        // Origin marker
                        style.addSource(
                            GeoJsonSource(
                                "origin-source",
                                Feature.fromGeometry(
                                    Point.fromLngLat(origin.longitude, origin.latitude)
                                )
                            )
                        )
                        style.addLayer(
                            SymbolLayer("origin-layer", "origin-source").withProperties(
                                PropertyFactory.iconImage("marker-icon"),
                                PropertyFactory.iconAllowOverlap(true)
                            )
                        )

                        // Destination marker
                        style.addSource(
                            GeoJsonSource(
                                "destination-source",
                                Feature.fromGeometry(
                                    Point.fromLngLat(destination.longitude, destination.latitude)
                                )
                            )
                        )
                        style.addLayer(
                            SymbolLayer("destination-layer", "destination-source").withProperties(
                                PropertyFactory.iconImage("marker-icon"),
                                PropertyFactory.iconAllowOverlap(true)
                            )
                        )

                        // Empty route source — will be filled after API call
                        style.addSource(GeoJsonSource("route-source"))
                        style.addLayer(
                            LineLayer("route-layer", "route-source").withProperties(
                                PropertyFactory.lineColor("#3b82f6"),  // blue
                                PropertyFactory.lineWidth(5f),
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
                            )
                        )

                        // Fetch route from OSRM
                        scope.launch(Dispatchers.IO) {
                            val points = fetchRoute(origin, destination)
                            if (points != null) {
                                withContext(Dispatchers.Main) {
                                    val routeSource = style.getSourceAs<GeoJsonSource>("route-source")
                                    routeSource?.setGeoJson(
                                        Feature.fromGeometry(LineString.fromLngLats(points))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        update = { mapView ->
            // No update needed — origin/destination are fixed
        }
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