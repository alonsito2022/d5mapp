package com.example.d5mandroidapp.data.models

data class DailyRoute(
    val routeId: Int = 0,
    val routeDailyRouteId: Int = 0,
    val routePersonId: Int = 0,
    val routePersonCode: String = "",
    val routePersonNames: String = "",
    val routePersonPhone: String = "",
    val routePersonDocumentTypeReadable: String = "",
    val routePersonDocumentNumber: String = "",
    val routePersonIsBlocked: Boolean = false,
    val routePersonIsSuspended: Boolean = false,
    val routePersonIsObserved: Boolean = false,
    val routeAddressId: Int = 0,
    val routeAddress: String = "",
    val routeDayId: Int = 0,
    val routeDayName: String = "",
    val routeDayColor: String = "",
    val routePinCode: Double = 0.0,
    val routeLatitude: Double = 0.0,
    val routeLongitude: Double = 0.0,
    val routeDistrictId: Int = 0,
    val routeDistrictName: String = "",
    val routeStatus: String = "",
    val routeStatusDisplay: String = "",
    val routeObservation: String = "",
    val routeDate: Any? = null,
    val routeType: String = "",
    val routeOperationId: Int = 0,
    val routeDailyRouteIsEnabled: Boolean = false,
    val routePersonTypeTradeId: Int = 0,
    val routePersonTypeTradeName: String = "",
    val routePersonBusinessTypeName: String = ""
) {
    constructor() : this(
        0, 0, 0,"","","","",
        "",false,false,false,0,"",
        0,"","",0.0,0.0,0.0,
        0,"","","","",
        null,"",0,false,0, "", ""
        )
}