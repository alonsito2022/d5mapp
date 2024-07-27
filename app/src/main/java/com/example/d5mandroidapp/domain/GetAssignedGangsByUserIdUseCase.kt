package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.Gang
import com.example.d5mandroidapp.data.network.RouteApiClient

class GetAssignedGangsByUserIdUseCase(
    private val routeApiClient: RouteApiClient
) {
    suspend fun execute(userId: Int, visitDate: String): List<Gang>{
        return routeApiClient.getAssignedGangsByUserId(userId, visitDate).sortedBy { it.id }
    }
}