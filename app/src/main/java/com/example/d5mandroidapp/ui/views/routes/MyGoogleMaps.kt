package com.example.d5mandroidapp.ui.views.routes
import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.d5mandroidapp.data.states.PermissionEvent
import com.example.d5mandroidapp.data.states.ViewState
import com.example.d5mandroidapp.ui.viewmodels.ClientViewModel
import com.example.d5mandroidapp.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
//import com.google.accompanist.permissions.rememberMultiplePermissionsState
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.PermissionsRequired
import com.example.d5mandroidapp.extension.hasLocationPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.CameraPositionState
import android.provider.Settings
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import androidx.compose.foundation.background
import androidx.compose.ui.text.style.TextAlign
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState

//@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.S)

@Composable
fun MyGoogleMaps(){
    val context = LocalContext.current

//    val multiplePermissionState = rememberMultiplePermissionsState(
//        permissions = listOf(
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//    )
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(LatLng(44.810058, 20.4617586), 17f)
//    }
//
//    LaunchedEffect(Unit) {
//        multiplePermissionState.launchMultiplePermissionRequest()
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Blue)
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Welcome to the MapsApp!",
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = TextAlign.Center
//        )
//        Spacer(modifier = Modifier.height(32.dp))
//        PermissionsRequired(
//            multiplePermissionsState = multiplePermissionState,
//            permissionsNotGrantedContent = { /* ... */ },
//            permissionsNotAvailableContent = { /* ... */ }
//        ) {


//    val singapore = LatLng(1.35, 103.87)
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(singapore, 10f)
//    }
//
//    GoogleMap(
//        modifier = Modifier.fillMaxSize(),
//        cameraPositionState = cameraPositionState,
//        properties = MapProperties(
////            isMyLocationEnabled = true,
//            mapType = MapType.HYBRID)
//    ) {
//        Marker(
//            state = MarkerState(position = singapore),
//            title = "Singapore",
//            snippet = "Marker in Singapore"
//        )
//    }


//            GoogleMap(
//                cameraPositionState = cameraPositionState,
//                modifier = Modifier.fillMaxSize().weight(1f),
//                properties = MapProperties(isMyLocationEnabled = true),
//                uiSettings = MapUiSettings(compassEnabled = true)
//            ) {
//                GoogleMarkers()
//                Polyline(
//                    points = listOf(
//                        LatLng(44.811058, 20.4617586),
//                        LatLng(44.811058, 20.4627586),
//                        LatLng(44.810058, 20.4627586),
//                        LatLng(44.809058, 20.4627586),
//                        LatLng(44.809058, 20.4617586)
//                    )
//                )
//            }
//        }
//    }

    val locationViewModel: MainViewModel = hiltViewModel()

//    val permissionState = rememberMultiplePermissionsState(
//        permissions = listOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//    )

    val viewState by locationViewModel.viewState.collectAsState()

//    LaunchedEffect(!context.hasLocationPermission()) {
//        permissionState.launchMultiplePermissionRequest()
//    }
//
//    when {
//        permissionState.allPermissionsGranted -> {
//            LaunchedEffect(Unit) {
//                locationViewModel.handle(PermissionEvent.Granted)
//            }
//        }
//
//        permissionState.shouldShowRationale -> {
//            RationaleAlert(onDismiss = { }) {
//                permissionState.launchMultiplePermissionRequest()
//            }
//        }
//
//        !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
//            LaunchedEffect(Unit) {
//                locationViewModel.handle(PermissionEvent.Revoked)
//            }
//        }
//    }


    with(viewState) {
        when (this) {
            ViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            ViewState.RevokedPermissions -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("We need permissions to use this app")
                    Button(
                        onClick = {
                            val i = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
                            context.startActivity(i)
//                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        },
                        enabled = !context.hasLocationPermission()
                    ) {
                        if (context.hasLocationPermission()) CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = Color.White
                        )
                        else Text("Settings")
                    }
                }
            }

            is ViewState.Success -> {
                val currentLoc =
                    LatLng(
                        this.location?.latitude ?: 0.0,
                        this.location?.longitude ?: 0.0
                    )
                val cameraState = rememberCameraPositionState()

                LaunchedEffect(key1 = currentLoc) {
                    cameraState.centerOnLocation(currentLoc)
                }

                MainScreen(
                    currentPosition = LatLng(
                        currentLoc.latitude,
                        currentLoc.longitude
                    ),
                    cameraState = cameraState
                )
            }
        }
    }



}


//@Composable
//fun GoogleMarkers() {
//    Marker(
//        state = rememberMarkerState(position = LatLng(44.811058, 20.4617586)),
//        title = "Marker1",
//        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
//    )
//    Marker(
//        state = rememberMarkerState(position = LatLng(44.811058, 20.4627586)),
//        title = "Marker2",
//        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
//    )
//    Marker(
//        state = rememberMarkerState(position = LatLng(44.810058, 20.4627586)),
//        title = "Marker3",
//        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
//    )
//    Marker(
//        state = rememberMarkerState(position = LatLng(44.809058, 20.4627586)),
//        title = "Marker4",
//        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
//    )
//    Marker(
//        state = rememberMarkerState(position = LatLng(44.809058, 20.4617586)),
//        title = "Marker5",
//        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
//    )
//}


@Composable
fun MainScreen(currentPosition: LatLng, cameraState: CameraPositionState) {
    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraState,
        properties = MapProperties(
            isMyLocationEnabled = true,
            mapType = MapType.HYBRID,
            isTrafficEnabled = true
        )
    ) {
        Marker(
            state = MarkerState(position = marker),
            title = "MyPosition",
            snippet = "This is a description of this Marker",
            draggable = true
        )
    }
}

@Composable
fun RationaleAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "We need location permissions to use this app",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                ) {
                    Text("OK")
                }
            }
        }
    }
}

private suspend fun CameraPositionState.centerOnLocation(
    location: LatLng
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location,
        15f
    ),
    durationMs = 1500
)