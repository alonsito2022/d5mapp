package com.example.d5mandroidapp.ui.views.clients

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.ContentAlpha
import com.example.d5mandroidapp.data.states.ClientState
import com.example.d5mandroidapp.data.states.SaleOrderState
import com.example.d5mandroidapp.data.states.UserState
import com.example.d5mandroidapp.ui.theme.PrimaryDarkJC
import com.example.d5mandroidapp.ui.theme.Purple40
import com.example.d5mandroidapp.ui.theme.Purple80
import com.example.d5mandroidapp.ui.viewmodels.ClientViewModel
import com.example.d5mandroidapp.ui.viewmodels.OrderViewModel
import com.example.d5mandroidapp.ui.viewmodels.UserViewModel
import com.example.d5mandroidapp.ui.viewmodels.WithoutOrderViewModel
import com.example.d5mandroidapp.ui.views.clients.ClientItem
import com.example.d5mandroidapp.ui.views.components.CustomDatePicker
import com.example.d5mandroidapp.ui.views.components.CustomRadioGroup
import com.example.d5mandroidapp.ui.views.orders.ProductTariffItem
import com.example.d5mandroidapp.ui.views.orders.SearchProductTariff
import com.example.d5mandroidapp.ui.views.orders.ShowProducts
import com.example.d5mandroidapp.ui.views.routes.ConfirmDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClientScreen(navController: NavController) {

    val userViewModel: UserViewModel = hiltViewModel()
    val userState by userViewModel.state.collectAsState()

    val withoutOrderViewModel: WithoutOrderViewModel = hiltViewModel()
    val withoutOrderState by withoutOrderViewModel.state.collectAsState()

    val searchClientText by userViewModel.searchClientText.collectAsState()
    val searchClientBy by userViewModel.searchClientBy.collectAsState()
    val context = LocalContext.current

    Column( modifier = Modifier.padding(top = 70.dp)){
        if (userState.showSearchPanel){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White)

            ){
                Text(text = "Fecha: ", fontWeight = FontWeight.Bold, color = Color.Gray)
                CustomDatePicker(
                    userState.visitDate,
                    userViewModel::setSelectedVisitDate
                )

                Text(text = "Grupo o cuadrilla: ", fontWeight = FontWeight.Bold, color = Color.Gray)
                CustomRadioGroup(
                    namesList = userState.gangs.map { gang -> "${gang.name} - ${gang.truckLicensePlate}" },
                    idsList = userState.gangs.map { gang -> gang.id },
                    setSelectedItem = userViewModel::setSelectedGang)

                Text(text = "Zona: ", fontWeight = FontWeight.Bold, color = Color.Gray)
                CustomRadioGroup(
                    namesList = userState.zones.map { zone -> "${zone.code} - ${zone.totalQuantityOfClients} CLIENTES" },
                    idsList = userState.zones.map { zone -> zone.id },
                    setSelectedItem = userViewModel::setSelectedZoneCenter
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
        ) {


            Text(text = "Buscar cliente", fontWeight = FontWeight.Bold, color = Color.Gray)

            SearchClient(
                searchText = searchClientText,
                searchBy = searchClientBy,
                onTextChangeTextField = userViewModel::onSearchTextChange,
                cleanTextField = userViewModel::onSearchTextChange,
                updateSearchBy = userViewModel::updateSearchBy,
                onClickButtonSearchClient = userViewModel::onClickButtonSearch,
                onClickButtonToggleSearchPanel = userViewModel::onClickButtonToggleSearchPanel,
                userState= userState
            )

            Text(modifier = Modifier.padding(start = 4.dp), color = PrimaryDarkJC, text = "Ingreso minimo 3 caracteres.", style = MaterialTheme.typography.labelSmall)
            Text(modifier = Modifier.padding(start = 4.dp), color = PrimaryDarkJC, text = "Se encontraron ${userState.filteredDailyRoutes.size} registros", style = MaterialTheme.typography.labelSmall)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = 2.dp,
                color = Color.DarkGray
            )

            if (userState.isLoading) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            } else {
                if (userState.filteredDailyRoutes.isNotEmpty()){
                    ShowClients(userState, withoutOrderViewModel, navController)
                }else{
                    Text(text = "No hay registros")
                }

            }
        }
    }

    if (withoutOrderState.isOpenConfirmDialog) {
        ConfirmDialog(
            withoutOrderState = withoutOrderState,
            onDismiss = withoutOrderViewModel::hideConfirmDialog,
            onTextChangeTextField = withoutOrderViewModel::onObservationTextChange,
            onUpdateButtonClicked = withoutOrderViewModel::saveWithoutOrder,
            onChangeSelectReason = withoutOrderViewModel::onChangeSelectReason,
        )
    }
    if (withoutOrderState.success){
        Toast.makeText(context, withoutOrderState.message, Toast.LENGTH_LONG).show()
        userViewModel.assignedGangsAndZones()
        withoutOrderViewModel.setSuccessOrError()
    }
    if (withoutOrderState.error){
        Toast.makeText(context, withoutOrderState.message, Toast.LENGTH_LONG).show()
        withoutOrderViewModel.setSuccessOrError()
    }

}


@Composable
fun ShowClients(userState: UserState, withoutOrderViewModel: WithoutOrderViewModel, navController: NavController){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(userState.filteredDailyRoutes) {
            ClientItem(
                dailyRoute = it,
                modifier = Modifier.fillMaxWidth(),
                withoutOrderViewModel = withoutOrderViewModel,
                navController = navController
            )
        }
    }
}