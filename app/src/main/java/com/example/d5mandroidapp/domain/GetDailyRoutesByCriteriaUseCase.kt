package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.network.RouteApiClient

class GetDailyRoutesByCriteriaUseCase(
    private val routeApiClient: RouteApiClient
) {
    suspend fun execute(userId: Int, gangId: Int, visitDate: String): List<DailyRoute>{
        return routeApiClient.getDailyRoutesByCriteria(userId, gangId, visitDate).sortedBy { it.routeDailyRouteId }
    }
}