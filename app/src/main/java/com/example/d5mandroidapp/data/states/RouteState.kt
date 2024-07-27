package com.example.d5mandroidapp.data.states


import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.models.Gang
import com.example.d5mandroidapp.data.models.Zone
import com.example.d5mandroidapp.data.models.ZonePoint
import com.google.android.gms.maps.model.LatLng
import javax.annotation.concurrent.Immutable

// After
//@Immutable
//data class ImmutableList<T>(var items: List<T>) {
//    constructor():this(listOf())
//}

@Stable
data class RouteState(
    val dailyRoutes: List<DailyRoute> = listOf(),
    val gangsList: List<String> = listOf(),
    val gangsIdsList: List<Int> = listOf(),
    val zoneCenterNameList: List<String> = listOf(),
    val zoneCenterIdList: List<Int> = listOf(),
    val gangs: List<Gang> = listOf(),
    val zones: List<Zone> = listOf(),
    val zonePoints: List<ZonePoint> = listOf(),
    val latLngList: List<LatLng> = listOf(),
    val isLoading: Boolean = false,
    val isOpenSearchDialog: Boolean = false,
    val isOpenMarkerDialog: Boolean = false,
    val selectedDailyRoute: DailyRoute = DailyRoute(),
    val visitDate: String = "2024-05-13",
    val userId: Int = 0,
    val truckId: Int = 0,
    val gangId: Int = 0,
    val truckName: String = "",
    val gangName: String = "",
    val zoneCenterId: Int = 0,
    val zoneCenterLatitude: Double = 0.0,
    val zoneCenterLongitude: Double = 0.0,
    val zoneCenterName: String = "",
    val message: String = "",
    val success: Boolean = false,
    val error: Boolean = false,
//    val navController: NavController
)