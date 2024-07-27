package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.Zone
import com.example.d5mandroidapp.data.network.RouteApiClient

class GetAssignedZonesByUserIdUseCase(
    private val routeApiClient: RouteApiClient
) {
    suspend fun execute(userId: Int, visitDate: String): List<Zone> {
        return routeApiClient.getAssignedZonesByUserId(userId, visitDate)
    }
}