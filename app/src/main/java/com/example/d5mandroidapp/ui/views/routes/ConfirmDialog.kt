package com.example.d5mandroidapp.ui.views.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.d5mandroidapp.data.states.RouteState
import com.example.d5mandroidapp.data.states.WithoutOrderState
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.viewmodels.RouteViewModel
import com.example.d5mandroidapp.ui.viewmodels.WithoutOrderViewModel
import com.example.d5mandroidapp.ui.views.orders.DialogSpinner
import kotlin.reflect.KFunction1

@Composable
fun ConfirmDialog(
    withoutOrderState: WithoutOrderState,
    onDismiss: () -> Unit,
    onTextChangeTextField: KFunction1<String, Unit>,
    onUpdateButtonClicked: () -> Unit,
    onChangeSelectReason: KFunction1<Boolean, Unit>
){
    var checked by rememberSaveable { mutableStateOf(withoutOrderState.selectReason) }

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
            ) {
                item {
                    Text(text = "MOTIVOS DE VISITA SIN PEDIDO", fontFamily = FontFamily.SansSerif, fontSize = 15.sp)
                    if(!withoutOrderState.selectReason){
                        DialogSpinner(
                            itemList = withoutOrderState.reasonList, selectedItem = withoutOrderState.observation,
                            onItemSelected = onTextChangeTextField,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .weight(7f)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.Absolute.Left,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                                onChangeSelectReason(it)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.DarkGray,
                                checkmarkColor = Color.White,
                                uncheckedColor = Color.DarkGray
                            ),
                        )
                        Text(text = "Otro motivo")
                    }
                    if(withoutOrderState.selectReason){
                        TextField(
                            value = withoutOrderState.observation,
                            onValueChange = onTextChangeTextField, modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(10.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    }



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
                                onUpdateButtonClicked()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }

    }
}