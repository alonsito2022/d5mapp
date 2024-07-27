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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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