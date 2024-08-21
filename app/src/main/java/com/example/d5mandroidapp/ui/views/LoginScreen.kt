package com.example.d5mandroidapp.ui.views
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.ImagePainter
import com.apollographql.apollo3.exception.ApolloException
import com.example.d5mandroidapp.LoginMutation
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.apollo.apolloClient
import com.example.d5mandroidapp.storage.TokenRepositoryImpl
import com.example.d5mandroidapp.storage.UserRepositoryImpl
import com.example.d5mandroidapp.ui.theme.D5MAndroidAppTheme
import com.example.d5mandroidapp.ui.theme.GreenJC
import com.example.d5mandroidapp.ui.theme.PrimaryDarkJC
import com.example.d5mandroidapp.ui.theme.Purple200
import com.example.d5mandroidapp.ui.theme.Purple500
import com.example.d5mandroidapp.ui.theme.Shapes
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navigateToProfile: () -> Unit){

    val context = LocalContext.current

    var email by remember { mutableStateOf("paola_vanessa@gmail.com") }
    var password by remember { mutableStateOf("123456") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    D5MAndroidAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .paint(
                    painterResource(id = R.drawable.bg21),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.TopCenter
                )  // Set background image
            ,
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
//            Image(
//                painter = painterResource(id = R.drawable.character_15),
//                contentDescription = "Login image",
//                modifier = Modifier.fillMaxWidth().padding(20.dp)
//            )
//            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "Bienvenido de nuevo", fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Ingresa a tu cuenta")

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = email, onValueChange = { email = it }, singleLine = true, label = {
                Text(text = "Correo electronico")
            })

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = password, onValueChange = { password = it }, singleLine = true, label = {
                Text(text = "Password")
            }, visualTransformation = PasswordVisualTransformation())

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = GreenJC, shape = RoundedCornerShape(10.dp))
                    .height(60.dp),
                enabled = !loading,
                onClick = {
                    loading = true
                    Log.i("Credential", "Email: $email Password: $password")
                    scope.launch {
                        val ok = login(context, email, password)
                        loading = false
                        if (ok) {
                            navigateToProfile()
                        }
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenJC
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 2.dp
                )

            ) {
                if (loading) {
                    Loading()
                } else {
                    Text(text = "Ingresar", color = Color.White, fontSize = 15.sp)
                }

            }

        }
    }
}

@Composable
fun Loading() {
    CircularProgressIndicator(modifier = Modifier.size(32.dp))
}

private suspend fun login(context: Context, email: String, password: String): Boolean {
    val response = try {
        apolloClient.mutation(LoginMutation(email = email, password = password)).execute()
    } catch (e: ApolloException) {
        Log.w("D5MAP2", "No se pudo conectar", e)
        Toast.makeText(context, "No se pudo conectar", Toast.LENGTH_SHORT).show()
        return false
    }
    if (response.hasErrors()) {
        Toast.makeText(context, "Error al iniciar sesión\n" +
                "${response.errors?.get(0)?.message}", Toast.LENGTH_SHORT).show()
        return false
    }
    val token = response.data?.tokenAuth?.token
    val refreshToken = response.data?.tokenAuth?.refreshToken

    if (token == null) {
        Toast.makeText(context, "No se pudo iniciar sesión: el backend no devolvió ningún token", Toast.LENGTH_SHORT).show()
        return false
    }

    val tokenRepository = TokenRepositoryImpl(context)
    tokenRepository.setToken(token)
    tokenRepository.setRefreshToken(refreshToken)

    val userRepository = UserRepositoryImpl(context)
    val user = response.data?.tokenAuth?.user
    if (user != null) {
        userRepository.setUserId(user.id)
        userRepository.setUserEmail(user.email)
    }
    return true
}