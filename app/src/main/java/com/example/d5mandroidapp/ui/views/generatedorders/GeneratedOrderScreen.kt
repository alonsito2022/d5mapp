package com.example.d5mandroidapp.ui.views.generatedorders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.d5mandroidapp.data.models.Operation
import com.example.d5mandroidapp.data.states.GeneratedOrderState
import com.example.d5mandroidapp.ui.theme.PrimaryDarkJC
import com.example.d5mandroidapp.ui.viewmodels.GeneratedOrderViewModel
import com.example.d5mandroidapp.ui.views.orders.ShowProducts
import kotlin.math.round

@Composable
fun GeneratedOrderScreen(navController: NavController) {
    val context = LocalContext.current
    val generatedOrderViewModel: GeneratedOrderViewModel = hiltViewModel()
    val generatedOrderState by generatedOrderViewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 70.dp)
    ){
        Text(modifier = Modifier.padding(start = 4.dp), color = PrimaryDarkJC, text = "Ventas realizadas - ${generatedOrderState.visitDate}", fontFamily = FontFamily.SansSerif, fontSize = 14.sp)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp),
            thickness = 2.dp,
            color = Color.DarkGray
        )

        if(generatedOrderState.operations.isEmpty()){
            Text(modifier = Modifier.padding(start = 4.dp), color = PrimaryDarkJC, text = "No hay ventas por mostrar.", fontFamily = FontFamily.SansSerif)

        }else{

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "TOTAL GENERADOS:", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text(text = "S/ ${String.format("%.2f", generatedOrderViewModel.getTotalGeneratedSales())}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "TOTAL APROBADOS:", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text(text = "S/ ${String.format("%.2f", generatedOrderViewModel.getTotalApprovedSales())}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "TOTAL ANULADOS:", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text(text = "S/ ${String.format("%.2f", generatedOrderViewModel.getTotalCancelledSales())}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            ListOfGeneratedOrders(generatedOrderState)

        }
    }
}

@Composable
fun ListOfGeneratedOrders(state: GeneratedOrderState){
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        items(state.operations) {
            ItemOfGeneratedOrder(
                state = state,
                operation = it,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun ItemOfGeneratedOrder(
    state: GeneratedOrderState,
    operation: Operation,
    modifier: Modifier = Modifier,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Column (modifier= Modifier
                .weight(85f)
                .padding(8.dp)
            ){

                Text(
                    text = "Pedido Nro: ${operation.id}",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Cliente: ${operation.clientNames}",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Fecha: ${operation.operationDate}",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )

                Text(
                    text = "Monto: S/ ${String.format("%.2f", operation.totalSale + operation.baseAmountPerception)}",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Desc: ${operation.paymentTypeReadable}",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )

                Text(
                    text = "Estado: ${operation.operationStatusName}",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )

            }
        }

    }

}