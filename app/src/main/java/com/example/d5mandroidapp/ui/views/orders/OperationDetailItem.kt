package com.example.d5mandroidapp.ui.views.orders

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithoutLabel
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.data.models.OperationDetail
import com.example.d5mandroidapp.data.states.SaleOrderState
import com.example.d5mandroidapp.ui.theme.ErrorJC
import com.example.d5mandroidapp.ui.theme.PrimaryLightJC
import com.example.d5mandroidapp.ui.theme.Purple200
import com.example.d5mandroidapp.ui.theme.Purple40
import com.example.d5mandroidapp.ui.theme.Teal200
import com.example.d5mandroidapp.ui.viewmodels.OrderViewModel
import kotlin.math.round

@Composable
fun OperationDetailItem(
    state: SaleOrderState,
    operationDetail: OperationDetail,
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel
){
//    var quantity by rememberSaveable { mutableStateOf(operationDetail.quantity.toString()) }
    var quantity by remember { mutableStateOf(TextFieldValue(operationDetail.quantity.toString())) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val context = LocalContext.current
    LaunchedEffect(isFocused) {
        val endRange = if (isFocused) quantity.text.length else 0
        quantity = quantity.copy(
            selection = TextRange(
                start = 0, end = endRange
            )
        )
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {


        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(if (operationDetail.productActiveType == "01") PrimaryLightJC else Purple200)
            ,
//                .height(120.dp)
//                .border(BorderStroke(2.dp, SolidColor(Color.DarkGray)),
//                shape = RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween

            ) {

            Column (modifier= Modifier
//                    .fillMaxWidth()
                .weight(95f)
                .padding(8.dp)
            ){
                Text(
                    text = "COD: ${(operationDetail.productCode).toInt()}",
                    color =  if (operationDetail.productActiveType=="01") Color.DarkGray else Color.White,
                    fontSize = 11.sp,
                )
                Text(
                    text = operationDetail.productName,
                    color =  if (operationDetail.productActiveType=="01") Color.DarkGray else Color.White,
                )
                if (operationDetail.productSubjectPerception){
                    Text(
                        text = "SUJETO A PERCEPCIÓN",
                        color =  Color.Blue,
                        fontSize = 11.sp,
                    )
                }
                if (operationDetail.productExemptFromIgv){
                    Text(
                        text = "EXONERADO DE IGV",
                        color =  Color.Red,
                        fontSize = 11.sp,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "PRECIO: S/ ${if(state.selectedPaymentType=="CREDITO"){round(operationDetail.creditPrice * 100) / 100}else{round(operationDetail.cashPrice * 100) / 100}}",
                        color = if (operationDetail.productActiveType=="01") Color.DarkGray else Color.White,
                        fontSize = 12.sp,
                    )

                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = "DTO:  ${round(operationDetail.percentageDiscount * 100) / 100}%",
                        color = if (operationDetail.productActiveType=="01") Color.DarkGray else Color.White,
                        fontSize = 10.sp,
                    )
                }

                Text(
                    text = "SUBTOTAL: S/ ${if(state.selectedPaymentType=="CREDITO"){round(operationDetail.creditSubtotal * 100) / 100}else{round(operationDetail.cashSubtotal * 100) / 100}}",
                    color = if (operationDetail.productActiveType=="01") Color.DarkGray else Color.White,
                    fontSize = 12.sp,
                )
                if (operationDetail.productActiveType=="01"){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            modifier = Modifier.size(40.dp),
                            onClick = {
                                viewModel.onClickDecreaseOperationDetailQuantity(operationDetail)
                                quantity = TextFieldValue(operationDetail.quantity.toString())
                            }) {
                            Image(
                                painter = painterResource(R.drawable.ic_remove_circle_24),
                                contentDescription = "Minus Icon",
                                colorFilter = ColorFilter.tint(Color.DarkGray)
                            )
                        }

                        BasicTextField(
                            value = quantity,
                            onValueChange = {newText ->
                                if (newText.text.all { it.isDigit() }){
                                    quantity = newText
                                    if (quantity.text.isNotEmpty()) {
                                        if (quantity.text.toDouble() > operationDetail.stock) {
                                            Toast.makeText(context, "Excedio el stock", Toast.LENGTH_SHORT ).show()
                                            quantity = TextFieldValue("")
                                        }
                                        viewModel.onTextChangeOperationDetailQuantity(newText.text, operationDetail)
                                    }
                                }else{
                                    Toast.makeText(context, "Invalid input: Please enter a number", Toast.LENGTH_SHORT ).show()
                                }

                            },
                            interactionSource = interactionSource,
                            modifier = Modifier
                                .width(100.dp)
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(5.dp)
                                )
                            ,

                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,

                                ),
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

//                            readOnly = true

                        )

                        IconButton(
                            modifier = Modifier.size(40.dp),
                            onClick = {
                                viewModel.onClickIncreaseOperationDetailQuantity(operationDetail)
                                quantity = TextFieldValue(operationDetail.quantity.toString())
                            }) {
                            Image(
                                painter = painterResource(R.drawable.ic_add_circle_24),
                                contentDescription = "Plus Icon",
                                colorFilter = ColorFilter.tint(Color.DarkGray)
                            )
                        }
                    }
                }
                else{
                    Text(
                        text = "CANTIDAD: ${operationDetail.quantity}",
                        color = if (operationDetail.productActiveType=="01") Color.DarkGray else Color.White,
                        modifier = Modifier.padding(all = 0.dp),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.SansSerif
//                    fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "BONO ID: ${operationDetail.bonusId}",
                        color = if (operationDetail.productActiveType=="01") Color.DarkGray else Color.White,
                        modifier = Modifier.padding(all = 0.dp),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold
                    )
                }


            }


            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Teal200)
//                        .weight(5f)
                    .size(30.dp),

                onClick = {
                    viewModel.deleteOperationDetailItem(operationDetail.productTariffId)
                }) {

                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Delete Icon",
                    tint = Color.White
                )
            }

        }
    }






}