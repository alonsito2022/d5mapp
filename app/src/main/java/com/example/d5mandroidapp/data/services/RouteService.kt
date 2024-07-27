package com.example.d5mandroidapp.data.services

import com.apollographql.apollo3.ApolloClient
import com.example.d5mandroidapp.AssignedGangListQuery
import com.example.d5mandroidapp.AssignedZoneListByUserQuery
import com.example.d5mandroidapp.DailyRouteFilteredListQuery
import com.example.d5mandroidapp.PointListByZoneQuery
import com.example.d5mandroidapp.ZoneListByGangQuery
import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.models.Gang
import com.example.d5mandroidapp.data.models.Zone
import com.example.d5mandroidapp.data.models.ZonePoint
import com.example.d5mandroidapp.data.network.RouteApiClient

class RouteService(private val apolloClient: ApolloClient): RouteApiClient {
    override suspend fun getZonePointsByZoneId(zoneId: Int): List<ZonePoint> {
        return apolloClient
            .query(PointListByZoneQuery(zoneId))
            .execute()
            .data
            ?.pointsByZoneCenterId
            ?.map { it!!.toZonePoint() }
            ?: emptyList()
    }

    override suspend fun getAssignedZonesByUserId(userId: Int, visitDate: String): List<Zone> {
        return apolloClient
            .query(AssignedZoneListByUserQuery(userId, visitDate))
            .execute()
            .data
            ?.assignedZonesByUserId
            ?.map { it!!.toZone() }
            ?: emptyList()
    }

    override suspend fun getAssignedGangsByUserId(userId: Int, visitDate: String): List<Gang> {
        return apolloClient
            .query(AssignedGangListQuery(userId, visitDate))
            .execute()
            .data
            ?.assignedGangsByUserId
            ?.map { it!!.toGangItem() }
            ?: emptyList()
    }

    override suspend fun getZonesByGangId(gangId: Int): List<Zone> {
        return apolloClient
            .query(ZoneListByGangQuery(gangId))
            .execute()
            .data
            ?.zonesByGangId
            ?.map { it!!.toZoneItem() }
            ?: emptyList()
    }

    override suspend fun getDailyRoutesByCriteria(
        userId: Int,
        gangId: Int,
        visitDate: String
    ): List<DailyRoute> {
        return apolloClient
            .query(DailyRouteFilteredListQuery(userId, gangId, visitDate))
            .execute()
            .data
            ?.dailyRoutesByCriteria
            ?.map { it!!.toDailyRoute() }
            ?: emptyList()
    }
}