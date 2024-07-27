package com.example.d5mandroidapp.ui.viewmodels

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.d5mandroidapp.data.models.DailyRoute
import com.example.d5mandroidapp.data.states.RouteState
import com.example.d5mandroidapp.domain.GetAssignedGangsByUserIdUseCase
import com.example.d5mandroidapp.domain.GetDailyRoutesByCriteriaUseCase
import com.example.d5mandroidapp.storage.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.d5mandroidapp.data.models.Zone
import com.example.d5mandroidapp.domain.GetAssignedZonesByUserIdUseCase
import com.example.d5mandroidapp.domain.GetZonePointsByZoneIdUseCase
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class RouteViewModel @Inject constructor(
    private val getAssignedGangsByUserIdUseCase: GetAssignedGangsByUserIdUseCase,
    private val getAssignedZonesByUserIdUseCase: GetAssignedZonesByUserIdUseCase,
    private val getZonePointsByZoneIdUseCase: GetZonePointsByZoneIdUseCase,
    private val getDailyRoutesByCriteriaUseCase: GetDailyRoutesByCriteriaUseCase,
    @ApplicationContext context: Context
): ViewModel() {
    private val _state = MutableStateFlow(RouteState())
    val state = _state.asStateFlow()
    private val userRepository = UserRepositoryImpl(context)

    init {
        val nextMondayDate = getCurrentOrNextMondayDate()
        viewModelScope.launch {
            userRepository.getUserId()?.let { setUser(it.toInt(), nextMondayDate) }
            assignedGangsAndZones()
        }
    }

//    private fun getDayOfWeekInKotlin(dayOfWeekInPython:Int): Int{
//        var targetDayOfWeek: Int = 0
//        when(dayOfWeekInPython){
//            0 -> targetDayOfWeek=2 // Lu
//            1 -> targetDayOfWeek=3 // Ma
//            2 -> targetDayOfWeek=4 // Mi
//            3 -> targetDayOfWeek=5 // Ju
//            4 -> targetDayOfWeek=6 // Vi
//            5 -> targetDayOfWeek=7 // Sa
//            6 -> targetDayOfWeek=1 // Do
//        }
//        return targetDayOfWeek
//    }

//    fun getFormattedDispatchDate(dayId: Int): String {
//        val today = Calendar.getInstance()
//        val todayDayOfWeek = today.get(Calendar.DAY_OF_WEEK) // 1 (Domingo) a 7 (Sab)
//
//        val targetDayOfWeek = getDayOfWeekInKotlin(dayId)
//        val daysDiff = targetDayOfWeek - todayDayOfWeek
//
//        val MILLIS_IN_SECOND = 1000
//        val SECONDS_IN_HOUR = 3600
//        val HOURS_IN_DAY = 24
//        val DAY_MILLIS = HOURS_IN_DAY * SECONDS_IN_HOUR * MILLIS_IN_SECOND
//
//        val adjustedDate = Calendar.getInstance()
//        adjustedDate.timeInMillis = today.timeInMillis + daysDiff * DAY_MILLIS
//
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
//        return dateFormat.format(adjustedDate.time)
//    }

    private fun getCurrentOrNextMondayDate(): String {
        // Get the current date
        val currentDate = Calendar.getInstance()

        // Check if today is Sunday
        val isSunday = currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY

        // Determine the date to return
        if (isSunday) {
            // If today is Sunday, get next Monday's date
            currentDate.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Format the date to "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(currentDate.time)
    }

    private fun setUser(userId: Int, visitDate: String) {
        _state.update { it.copy( userId = userId, visitDate =  visitDate ) }
    }

    fun setSelectedZoneCenter(zoneCenterId: Int, zoneCenterName: String) {
        viewModelScope.launch {
            val selectedZone = _state.value.zones.find{ zone ->  zone.id == zoneCenterId } as Zone
            val latLngList = getZonePointsByZoneIdUseCase.execute(zoneCenterId).map { point -> LatLng(point.latitude, point.longitude) }
            _state.update { it.copy(
                zoneCenterId = zoneCenterId,
                zoneCenterName = zoneCenterName,
                zoneCenterLatitude = selectedZone.latitude,
                zoneCenterLongitude = selectedZone.longitude,
                latLngList = latLngList.toMutableList().apply { add(latLngList[0]) },

//                dispatchDate = getFormattedDispatchDate(selectedZone.dayId)
            ) }
        }
    }

    fun setSelectedTruck(truckId: Int, truckName: String) {
        viewModelScope.launch {
            _state.update { it.copy(
                truckId = truckId,
                truckName = truckName
            ) }
        }
    }

    fun showRouteDialog() {
        _state.update { it.copy(isOpenSearchDialog = true) }
    }

    fun hideRouteDialog() {
        _state.update { it.copy(isOpenSearchDialog = false) }
    }

    fun setSelectedDailyRoute(dailyRouteId: Int) {
        _state.update { it ->
            it.copy(
            isOpenMarkerDialog = true,
            selectedDailyRoute = _state.value.dailyRoutes.find{ dailyRoute ->  dailyRoute.routeDailyRouteId == dailyRouteId } as DailyRoute
        ) }
    }

//    fun showMarkerDialog() {
//        _state.update { it.copy(isOpenMarkerDialog = true) }
//    }

    fun hideMarkerDialog() {
        _state.update { it.copy(
            isOpenMarkerDialog = false,
//            selectedDailyRoute = DailyRoute()
        ) }
    }

    private suspend fun assignedGangsAndZones(){
        val updatedAssignedGangs = getAssignedGangsByUserIdUseCase.execute(state.value.userId, state.value.visitDate)
        val updatedAssignedZones = getAssignedZonesByUserIdUseCase.execute(state.value.userId, state.value.visitDate).filter { zone -> zone.totalQuantityOfClients > 0 }


        if (updatedAssignedGangs.isNotEmpty() && updatedAssignedZones.isNotEmpty()){

            val latLngList = getZonePointsByZoneIdUseCase.execute(updatedAssignedZones[0].id).map { point -> LatLng(point.latitude, point.longitude) }
            Log.d("D5MAP","latLngList : $latLngList")
            _state.update { it.copy(
                gangs = updatedAssignedGangs,
                gangsList =  updatedAssignedGangs.map { gang -> "${gang.name} - ${gang.truckLicensePlate}" },
                gangsIdsList =  updatedAssignedGangs.map { gang -> gang.id },

                gangId = updatedAssignedGangs[0].id,
                gangName = updatedAssignedGangs[0].name,
                truckId = updatedAssignedGangs[0].truckId,
                truckName = updatedAssignedGangs[0].truckLicensePlate,

                zones = updatedAssignedZones,
                zoneCenterNameList = updatedAssignedZones.map { zone -> "${zone.code} - ${zone.totalQuantityOfClients} CLIENTES" },
                zoneCenterIdList = updatedAssignedZones.map { zone -> zone.id },

                zoneCenterId = updatedAssignedZones[0].id,
                zoneCenterName = updatedAssignedZones[0].name,
                zoneCenterLatitude = updatedAssignedZones[0].latitude,
                zoneCenterLongitude = updatedAssignedZones[0].longitude,
                latLngList = latLngList.toMutableList().apply { add(latLngList[0]) },

//                dispatchDate = getFormattedDispatchDate(updatedAssignedZones[0].dayId)
            ) }

        }
    }
    fun setSelectedVisitDate(visitDate: String) {
        _state.update { it.copy( visitDate = visitDate) }
        viewModelScope.launch {
            assignedGangsAndZones()
        }
    }

    fun cleanList(){
        viewModelScope.launch {
            _state.update { it.copy(
                dailyRoutes = listOf()
            ) }
        }
    }
    fun searchQuery(){
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }

            _state.update { it.copy(
                dailyRoutes = getDailyRoutesByCriteriaUseCase.execute(
                    state.value.userId,
                    state.value.gangId,
                    state.value.visitDate,
                ).filter { it.routeDailyRouteIsEnabled },
                isLoading = false
            ) }
        }
//        Log.d("D5MAP","userId : ${state.value.userId}")
//        Log.d("D5MAP","truckId : ${state.value.truckId}")
//        Log.d("D5MAP","truckName : ${state.value.truckName}")
//        Log.d("D5MAP","zoneCenterId : ${state.value.zoneCenterId}")
//        Log.d("D5MAP","zoneCenterName : ${state.value.zoneCenterName}")
//        Log.d("D5MAP","dispatchDate : ${state.value.dispatchDate}")
    }
//    fun navTo(){
//        val action = Screens.Order.screen + "/${state.value.selectedDailyRoute.routePersonId}/${state.value.selectedDailyRoute.routePersonNames}/${state.value.selectedDailyRoute.routeDailyRouteId}"
//        rememberNavController().navigate(action)
//    }
}