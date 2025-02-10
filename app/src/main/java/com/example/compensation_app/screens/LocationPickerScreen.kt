package com.example.compensation_app.screens//package com.example.compensation_app.screens
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.Address
//import android.location.Geocoder
//import android.location.Location
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.core.app.ActivityCompat
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.rememberPermissionState
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
//import com.google.maps.android.compose.*
//import java.util.*
//
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun LocationPickerScreen(onLocationSelected: (String) -> Unit) {
//    val context = LocalContext.current
//    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
//    val cameraPositionState = rememberCameraPositionState()
//    var userLocation by remember { mutableStateOf<LatLng?>(null) }
//    var address by remember { mutableStateOf("") }
//
//    // Request location permission state
//    val locationPermissionState = rememberPermissionState(
//        permission = Manifest.permission.ACCESS_FINE_LOCATION
//    )
//
//    // Fetch location on permission granted
//    LaunchedEffect(locationPermissionState.hasPermission) {
//        if (locationPermissionState.hasPermission) {
//            if (ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return
//            }
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                location?.let {
//                    val latLng = LatLng(it.latitude, it.longitude)
//                    userLocation = latLng
//                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
//                    address = getAddressFromLatLng(context, it.latitude, it.longitude).toString()
//                }
//            }
//        } else {
//            locationPermissionState.launchPermissionRequest()
//        }
//    }
//
//    Box(Modifier.fillMaxSize()) {
//        // GoogleMap UI
//        GoogleMap(
//            modifier = Modifier.fillMaxSize(),
//            cameraPositionState = cameraPositionState,
//            properties = MapProperties(isMyLocationEnabled = locationPermissionState.hasPermission),
//            uiSettings = MapUiSettings(myLocationButtonEnabled = true),
//            onMapClick = { latLng ->
//                userLocation = latLng
//                address = getAddressFromLatLng(context, latLng.latitude, latLng.longitude).toString()
//            }
//        ) {
//            userLocation?.let { location ->
//                Marker(
//                    state = MarkerState(position = location),
//                    title = "Selected Location"
//                )
//            }
//        }
//
//        // Selected address and button
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(16.dp)
//                .background(Color.White, shape = RoundedCornerShape(8.dp))
//                .padding(8.dp)
//        ) {
//            Text(text = "Selected Address: $address", fontWeight = FontWeight.Bold)
//
//            Button(
//                onClick = { onLocationSelected(address) },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Select This Location")
//            }
//        }
//    }
//}
//
//fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String? {
//    val geocoder = Geocoder(context, Locale.getDefault())
//    val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
//    if (addresses != null) {
//        return if (addresses.isNotEmpty()) {
//            addresses[0].getAddressLine(0) ?: "Unknown Address"
//        } else {
//            "Address not found"
//        }
//    }
//    return null
//}
