package com.example.d5mandroidapp.ui.views
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.ui.theme.D5MAndroidAppTheme
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.theme.Purple80
import com.example.d5mandroidapp.ui.viewmodels.ClientViewModel
import com.example.d5mandroidapp.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Profile(){
    val viewModel: ProfileViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    D5MAndroidAppTheme {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Box (modifier = Modifier.fillMaxSize()){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(top = 40.dp, start = 8.dp, end = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Image(
                        painter = painterResource(id = R.drawable.bg10),
                        contentDescription = "Login image",
                        modifier = Modifier.fillMaxWidth()

                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "¡Bienvenido(a) ${state.selectedUser.firstName}!",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Nos alegra que estés aquí.",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )


                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "Tu Perfil", fontSize = 30.sp, fontWeight = FontWeight.Bold,
                        color = Color.Black, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(modifier = Modifier.fillMaxWidth(), text = "CORREO: ${state.selectedUser.username}", fontSize = 15.sp, fontWeight = FontWeight.Normal,
                        color = Color.Black, overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(modifier = Modifier.fillMaxWidth(), text = "CARGO: ${state.selectedUser.roleReadable}", fontSize = 15.sp, fontWeight = FontWeight.Light,
                        color = Color.Black, overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    if (state.selectedUser.phone.isNotEmpty() && state.selectedUser.phone.lowercase() != "null"){
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(modifier = Modifier.fillMaxWidth(), text = "CELULAR: ${state.selectedUser.phone}", fontSize = 15.sp, fontWeight = FontWeight.Light,
                            color = Color.Black, overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(modifier = Modifier.fillMaxWidth(), text = "DNI: ${state.selectedUser.document}", fontSize = 15.sp, fontWeight = FontWeight.Light,
                        color = Color.Black, overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    if (state.selectedUser.address.isNotEmpty() && state.selectedUser.address.lowercase() != "null"){
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(modifier = Modifier.fillMaxWidth(), text = "DIRECCION: ${state.selectedUser.address}", fontSize = 15.sp, fontWeight = FontWeight.Light,
                            color = Color.Black, overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }

//                    CustomDatePicker()



                }
            }
        }



    }
}
