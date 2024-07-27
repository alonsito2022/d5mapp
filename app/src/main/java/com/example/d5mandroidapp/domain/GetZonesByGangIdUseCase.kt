package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.Zone
import com.example.d5mandroidapp.data.network.RouteApiClient

class GetZonesByGangIdUseCase(
    private val routeApiClient: RouteApiClient
) {
    suspend fun execute(gangId: Int): List<Zone>{
        return routeApiClient.getZonesByGangId(gangId).sortedBy { it.id }
    }
}