package com.example.d5mandroidapp.data.states
import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.models.Gang
import com.example.d5mandroidapp.data.models.User
import com.example.d5mandroidapp.data.models.Zone

data class UserState(
    val isLoading: Boolean = false,
    val selectedUser: User = User(),
    val message: String = "",
    val token: String = "",
    val success: Boolean = false,
    val error: Boolean = false,
    val invalidSignature: Boolean = false,

    val visitDate: String = "2024-05-13",
    val gangs: List<Gang> = listOf(),
    val zones: List<Zone> = listOf(),
    val gangId: Int = 0,
    val zoneCenterId: Int = 0,
    val userId: Int = 0,
    val truckId: Int = 0,

    val gangName: String = "",
    val zoneCenterName: String = "",
    val dailyRoutes: List<DailyRoute> = listOf(),
    var filteredDailyRoutes: List<DailyRoute> = emptyList(),
    var showSearchPanel: Boolean = false,

) {
}