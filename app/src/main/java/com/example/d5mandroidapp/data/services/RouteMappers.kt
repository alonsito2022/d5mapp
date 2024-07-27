package com.example.d5mandroidapp.data.services
import com.example.d5mandroidapp.AssignedZoneListByUserQuery
import com.example.d5mandroidapp.AssignedGangListQuery
import com.example.d5mandroidapp.DailyRouteFilteredListQuery
import com.example.d5mandroidapp.ZoneListByGangQuery
import com.example.d5mandroidapp.PointListByZoneQuery
import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.models.Gang
import com.example.d5mandroidapp.data.models.Zone
import com.example.d5mandroidapp.data.models.ZonePoint


fun PointListByZoneQuery.PointsByZoneCenterId.toZonePoint(): ZonePoint {
    return ZonePoint(
        id ?: 0,
        latitude ?: 0.0,
        longitude ?: 0.0
    )
}

fun AssignedZoneListByUserQuery.AssignedZonesByUserId.toZone(): Zone {
    return Zone(
        id ?: 0,
        name ?: "",
        code ?: "",
        color ?: "",
        latitude ?: 0.0,
        longitude ?: 0.0,
        userId ?: 0,
        totalQuantityOfClients?: 0
    )
}

fun AssignedGangListQuery.AssignedGangsByUserId.toGangItem(): Gang {
    return Gang(
        id ?: 0,
        truckId ?: 0,
        truckLicensePlate ?: "",
        name ?: "",
        visitDayId ?: 0,
        visitDayName ?: ""
    )
}

fun ZoneListByGangQuery.ZonesByGangId.toZoneItem(): Zone {
    return Zone(
        id ?: 0,
        name ?: "",
        code ?: "",
        color ?: "",
        latitude ?: 0.0,
        longitude ?: 0.0,
        userId ?: 0
    )
}
fun DailyRouteFilteredListQuery.DailyRoutesByCriterium.toDailyRoute(): DailyRoute {
    return DailyRoute(
        routeId ?: 0,
        routeDailyRouteId ?: 0,
        routePersonId ?: 0,
        routePersonCode ?: "",
        routePersonNames ?: "",
        routePersonPhone ?: "",
        routePersonDocumentTypeReadable ?: "",
        routePersonDocumentNumber ?: "",
        routePersonIsBlocked ?: false,
        routePersonIsSuspended ?: false,
        routePersonIsSuspended ?: false,
        routeAddressId ?: 0,
        routeAddress ?: "",
        routeDayId ?: 0,
        routeDayName ?: "",
        routeDayColor ?: "",
        routePinCode ?: 0.0,
        routeLatitude ?: 0.0,
        routeLongitude ?: 0.0,
        routeDistrictId ?: 0,
        routeDistrictName ?: "",
        routeStatus ?: "",
        routeStatusDisplay ?: "",
        routeObservation ?: "",
        routeDate ?: "",
        routeType ?: "",
        routeOperationId ?: 0,
        routeDailyRouteIsEnabled ?: false,
        routePersonTypeTradeId ?: 0,
        routePersonTypeTradeName ?: "",
        routePersonBusinessTypeName ?: ""
    )
}