package com.example.d5mandroidapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.d5mandroidapp.ui.views.HomeScreen
import com.example.d5mandroidapp.ui.views.LoginScreen
import com.example.d5mandroidapp.ui.views.Profile
import com.example.d5mandroidapp.ui.views.SplashScreen
import com.example.d5mandroidapp.ui.views.TruckList
import com.example.d5mandroidapp.ui.views.clients.ClientScreen
import com.example.d5mandroidapp.ui.views.collections.DebtScreen
import com.example.d5mandroidapp.ui.views.orders.OrderScreen
import com.example.d5mandroidapp.ui.views.routes.RouteScreen

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Hosts(
    navigationController: NavHostController, onTopBarState: (topBarState: Boolean) -> Unit, onGesturesEnabled: (gesturesEnabled: Boolean) -> Unit
){
    NavHost(
        navController = navigationController,
        startDestination = "splash"
    ){
        composable(Screens.Splash.screen){
            onTopBarState(false)
            SplashScreen(navController = navigationController)
        }
        composable(Screens.Home.screen){
//            topBarState = true
            onTopBarState(true)
            HomeScreen()
        }
        composable(Screens.Profile.screen){
//            topBarState = true
            onTopBarState(true)
            Profile()
        }
        composable(Screens.Order.screen + "/{clientId}/{addressId}/{dailyRouteId}") { backStackEntry ->

//            topBarState = true
            onTopBarState(true)

            OrderScreen(
                navigationController,
                backStackEntry.arguments?.getString("clientId"),
                backStackEntry.arguments?.getString("addressId"),
                backStackEntry.arguments?.getString("dailyRouteId")
            )

        }
        composable(Screens.Debt.screen + "/{clientId}"){backStackEntry ->
            onTopBarState(true)
            DebtScreen(
                navigationController,
                backStackEntry.arguments?.getString("clientId")
            )
        }
        composable(Screens.Trucks.screen){
//            topBarState = true
            onTopBarState(true)
            TruckList()
        }
        composable(Screens.Route.screen){
//            topBarState = true
            onTopBarState(true)
//            onGesturesEnabled(false)
//            val viewModel = hiltViewModel<RouteViewModel>()

            RouteScreen(navigationController)
//            RouteScreen()

        }
        composable(Screens.Client.screen){
//            topBarState = true
            onTopBarState(true)
            ClientScreen(navigationController)
        }
        composable("login"){
//            topBarState = false
            onTopBarState(false)
//            onGesturesEnabled(false)
            LoginScreen {
                navigationController.navigate(Screens.Profile.screen)
            }
        }


    }

}
