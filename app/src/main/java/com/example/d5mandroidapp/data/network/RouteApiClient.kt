package com.example.d5mandroidapp.data.network

import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.models.Gang
import com.example.d5mandroidapp.data.models.Zone
import com.example.d5mandroidapp.data.models.ZonePoint

interface RouteApiClient {
    suspend fun getZonePointsByZoneId(zoneId: Int): List<ZonePoint>
    suspend fun getAssignedZonesByUserId(userId: Int, visitDate: String): List<Zone>
    suspend fun getAssignedGangsByUserId(userId: Int, visitDate: String): List<Gang>
    suspend fun getZonesByGangId(gangId: Int): List<Zone>
    suspend fun getDailyRoutesByCriteria(userId: Int, gangId: Int, visitDate: String, searchQuery: String, searchBy: String): List<DailyRoute>
}