package com.example.d5mandroidapp.ui.views.collections

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.data.models.OrderWithDebt
import com.example.d5mandroidapp.data.models.ProductTariff
import com.example.d5mandroidapp.data.states.ClientState
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.viewmodels.ClientViewModel

@Composable
fun OrderWithDebtItem(
    state: ClientState,
    modifier: Modifier = Modifier,
    orderWithDebt: OrderWithDebt,
    viewModel: ClientViewModel
){
    var totalToPaid by remember { mutableStateOf(TextFieldValue(orderWithDebt.totalToPaid.toString())) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val context = LocalContext.current
    var checked by rememberSaveable { mutableStateOf(orderWithDebt.isSelected) }
    LaunchedEffect(isFocused) {
        val endRange = if (isFocused) totalToPaid.text.length else 0
        totalToPaid = totalToPaid.copy(
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
                .background(Color.White)
            ,
//                .height(120.dp)
//                .border(BorderStroke(2.dp, SolidColor(Color.DarkGray)), shape = RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Column (modifier= Modifier
                .padding(8.dp)
            ){
                Text(
                    text = orderWithDebt.documentNumber,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(all = 0.dp),
                    fontFamily = FontFamily(Font(R.font.noto_sans_bold)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

            }

            Column (modifier= Modifier
                .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Text(
                    text = "S/ ${String.format("%.2f", orderWithDebt.totalPending)}",
                    color = Color.DarkGray,
                    modifier = Modifier.padding(all = 0.dp),
                    fontFamily = FontFamily(Font(R.font.noto_sans_bold)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                BasicTextField(
                    value = totalToPaid,
                    onValueChange = {
                        totalToPaid = it
                        if (totalToPaid.text.isNotEmpty()) {
                            if (totalToPaid.text.toDouble() > orderWithDebt.totalPending) {
                                Toast.makeText(context, "Excedio la deuda", Toast.LENGTH_SHORT ).show()
                                totalToPaid = TextFieldValue("")
                            }
                            viewModel.onTextChangeOrderWithDebtTotalToPaid(it.text, orderWithDebt)
                        }

                    },
                    modifier = Modifier
                        .width(100.dp)
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(5.dp)
                        ),
                    interactionSource = interactionSource,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                        ),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

//                            readOnly = true

                )
            }
            Column (modifier= Modifier
                .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Checkbox(
                    modifier = Modifier.weight(18f),
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        viewModel.onClickOrderWithDebtItem(orderWithDebt, it)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.DarkGray,
                        checkmarkColor = Color.White,
                        uncheckedColor = Color.DarkGray
                    ),
                )
            }
        }

    }
}
