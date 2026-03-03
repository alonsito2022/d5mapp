package com.example.d5mandroidapp.ui.views.clients

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.models.ProductTariff
import com.example.d5mandroidapp.navigation.Screens
import com.example.d5mandroidapp.ui.theme.AccentJC
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.theme.PrimaryLightJC
import com.example.d5mandroidapp.ui.theme.Purple200
import com.example.d5mandroidapp.ui.theme.Purple40
import com.example.d5mandroidapp.ui.theme.Purple500
import com.example.d5mandroidapp.ui.theme.Purple80
import com.example.d5mandroidapp.ui.theme.Teal200
import com.example.d5mandroidapp.ui.viewmodels.ClientViewModel
import com.example.d5mandroidapp.ui.viewmodels.UserViewModel
import com.example.d5mandroidapp.ui.viewmodels.WithoutOrderViewModel


@Composable
fun ClientItem(
    dailyRoute: DailyRoute,
    modifier: Modifier = Modifier,
    withoutOrderViewModel: WithoutOrderViewModel,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable { Log.d("MyCard", "You clicked ${dailyRoute.routePersonNames}") },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column (modifier = Modifier
            .fillMaxWidth()
            .background(if(dailyRoute.routeStatus=="06") GreenJC else if(dailyRoute.routeStatus=="05") Teal200 else if(dailyRoute.routeStatus=="04") PrimaryLightJC else if(dailyRoute.routeStatus=="03") colorResource(id=R.color.danger_light) else Color.White)
            .padding(8.dp)){
            Text(
                text = "CODIGO: ${dailyRoute.routePersonCode} ${dailyRoute.routeStatus}",
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
            )
            Text(
                text = dailyRoute.routePersonNames, fontSize = 18.sp,
            )

            Text(
                text = "${dailyRoute.routePersonDocumentTypeReadable}: ${dailyRoute.routePersonDocumentNumber}\nDIRECCION: ${dailyRoute.routeAddress}\nLISTA DE PRECIOS: ${dailyRoute.routePersonTypeTradeName}",
                color = Color.DarkGray, fontSize = 12.sp, lineHeight = 12.sp
            )
            Row(
                modifier= Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = {
                        val action = Screens.Order.screen + "/${dailyRoute.routePersonId}/${dailyRoute.routeAddressId}/${dailyRoute.routeDailyRouteId}"
                        navController.navigate(action)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id=R.color.yellow))
                ) {
                    Text(text = "PEDIDO", color = Color.Black)
                }
                OutlinedButton(
                    onClick = {
                        val action = Screens.Debt.screen + "/${dailyRoute.routePersonId}"
                        navController.navigate(action)
                    },
                    Modifier.padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id=R.color.green))
                ) {
                    Text(text = "COBRAR", color = Color.White)
                }
                OutlinedButton(
                    onClick = {
                        withoutOrderViewModel.setDailyRouteId(dailyRoute.routeDailyRouteId)
                        withoutOrderViewModel.showConfirmDialog()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id=R.color.red))
                ) {
                    Text(text = "SP", color = Color.White)

                }
            }

        }

    }


}

@Composable
fun ClientItemFromClient(
    client: Client,
    modifier: Modifier = Modifier,
    withoutOrderViewModel: WithoutOrderViewModel,
    navController: NavController,
    clientViewModel: ClientViewModel
) {
    val scope = rememberCoroutineScope()
    var showDateDialog by remember { mutableStateOf(false) }
    val visitDates = client.visitDatesCurrentWeek
    
    val statusText = buildString {
        if (client.isBlocked) append(" BLOQUEADO")
        if (client.isSuspended) append(" SUSPENDIDO")
        if (client.isObserved) append(" OBSERVADO")
    }
    
    // Diálogo para seleccionar fecha si hay múltiples fechas
    if (showDateDialog && visitDates.size > 1) {
        VisitDateSelectionDialog(
            visitDates = visitDates,
            clientName = client.names,
            onDismiss = { showDateDialog = false },
            onDateSelected = { selectedDate ->
                showDateDialog = false
                // Usar la fecha seleccionada como dailyRouteId
                withoutOrderViewModel.setDailyRouteId(selectedDate)
                withoutOrderViewModel.showConfirmDialog()
            }
        )
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable { Log.d("MyCard", "You clicked ${client.names}") },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (client.isBlocked) colorResource(id = R.color.danger_light)
                    else if (client.isSuspended) Teal200
                    else if (client.isObserved) PrimaryLightJC
                    else Color.White
                )
                .padding(8.dp)
        ) {
            Text(
                text = "CODIGO: ${client.code}$statusText",
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
            )
            Text(
                text = client.names,
                fontSize = 18.sp,
            )

            Text(
                text = "${client.documentTypeReadable}: ${client.documentNumber}\nTELEFONO: ${client.phone}\nLISTA DE PRECIOS: ${client.typeTradeName}",
                color = Color.DarkGray,
                fontSize = 12.sp,
                lineHeight = 12.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(
                    onClick = {
                        // Cargar direcciones y navegar
                        clientViewModel.searchAddresses(client.id.toString())
                        scope.launch {
                            delay(500)
                            val loadedAddresses = clientViewModel.state.value.addresses
                            val address = loadedAddresses.firstOrNull()
                            if (address != null) {
                                val action = Screens.Order.screen + "/${client.id}/${address.id}/0"
                                navController.navigate(action)
                            } else {
                                Log.d("D5MAP", "No address found for client ${client.id}, navigating without address")
                                // Navegar sin dirección si no se encuentra
                                val action = Screens.Order.screen + "/${client.id}/0/0"
                                navController.navigate(action)
                            }
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.yellow))
                ) {
                    Text(text = "PEDIDO", color = Color.Black)
                }
                OutlinedButton(
                    onClick = {
                        val action = Screens.Debt.screen + "/${client.id}"
                        navController.navigate(action)
                    },
                    Modifier.padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green))
                ) {
                    Text(text = "COBRAR", color = Color.White)
                }
                // Mostrar botón SP solo si hay fechas de visita
                if (visitDates.isNotEmpty()) {
                    OutlinedButton(
                        onClick = {
                            // Si hay una sola fecha, usarla directamente
                            if (visitDates.size == 1) {
                                withoutOrderViewModel.setDailyRouteId(visitDates.first())
                                withoutOrderViewModel.showConfirmDialog()
                            } else {
                                // Si hay múltiples fechas, mostrar diálogo de selección
                                showDateDialog = true
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.red))
                    ) {
                        Text(text = "SP", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun VisitDateSelectionDialog(
    visitDates: List<String>,
    clientName: String,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Seleccionar Fecha de Visita",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Cliente: $clientName",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                visitDates.forEach { date ->
                    OutlinedButton(
                        onClick = { onDateSelected(date) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green))
                    ) {
                        Text(text = date, color = Color.White)
                    }
                }
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Text(text = "Cancelar", color = Color.White)
                }
            }
        }
    }
}