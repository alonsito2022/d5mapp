package com.example.d5mandroidapp.ui.views.routes

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.data.states.RouteState
import com.example.d5mandroidapp.utils.createDrawableFromView
import com.example.d5mandroidapp.utils.getBitmapFromImage
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun GoogleMapView(
    modifier: Modifier,
    state: RouteState,
    cameraPositionState: CameraPositionState,
    markedClicked: Int,
    onMarkedClicked:(Int)->Unit
){

    val context = LocalContext.current
//    val bitmap = getBitmapFromImage(context, R.drawable.image_enhacer)
    val inflater = LayoutInflater.from(context)
    val viewMarker = inflater.inflate(R.layout.custom_marker_layout, null)
    val textViewNum = viewMarker.findViewById<TextView>(R.id.textViewNum)
    val imageViewMarker = viewMarker.findViewById<ImageView>(R.id.imageViewMarker)

    GoogleMap(
        modifier = modifier.height(400.dp),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(compassEnabled = true)

    ) {


        state.dailyRoutes.forEach { dailyRoute ->
            when(dailyRoute.routeStatus){
                "01" -> { imageViewMarker.setImageResource(R.drawable.ic_marker_blue) }
                "03" -> { imageViewMarker.setImageResource(R.drawable.ic_marker_red) }
                "04" -> { imageViewMarker.setImageResource(R.drawable.ic_marker_yellow) }
                "05" -> { imageViewMarker.setImageResource(R.drawable.ic_marker_sky_blue) }
                "06" -> { imageViewMarker.setImageResource(R.drawable.ic_marker_dark_green) }
                else -> TODO("Unhandled route status: ${dailyRoute.routeStatus}")
            }
            textViewNum.text = dailyRoute.routePersonCode.toString()
            Marker(
                state = rememberMarkerState(position = LatLng(dailyRoute.routeLatitude, dailyRoute.routeLongitude)),
                icon = createDrawableFromView(context, viewMarker),
//                title = dailyRoute.routeDailyRouteId.toString(),
//                tag = dailyRoute.routeDailyRouteId.toString(),
                onClick = {
//                    markedClicked = dailyRoute.routeDailyRouteId
                    onMarkedClicked(dailyRoute.routeDailyRouteId)
                    false
                }
            )

        }
//        if(state.dailyRoutes.isNotEmpty()){
//            val index = state.dailyRoutes.size - 1
//            cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(state.dailyRoutes[index].routeLatitude, state.dailyRoutes[index].routeLongitude), 17f)
//            Log.d("Marker", "cameraPositionState...")
//        }
        Polyline(points = state.latLngList, color = Color(0xFF03DAC5))
        Log.d("Marker", "GoogleMap recomposite...")
    }
}
