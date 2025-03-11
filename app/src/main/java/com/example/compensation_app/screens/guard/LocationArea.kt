package com.example.landareacalculator

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.*

class LandAreaTracker(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val _locationList = MutableStateFlow<List<LatLng>>(emptyList())
    val locationList: StateFlow<List<LatLng>> = _locationList
    private var tracking = false

    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun startTracking() {
        if (!isGPSEnabled()) {
            requestEnableGPS()
            return
        }

        if (tracking) return // Prevent multiple starts

        tracking = true
        _locationList.value = emptyList() // Clear old data only when we start tracking

        val locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (tracking) {
                    val newLocations = locationResult.locations.map { LatLng(it.latitude, it.longitude) }
                    Log.d("Location Tracking", "Received: ${newLocations.size} new locations")

                    if (newLocations.isNotEmpty()) {
                        _locationList.value = (_locationList.value + newLocations).toList()
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback!!, null)
    }


    fun stopTracking(): Double {
        tracking = false

        // Stop location updates
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
            locationCallback = null
        }

        val storedPoints = _locationList.value
        Log.d("Tracking", "Total Locations Collected: ${storedPoints.size}")

        if (storedPoints.size < 3) {
            Log.e("Area Calculation", "Not enough points to calculate area.")
            return 0.0
        }

        val closedPoints = closeLoopIfNeeded(storedPoints)
        val area = calculatePolygonArea(closedPoints)

        Log.d("Calculated Area", "Final Area: $area square meters")
        return area
    }



    private fun closeLoopIfNeeded(points: List<LatLng>): List<LatLng> {
        if (points.size < 3) return points

        val first = points.first()
        val last = points.last()

        val distance = calculateHaversineDistance(first.latitude, first.longitude, last.latitude, last.longitude)

        Log.d("Loop Closure", "First and Last Point Distance: $distance meters")

        return if (distance > 5.0) points + first else points
    }

    private fun calculatePolygonArea(points: List<LatLng>): Double {
        if (points.size < 3) return 0.0

        val earthRadius = 6378137.0 // in meters (WGS84 standard)
        var area = 0.0

        for (i in points.indices) {
            val p1 = points[i]
            val p2 = points[(i + 1) % points.size]

            val x1 = Math.toRadians(p1.longitude) * earthRadius * cos(Math.toRadians(p1.latitude))
            val y1 = Math.toRadians(p1.latitude) * earthRadius
            val x2 = Math.toRadians(p2.longitude) * earthRadius * cos(Math.toRadians(p2.latitude))
            val y2 = Math.toRadians(p2.latitude) * earthRadius

            area += x1 * y2 - x2 * y1
        }

        return abs(area) / 2.0 // Final area in square meters
    }

    private fun calculateHaversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371000 // Earth radius in meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    // ✅ Check if GPS is enabled
    private fun isGPSEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // ✅ Prompt the user to enable GPS
    private fun requestEnableGPS() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }
}@Composable
fun MapScreen(points: List<LatLng>) {
    val cameraPositionState = rememberCameraPositionState()

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp), // Larger Map Size
        cameraPositionState = cameraPositionState
    ) {
        if (points.size >= 3) {
            val closedPoints = points + points.first() // Close the loop

            // Draw the enclosed area
            Polygon(
                points = closedPoints,
                fillColor = Color(0x8000FF00), // Semi-transparent green
                strokeColor = Color.Green,
                strokeWidth = 5f
            )
        }
    }

    // Ensure camera focuses on the drawn area
    LaunchedEffect(points) {
        if (points.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.Builder()
            points.forEach { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 50))
        }
    }
}

@Composable
fun ControlButtons(landAreaTracker: LandAreaTracker, onAreaCalculated: (Double) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { landAreaTracker.startTracking() }) {
            Text("Start Tracking")
        }
        Button(onClick = {
            val area = landAreaTracker.stopTracking()
            Log.d("Area", "Damaged Land Area: $area square meters")
            onAreaCalculated(area)
        }) {
            Text("Stop & Calculate")
        }
    }
}
