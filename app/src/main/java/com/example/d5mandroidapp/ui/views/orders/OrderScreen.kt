package com.example.d5mandroidapp.ui.views.orders

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.d5mandroidapp.ui.viewmodels.OrderViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.states.SaleOrderState
import com.example.d5mandroidapp.navigation.Screens
import com.example.d5mandroidapp.storage.TokenRepositoryImpl
import com.example.d5mandroidapp.storage.UserRepositoryImpl
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.theme.PrimaryDarkJC
import com.example.d5mandroidapp.ui.theme.PrimaryLightJC
import com.example.d5mandroidapp.ui.theme.Purple200
import com.example.d5mandroidapp.ui.theme.Purple500
import com.example.d5mandroidapp.ui.theme.Purple700
import com.example.d5mandroidapp.ui.theme.Teal700
import com.example.d5mandroidapp.ui.viewmodels.ClientViewModel
import com.example.d5mandroidapp.ui.viewmodels.WithoutOrderViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderScreen(navController: NavController, clientId: String?, addressId: String?, dailyRouteId: String?){
    val context = LocalContext.current
    val orderViewModel: OrderViewModel = hiltViewModel()
    val clientViewModel: ClientViewModel = hiltViewModel()

    val clientState by clientViewModel.state.collectAsState()
    val orderState by orderViewModel.state.collectAsState()

    val searchText by orderViewModel.searchText.collectAsState()
    val searchBy by orderViewModel.searchBy.collectAsState()
    var addressesList: List<String> = listOf()
    var addressesIdsList: List<Int> = listOf()
    val userRepository = UserRepositoryImpl(context)


    if (clientId != null) {

        Log.d("D5MAP","clientId: $clientId")
        userRepository.getUserId()?.let {
            orderViewModel.setUser(it)
        }
        orderViewModel.setClient(clientId)
        clientViewModel.setClient(clientId)

        if (dailyRouteId != null) {
            val selectedClient = clientState.selectedClient
            if (selectedClient != null && selectedClient.typeTradeId > 0) {
                orderViewModel.setDailyRouteIdAndTypeTradeId(dailyRouteId, selectedClient.typeTradeId)
            }
        }

        if (clientState.addresses.isNotEmpty()){
            val searchAddress = clientState.addresses.find { it.id == addressId?.toInt() }
            if (searchAddress != null) {
                orderViewModel.setAddress(searchAddress.id, searchAddress.address)
            }
        }

    } else {

        Log.d("D5MAP","clientId nullable")
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                ,

                shape = CircleShape,
                containerColor = Purple200,
                contentColor = Color.White,
                onClick = {
                    orderViewModel.showProductTariffDialog()
                    if (clientId != null) {
                        clientViewModel.searchAddresses(clientId)
                    }
                },
            ){
//                Icon(modifier = Modifier
//                    .size(50.dp)
//                    .padding(top = 10.dp), imageVector = Icons.Filled.ShoppingCart, contentDescription = null)
                Text(color = Color.White, text = orderState.operationDetails.size.toString(), fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif, style = MaterialTheme.typography.bodyLarge)

            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 70.dp)
        ){

            Text(text = "CLIENTE: ${clientState.selectedClient?.names}", color = PrimaryDarkJC, fontSize = 15.sp)
            Text(text = "${clientState.selectedClient?.documentTypeReadable}: ${clientState.selectedClient?.documentNumber}", color = PrimaryDarkJC, fontSize = 14.sp)
            Text(text = "LINEA DE CREDITO: ${clientState.selectedClient?.creditLine}", color = PrimaryDarkJC, fontSize = 14.sp)
            Text(text = "LISTA DE PRECIOS: ${clientState.selectedClient?.typeTradeName}", color = PrimaryDarkJC, fontSize = 14.sp)

            Text(text = "Selección de productos", fontWeight = FontWeight.Bold)
            SearchProductTariff(
                searchText = searchText,
                searchBy = searchBy,
                onTextChangeTextField = orderViewModel::onSearchTextChange,
                cleanTextField = orderViewModel::onSearchTextChange,
                updateSearchBy = orderViewModel::updateSearchBy,
                onClickButtonSearchProduct = orderViewModel::onClickButtonSearch
            )
            Text(modifier = Modifier.padding(start = 4.dp), color = PrimaryDarkJC, text = "Ingreso minimo 3 caracteres.", fontFamily = FontFamily.SansSerif, style = MaterialTheme.typography.labelSmall)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = 2.dp,
                color = Color.DarkGray
            )

            if (orderState.isLoading) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            } else {
                ShowProducts(orderState, orderViewModel)
                if(orderState.isOpenDialog){
                    OrderDialog(
                        orderState = orderState,
                        clientState = clientState,
                        viewModel = orderViewModel,
                        onDismiss = orderViewModel::hideProductTariffDialog
                    )
                }
            }

            if (orderState.success){
                Toast.makeText(context, orderState.message, Toast.LENGTH_LONG).show()
                orderViewModel.setSuccessOrError()
                navController.navigate(Screens.Client.screen)
            }
            if (orderState.error){
                Toast.makeText(context, orderState.message, Toast.LENGTH_LONG).show()
                orderViewModel.setSuccessOrError()
            }

        }


    }

}


@Composable
fun ShowProducts(state: SaleOrderState, viewModel: OrderViewModel){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(state.productTariffs) {
            ProductTariffItem(
                state = state,
                productTariff = it,
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel
            )
        }
    }
}