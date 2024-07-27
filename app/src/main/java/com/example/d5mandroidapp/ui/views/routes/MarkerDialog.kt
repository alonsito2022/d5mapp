package com.example.d5mandroidapp.ui.views.routes

import android.os.Build
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.states.RouteState
import com.example.d5mandroidapp.data.states.SaleOrderState
import com.example.d5mandroidapp.navigation.Screens
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.theme.Teal700
import com.example.d5mandroidapp.ui.viewmodels.OrderViewModel
import com.example.d5mandroidapp.ui.viewmodels.RouteViewModel
import com.example.d5mandroidapp.ui.views.components.CustomDatePicker
import com.example.d5mandroidapp.ui.views.components.CustomRadioGroup
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MarkerDialog(
    dailyRoute: DailyRoute,
    routeViewModel: RouteViewModel,
    onDismiss: () -> Unit,
    navController: NavController
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
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                item {
                    Text(text = "Cliente seleccionado", fontFamily = FontFamily.SansSerif, style = MaterialTheme.typography.headlineSmall)

                    Text(text = dailyRoute.routePersonNames, fontWeight = FontWeight.Thin, fontFamily = FontFamily.SansSerif, color = Color.Gray)
                    Text(text = dailyRoute.routeAddress, fontWeight = FontWeight.Thin, fontFamily = FontFamily.SansSerif, color = Color.Gray)


                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val action = Screens.Order.screen + "/${dailyRoute.routePersonId}/${dailyRoute.routeAddressId}/${dailyRoute.routeDailyRouteId}"
                            navController.navigate(action)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Hacer pedido")
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Sin pedido")
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Editar Cliente")
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onDismiss() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Text(text = "Cerrar")
                    }

                }

            }


        }
    }
}



