package com.example.d5mandroidapp.ui.views.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.data.models.ProductTariff
import com.example.d5mandroidapp.data.states.SaleOrderState
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.viewmodels.OrderViewModel
import kotlin.math.round


@Composable
fun ProductTariffItem(
    state: SaleOrderState,
    productTariff: ProductTariff,
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel
) {

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
                .clickable {
                    viewModel.onClickProductTariffItem(productTariff, !productTariff.isSelected)
                }
                .background(if (productTariff.isSelected) GreenJC else Color.White)
                ,
//                .height(120.dp)
//                .border(BorderStroke(2.dp, SolidColor(Color.DarkGray)), shape = RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.CenterVertically,

        ) {


//            AsyncImage(
//                modifier = Modifier.size(70.dp).weight(20f),
//                model = "https://delasign.com/delasignBlack.png",
//                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
//                error = painterResource(id = R.drawable.ic_launcher_foreground),
//                contentDescription = "The delasign logo",
//            )
//            Image(
//                painter = painterResource(id = R.mipmap.ic_box),
//                contentDescription = "Login image",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .size(70.dp).weight(20f),
//
//            )
            Column (modifier= Modifier
                .weight(85f)
                .padding(8.dp)){
                Text(
                    text = "CODIGO: ${productTariff.productCode.toString().toInt()}",
                    color = Color.DarkGray,
                    modifier = Modifier.padding(all = 0.dp),
                    fontFamily = FontFamily(Font(R.font.noto_sans_bold)),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = productTariff.productName,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
                if (productTariff.subjectPerception){
                    Text(
                        text = "SUJETO A PERCEPCIÓN",
                        color =  Color.Blue,
                        fontSize = 11.sp,
                    )
                }
                if (productTariff.exemptFromIgv){
                    Text(
                        text = "EXONERADO DE IGV",
                        color = Color.Red,
                        fontSize = 11.sp,
                    )
                }
                Text(
                    text = "PRECIO: S/ ${if(state.selectedPaymentType=="CREDITO"){round(productTariff.creditSalePrice.toString().toDouble() * 100) / 100}else{round(productTariff.cashSalePrice.toString().toDouble() * 100) / 100}}",
                    color = Color.DarkGray,
                    modifier = Modifier.padding(all = 0.dp),
                    fontFamily = FontFamily(Font(R.font.noto_sans)),
                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold
                )
                Text(text = "STOCK ${productTariff.stock.toInt()}", fontFamily = FontFamily.SansSerif, fontSize = 14.sp)

            }
//            Checkbox(
//                modifier = Modifier.weight(15f),
//                colors = CheckboxDefaults.colors(
//                    checkedColor = Color.DarkGray,
//                    checkmarkColor = Color.White,
//                    uncheckedColor = Color.DarkGray,
//                ),
//                checked = productTariff.isSelected,
//                enabled = false,
//                onCheckedChange = {})

        }
    }


}