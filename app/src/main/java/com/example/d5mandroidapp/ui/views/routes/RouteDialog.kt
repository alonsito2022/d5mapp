package com.example.d5mandroidapp.ui.views.routes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.states.RouteState
import com.example.d5mandroidapp.data.states.SaleOrderState
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.viewmodels.OrderViewModel
import com.example.d5mandroidapp.ui.viewmodels.RouteViewModel
import com.example.d5mandroidapp.ui.views.components.CustomDatePicker
import com.example.d5mandroidapp.ui.views.components.CustomRadioGroup
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RouteDialog(
    routeState: RouteState,
    routeViewModel: RouteViewModel,
    onDismiss: () -> Unit,
    searchButtonClicked: Boolean,
    onSearchButtonClicked: (Boolean) -> Unit

){
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ){
                item {
                    Text(text = "Busqueda de zonas", fontFamily = FontFamily.SansSerif, style = MaterialTheme.typography.headlineSmall)

                    Text(text = "Fecha: ", fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif, color = Color.Gray)
                    CustomDatePicker(
                        routeState.visitDate,
                        routeViewModel::setSelectedVisitDate
                    )

                    Text(text = "Grupo o cuadrilla: ", fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif, color = Color.Gray)
                    CustomRadioGroup(
                    namesList = routeState.gangsList,
                    idsList = routeState.gangsIdsList,
                    setSelectedItem = routeViewModel::setSelectedTruck)

                    Text(text = "Zona: ", fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif, color = Color.Gray)
                    CustomRadioGroup( namesList = routeState.zoneCenterNameList, idsList = routeState.zoneCenterIdList, setSelectedItem = routeViewModel::setSelectedZoneCenter)

                }


                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { onDismiss() },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                        ) {
                            Text(text = "Cerrar")
                        }
                        Button(
                            onClick = {
                                onDismiss()
                                onSearchButtonClicked(true)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenJC)
                        ) {
                            Text(text = "Buscar")
                        }
                    }
                }
            }
        }
        Log.d("Marker", "Dialog recomposition...")
    }



}



