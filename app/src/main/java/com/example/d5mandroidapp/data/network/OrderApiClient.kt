package com.example.d5mandroidapp.data.network
import com.example.d5mandroidapp.data.models.Message
import com.example.d5mandroidapp.data.models.Order
import com.example.d5mandroidapp.data.models.WithoutOrder

interface OrderApiClient {
    suspend fun saveOrder(order: Order): String?
    suspend fun updateWithoutOrder(withoutOrder: WithoutOrder): String?
}