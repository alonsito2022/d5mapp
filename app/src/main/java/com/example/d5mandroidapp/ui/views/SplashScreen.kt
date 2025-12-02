package com.example.d5mandroidapp.ui.views

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.data.states.AuthState
import com.example.d5mandroidapp.navigation.Screens
import com.example.d5mandroidapp.ui.theme.D5MAndroidAppTheme
import com.example.d5mandroidapp.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val alpha = remember {
        Animatable(0f)
    }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            1f,
            animationSpec = tween(2500)
        )
        delay(1500)
        // Verificar estado de autenticación
        viewModel.checkAuthenticationStatus()
    }

    // Navegar según el estado de autenticación
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                navController.popBackStack()
                if (state.isAuthenticated) {
                    navController.navigate(Screens.Profile.screen) {
                        // Limpiar el back stack para evitar volver al splash
                        popUpTo(Screens.Splash.screen) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screens.Login.screen) {
                        popUpTo(Screens.Splash.screen) { inclusive = true }
                    }
                }
            }
            is AuthState.Error -> {
                // Si hay error (token expirado o inválido), ir a login
                navController.popBackStack()
                navController.navigate(Screens.Login.screen) {
                    popUpTo(Screens.Splash.screen) { inclusive = true }
                }
            }
            else -> {
                // Loading o Idle - esperar
            }
        }
    }

    D5MAndroidAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoaderAnimation(
                modifier = Modifier.size(400.dp),
                anim = R.raw.delivery_truck
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "¡Comencemos!",
                modifier = Modifier.alpha(alpha.value),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Descubre lo que tenemos para ti.",
                modifier = Modifier.alpha(alpha.value),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LoaderAnimation(modifier: Modifier, anim: Int) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(anim))

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}
