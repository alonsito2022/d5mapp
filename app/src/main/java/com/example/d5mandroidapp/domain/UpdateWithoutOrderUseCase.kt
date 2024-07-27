package com.example.d5mandroidapp.domain

import android.util.Log
import com.example.d5mandroidapp.data.models.Order
import com.example.d5mandroidapp.data.models.WithoutOrder
import com.example.d5mandroidapp.data.network.OrderApiClient

class UpdateWithoutOrderUseCase(
    private val orderApiClient: OrderApiClient
) {
    suspend fun execute(withoutOrder: WithoutOrder): String? {
        return orderApiClient.updateWithoutOrder(withoutOrder)
    }
}