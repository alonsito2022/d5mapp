package com.example.d5mandroidapp.ui.views.orders
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

import com.example.d5mandroidapp.data.states.SaleOrderState
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.viewmodels.OrderViewModel

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.data.states.ClientState
import com.example.d5mandroidapp.ui.theme.PrimaryDarkJC
import com.example.d5mandroidapp.ui.theme.PrimaryLightJC
import com.example.d5mandroidapp.ui.theme.Teal200
import com.example.d5mandroidapp.ui.theme.Teal700

@Composable
fun OrderDialog(
    orderState: SaleOrderState,
    clientState: ClientState,
    viewModel: OrderViewModel,
    onDismiss: () -> Unit
){
    Dialog(
        onDismissRequest = onDismiss
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

            ){
                item {
                    Text(text = "NUEVO PEDIDO", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    HorizontalDivider()
                    Text(text = "CLIENTE: ${clientState.selectedClient?.names!!}", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                    Text(text = "LISTA DE PRECIO: ${clientState.selectedClient.typeTradeName}", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                    Text(text = "CALIFICACIÓN: ${if (clientState.selectedClient.isObserved) {"OBSERVADO"}else if (clientState.selectedClient.isSuspended) {"SUSPENDIDO DE CREDITO"}else if (clientState.selectedClient.isBlocked) {"BLOQUEADO DE VENTA"}else {"APTO"}}", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)

                    Text(text = "DEUDA PENDIENTE: S/ ${clientState.selectedClient.debt}", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                    Text(text = "DIRECCION: ${orderState.addressName}", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        val paymentTypes = if (clientState.selectedClient.isSuspended) {
                            listOf("CONTADO")
                        } else {
                            listOf("CONTADO", "CREDITO")
                        }
                        Text(text = "COND. VENTA:", fontWeight = FontWeight.Bold, modifier = Modifier.weight(3f), color = Color.Gray, fontSize = 12.sp, lineHeight = 1.2.em,)

                        DialogSpinner(
                            itemList = paymentTypes, selectedItem = orderState.selectedPaymentType,
                            onItemSelected = viewModel::onSelectedPaymentType,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .weight(7f)
                        )

                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

                        val documentTypes = if (clientState.selectedClient.documentTypeReadable == "RUC") {
                            listOf("FACTURA")
                        } else {
                            listOf("BOLETA")
                        }
                        val selectedDocumentType = if (clientState.selectedClient.documentTypeReadable == "RUC") {
                            "FACTURA"
                        } else {
                            "BOLETA"
                        }
                        viewModel.onSelectedDocumentType(selectedDocumentType)

                        Text(text = "DOC A EMITIR:", fontWeight = FontWeight.Bold, modifier = Modifier.weight(3f), color = Color.Gray, fontSize = 12.sp, lineHeight = 1.2.em,)
                        DialogSpinner(
                            itemList = documentTypes, selectedItem = orderState.selectedDocumentType,
                            onItemSelected = viewModel::onSelectedDocumentType,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .weight(7f)
                        )

                    }


                    LazyColumn(
                        modifier = Modifier.height(350.dp)
                    ) {
                        items(orderState.operationDetails) {
                            OperationDetailItem(
                                state = orderState,
                                operationDetail = it, modifier = Modifier.fillMaxWidth(),
                                viewModel = viewModel
                            )
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "DESCUENTO:", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Text(text = "S/ ${orderState.discountCost}", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "EXONERADA:", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Text(text = "S/ ${orderState.exoneratedCost}", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "GRAVADA:", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Text(text = "S/ ${orderState.baseCost}", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "IGV:", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Text(text = "S/ ${orderState.igvCost}", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "TOTAL:", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Text(text = "S/ ${String.format("%.2f", orderState.totalSale)}", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "GRATUITA:", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Text(text = "S/ ${orderState.freeCost}", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "PERCEPCIÓN(2%):", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Text(text = "S/ ${orderState.perceptionCost}", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "TOTAL A COBRAR:", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Text(text = "S/ ${orderState.totalToPay}", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp),
                            onClick = { onDismiss() },
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(Color.Gray)
                        ) {
                            Text(text = "Cerrar")
                        }
                        if (!orderState.isLoading && orderState.operationDetails.isNotEmpty()) {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp),
                                onClick = {
                                    onDismiss()
                                    viewModel.saveOrder()

                                },
                                enabled = !clientState.selectedClient.isBlocked,
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
                            ) {
                                Text(text = "Guardar")
                            }
                        }
                    }

                }



            }
        }
    }
}


@Composable
fun DialogSpinner(
    modifier: Modifier,
    itemList: List<String>,
    selectedItem: String,
    onItemSelected: (selectedItem: String) -> Unit
){
    var tempSelectedItem = selectedItem
    if(tempSelectedItem.isBlank() && itemList.isNotEmpty()){
        onItemSelected(itemList[0])
        tempSelectedItem = itemList[0]
    }
    var expanded by rememberSaveable() { mutableStateOf(false) }

    OutlinedButton(
        onClick = { expanded = true },
        modifier = modifier,
        enabled = itemList.isNotEmpty(),
        shape = RoundedCornerShape(5.dp)
    ) {
        Text(
            text = tempSelectedItem,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .weight(90f)
                .padding(horizontal = 4.dp, vertical = 4.dp)

        )
        Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(30.dp), tint = Color.Black)

        DropdownMenu(
            modifier = Modifier
                .padding(horizontal = 4.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            itemList.forEach{

                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        Text(text = it, style = MaterialTheme.typography.bodyMedium)
                    },
                    onClick = {
                        expanded = false
                        onItemSelected(it)
                    }
                )
                HorizontalDivider()

            }
        }
    }
}

