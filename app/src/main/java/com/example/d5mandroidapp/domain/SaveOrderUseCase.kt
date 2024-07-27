package com.example.d5mandroidapp.domain

import android.util.Log
import com.example.d5mandroidapp.data.models.Order
import com.example.d5mandroidapp.data.network.OrderApiClient

class SaveOrderUseCase(
    private val orderApiClient: OrderApiClient
) {
    suspend fun execute(order: Order): String? {
        Log.d("D5MAP2","order execute: $order")
        return orderApiClient.saveOrder(order)
    }
}