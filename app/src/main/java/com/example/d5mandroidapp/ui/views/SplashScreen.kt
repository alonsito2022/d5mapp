package com.example.d5mandroidapp.ui.views

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.navigation.Screens
import com.example.d5mandroidapp.storage.TokenRepositoryImpl
import com.example.d5mandroidapp.utils.JWTUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.Date

@Composable
fun SplashScreen(navController: NavHostController){
    val context = LocalContext.current.applicationContext
    val alpha = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            1f,
            animationSpec = tween(2500)
        )
        delay(1500)
        val tokenRepository = TokenRepositoryImpl(context)
        val token = runBlocking { tokenRepository.getToken() }

        Log.d("JWT_DECODED", "token: $token")
        if (token != null) {
            if (token.isNotEmpty()){
                val expirationTimestamp = JWTUtils.getExpirationTime(token)
                val expirationDate = Date(expirationTimestamp * 1000)
                val timeRemaining = expirationDate.time - System.currentTimeMillis()

                if (timeRemaining > 0) {
                    navController.popBackStack()
                    navController.navigate(Screens.Profile.screen)
                } else {
                    navController.popBackStack()
                    tokenRepository.clearToken()
                    navController.navigate(Screens.Login.screen)

                }
            }
            else{
                Log.d("JWT_DECODED", "token.isEmpty: $token")
//                navigationController.navigate(Screens.Login.screen)
            }
        }else{
            Log.d("JWT_DECODED", "token.is null: $token")
            navController.navigate(Screens.Login.screen)
//            navigationController.navigate(Screens.Login.screen)
        }



    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoaderAnimation(
            modifier = Modifier.size(400.dp), anim = R.raw.delivery_truck
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "¡Comencemos!",
            modifier = Modifier.alpha(alpha.value),
            fontSize = 30.sp,
            fontWeight = FontWeight.Light
        )
        Text(
            text = "Descubre lo que tenemos para ti.",
            modifier = Modifier.alpha(alpha.value),
            fontSize = 15.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun LoaderAnimation(modifier: Modifier, anim: Int){
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(anim))

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}