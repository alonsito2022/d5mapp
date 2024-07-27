package com.example.d5mandroidapp.ui.views.collections

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.d5mandroidapp.data.states.ClientState
import com.example.d5mandroidapp.data.states.SaleOrderState
import com.example.d5mandroidapp.navigation.Screens
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.viewmodels.ClientViewModel
import com.example.d5mandroidapp.ui.viewmodels.OrderViewModel
import com.example.d5mandroidapp.ui.views.orders.ProductTariffItem
import com.example.d5mandroidapp.ui.views.orders.ShowProducts

@Composable
fun DebtScreen(navController: NavController, clientId: String?){
    val clientViewModel: ClientViewModel = hiltViewModel()
    val clientState by clientViewModel.state.collectAsState()
    val context = LocalContext.current

    if (clientId != null) {
        clientViewModel.setClient(clientId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 70.dp)
    ){
        Text(text = "Cliente: ${clientState.selectedClient?.names}", fontFamily = FontFamily.SansSerif, fontSize = 15.sp)
        ShowOrdersWithDebt(clientState, clientViewModel)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            Text(text = "Total:", fontFamily = FontFamily.SansSerif, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = "S/ ${clientState.selectedClient?.debt}", fontFamily = FontFamily.SansSerif, fontSize = 15.sp, fontWeight = FontWeight.Bold)

        }
        Button(
            modifier = Modifier.padding(4.dp).fillMaxWidth(),
            onClick = {
                clientViewModel.sendOrdersToPay()
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenJC)
        ) {
            Text(text = "Pagar")
        }

        if (clientState.success){
            Toast.makeText(context, clientState.message, Toast.LENGTH_LONG).show()
            clientViewModel.setSuccessOrError()
            navController.navigate(Screens.Client.screen)
        }
        if (clientState.error){
            Toast.makeText(context, clientState.message, Toast.LENGTH_LONG).show()
            clientViewModel.setSuccessOrError()
        }
    }
}

@Composable
fun ShowOrdersWithDebt(state: ClientState, viewModel: ClientViewModel){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(state.ordersWithDebt) {
            OrderWithDebtItem(
                state = state,
                modifier = Modifier.fillMaxWidth(),
                orderWithDebt = it,
                viewModel = viewModel
            )
        }
    }
}