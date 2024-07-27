package com.example.d5mandroidapp.ui.views.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    displayDate: String,
    setSelectedDate: (selectedDate: String) -> Unit
){
    val state = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }
    val year: Int
    val month: Int
    val day: Int
    val mCalendar: Calendar = Calendar.getInstance()
    year = mCalendar.get(Calendar.YEAR)
    month = mCalendar.get(Calendar.MONTH)
    day = mCalendar.get(Calendar.DAY_OF_MONTH)
//    var displayDate: String by rememberSaveable { mutableStateOf("${customFormatted(day)}/${customFormatted(month+1)}/${year}") }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(
                BorderStroke(1.dp, SolidColor(Color.Gray)),
                shape = RoundedCornerShape(4.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if(showDialog){
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        val date = state.selectedDateMillis
                        date?.let{
                            val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
//                            displayDate = "${customFormatted(localDate.dayOfMonth)}/${customFormatted(localDate.monthValue)}/${localDate.year}"
                            setSelectedDate("${localDate.year}-${customFormatted(localDate.monthValue)}-${customFormatted(localDate.dayOfMonth)}")
                        }
                    }) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDialog = false }) {
                        Text(text = "CERRAR")
                    }
                }
            ) {
                DatePicker(state = state)
            }
        }
        Text(
            modifier = Modifier
                .clickable {
                    showDialog = true
                }
                .weight(80f)
                .padding(horizontal = 40.dp),
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            text = AnnotatedString(customDisplayDate(displayDate)),
        )

        Icon(imageVector = Icons.Filled.DateRange,
            contentDescription = null,
            modifier = Modifier
                .clickable { showDialog = true }
                .size(60.dp)
                .weight(20f)
        )
    }
}

fun customFormatted(value: Int): String{
    return if(value<10) "0${value}" else "$value"
}

fun customDisplayDate(inputDateString: String): String {
    val outputDateFormat = SimpleDateFormat("dd/MM/yyyy")
    val inputDate = SimpleDateFormat("yyyy-MM-dd").parse(inputDateString)
    return outputDateFormat.format(inputDate)

}
