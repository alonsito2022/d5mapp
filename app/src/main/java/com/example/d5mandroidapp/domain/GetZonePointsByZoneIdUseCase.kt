package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.ZonePoint
import com.example.d5mandroidapp.data.network.RouteApiClient

class GetZonePointsByZoneIdUseCase(
    private val routeApiClient: RouteApiClient
) {
    suspend fun execute(zoneId: Int): List<ZonePoint> {
        return routeApiClient.getZonePointsByZoneId(zoneId)
    }
}