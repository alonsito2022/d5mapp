package com.example.d5mandroidapp.data.models

data class Zone(
    val id: Int = 0,
    val name: String = "",
    val code: String = "",
    val color: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val userId: Int = 0,
    val totalQuantityOfClients: Int = 0
) {
}