package com.example.d5mandroidapp.navigation

sealed class Screens (val screen: String){
    data object Login: Screens("login")
    data object Home: Screens("home")
    data object Profile: Screens("profile")
    data object Order: Screens("order")
    data object Debt: Screens("debt")
    data object Trucks: Screens("trucks")
    data object Client: Screens("client")
    data object Route: Screens("route")
    data object Splash: Screens("splash")
}