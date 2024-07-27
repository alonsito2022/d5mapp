package com.example.d5mandroidapp.ui.views.routes

//import com.google.accompanist.permissions.rememberMultiplePermissionsState
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.PermissionsRequired

import android.Manifest
import android.annotation.SuppressLint

import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.d5mandroidapp.navigation.Screens
import com.example.d5mandroidapp.storage.UserRepositoryImpl
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.viewmodels.RouteViewModel
import com.example.d5mandroidapp.ui.viewmodels.WithoutOrderViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun RouteScreen(navController: NavController) {
    val routeViewModel: RouteViewModel = hiltViewModel()
    val state by routeViewModel.state.collectAsState()
    val withoutOrderViewModel: WithoutOrderViewModel = hiltViewModel()
    val withoutOrderState by withoutOrderViewModel.state.collectAsState()
    var fabClicked by rememberSaveable { mutableStateOf(false) }
    var searchButtonClicked by rememberSaveable { mutableStateOf(false) }
    var markedClicked by rememberSaveable { mutableStateOf(0) }
    val context = LocalContext.current

    val avelinoOval = LatLng(-16.42237728568478, -71.54430541872024)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(avelinoOval, 17f)
    }
    val multiplePermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        multiplePermissionState.launchMultiplePermissionRequest()
    }

    if (multiplePermissionState.allPermissionsGranted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Rutas asignadas",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )

                ExtendedFloatingActionButton(
                    onClick = { fabClicked=true},
                    expanded = true,
                    containerColor = GreenJC,
                    contentColor = Color.Black,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 10.dp
                    ),
                    icon = { Icon(Icons.Filled.Edit, "Extended floating action button.") },
                    text = { Text(text = "Consultar") },
                )

            }


            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = 2.dp,
                color = Color.DarkGray
            )
            if (state.dailyRoutes.isNotEmpty()){
                Text(
                    text = "Se encontraron: ${state.dailyRoutes.size} resultados.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp

                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            GoogleMapView(modifier= Modifier.weight(1f),
                state = state,
                cameraPositionState= cameraPositionState,
                markedClicked = markedClicked,
                onMarkedClicked = {markedClicked = it})

            LaunchedEffect(key1 = markedClicked){
                if (markedClicked > 0){
                    routeViewModel.setSelectedDailyRoute(markedClicked)
                    withoutOrderViewModel.setDailyRouteId(markedClicked)
                    Log.d("Marker", "markedClicked... ${markedClicked}")
                }
                markedClicked = 0
            }

            if(state.isOpenMarkerDialog){
                Text(
                    text = "CODIGO: ${state.selectedDailyRoute.routePersonCode}",
                    color =  Color.DarkGray,
                    modifier = Modifier.padding(all = 0.dp),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "CLIENTE: ${state.selectedDailyRoute.routePersonNames}",
                    color =  Color.DarkGray,
                    modifier = Modifier.padding(all = 0.dp),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "${state.selectedDailyRoute.routePersonDocumentTypeReadable}: ${state.selectedDailyRoute.routePersonDocumentNumber}",
                    color =  Color.DarkGray,
                    modifier = Modifier.padding(all = 0.dp),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "DIRECCIÓN: ${state.selectedDailyRoute.routeAddress}",
                    color =  Color.DarkGray,
                    modifier = Modifier.padding(all = 0.dp),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "LISTA DE PRECIOS: ${state.selectedDailyRoute.routePersonTypeTradeName}",
                    color =  Color.DarkGray,
                    modifier = Modifier.padding(all = 0.dp),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
                Button(onClick = {
                    val action = Screens.Order.screen + "/${state.selectedDailyRoute.routePersonId}/${state.selectedDailyRoute.routeAddressId}/${state.selectedDailyRoute.routeDailyRouteId}"
                    navController.navigate(action)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Realizar pedido")
                }
                Button(
                    onClick = {
                        withoutOrderViewModel.showConfirmDialog()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                ){
                    Text(text = "Sin pedido")
                }
            }

        }

    } else {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            Text(
                modifier = Modifier.padding(top = 6.dp),
                textAlign = TextAlign.Center,
                text = "This app functions 150% times better with precise location enabled"
            )
            Button(modifier = Modifier.padding(top = 12.dp), onClick = {
                multiplePermissionState.launchMultiplePermissionRequest()
            }) {
                Text(text = "Grant Permission")
            }
        }
    }

    LaunchedEffect(key1 = fabClicked ){
        if(fabClicked){
            routeViewModel.showRouteDialog()
            routeViewModel.cleanList()
            Log.d("Marker", "fabClicked...")
        }
        fabClicked = false
    }
    if (state.isOpenSearchDialog) {
        RouteDialog(
            routeState = state,
            routeViewModel = routeViewModel,
            onDismiss = routeViewModel::hideRouteDialog,
            searchButtonClicked
        ){searchButtonClicked = it}
        Log.d("Marker", "isOpenSearchDialog recomposition...")
    }

    LaunchedEffect(key1 = searchButtonClicked){
        if (searchButtonClicked){
            routeViewModel.searchQuery()
            cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(state.zoneCenterLatitude, state.zoneCenterLongitude), 17f)
            Log.d("Marker", "searchButtonClicked...")
        }
        searchButtonClicked = false
    }

    if (withoutOrderState.isOpenConfirmDialog) {
        ConfirmDialog(
            withoutOrderState = withoutOrderState,
            onDismiss = withoutOrderViewModel::hideConfirmDialog,
            onTextChangeTextField = withoutOrderViewModel::onObservationTextChange,
            onUpdateButtonClicked = withoutOrderViewModel::saveWithoutOrder,
            onChangeSelectReason = withoutOrderViewModel::onChangeSelectReason
        )
    }
    if (withoutOrderState.success){
        Toast.makeText(context, withoutOrderState.message, Toast.LENGTH_LONG).show()
        routeViewModel.searchQuery()
        routeViewModel.hideMarkerDialog()
        withoutOrderViewModel.setSuccessOrError()
    }
    if (withoutOrderState.error){
        Toast.makeText(context, withoutOrderState.message, Toast.LENGTH_LONG).show()
        withoutOrderViewModel.setSuccessOrError()
    }
}