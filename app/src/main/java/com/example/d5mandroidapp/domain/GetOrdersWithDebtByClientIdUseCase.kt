package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.OrderWithDebt
import com.example.d5mandroidapp.data.network.ClientApiClient

class GetOrdersWithDebtByClientIdUseCase(
    private val clientApiClient: ClientApiClient
) {
    suspend fun execute(id: Int): List<OrderWithDebt>{
        return clientApiClient.getOrderWithDebtByClientId(id).sortedBy { it.id }
    }
}