package com.example.d5mandroidapp.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.d5mandroidapp.data.models.Gang
import com.example.d5mandroidapp.data.models.Zone
import com.example.d5mandroidapp.data.states.UserState
import com.example.d5mandroidapp.domain.GetAssignedGangsByUserIdUseCase
import com.example.d5mandroidapp.domain.GetAssignedZonesByUserIdUseCase
import com.example.d5mandroidapp.domain.GetClientWithDebtByIdUseCase
import com.example.d5mandroidapp.domain.GetClientsFilteredUseCase
import com.example.d5mandroidapp.domain.GetDailyRoutesByCriteriaUseCase
import com.example.d5mandroidapp.domain.GetOrdersWithDebtByClientIdUseCase
import com.example.d5mandroidapp.domain.GetSimpleAddressesByClientIdUseCase
import com.example.d5mandroidapp.domain.GetZonePointsByZoneIdUseCase
import com.example.d5mandroidapp.domain.RemoveRefreshTokenUseCase
import com.example.d5mandroidapp.domain.SavePaymentListUseCase
import com.example.d5mandroidapp.domain.VerifyTokenUseCase
import com.example.d5mandroidapp.storage.TokenRepositoryImpl
import com.example.d5mandroidapp.storage.UserRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getClientsFilteredUseCase: GetClientsFilteredUseCase,
    private val getSimpleAddressesByClientIdUseCase: GetSimpleAddressesByClientIdUseCase,
    private val getClientWithDebtByIdUseCase: GetClientWithDebtByIdUseCase,
    private val getOrdersWithDebtByClientIdUseCase: GetOrdersWithDebtByClientIdUseCase,
    private val savePaymentListUseCase: SavePaymentListUseCase,
    private val getAssignedGangsByUserIdUseCase: GetAssignedGangsByUserIdUseCase,
    private val getAssignedZonesByUserIdUseCase: GetAssignedZonesByUserIdUseCase,
//    private val verifyTokenUseCase: VerifyTokenUseCase,
    private val getDailyRoutesByCriteriaUseCase: GetDailyRoutesByCriteriaUseCase,
    private val removeRefreshTokenUseCase: RemoveRefreshTokenUseCase,
    @ApplicationContext context: Context
): ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state = _state.asStateFlow()

    private val _searchClientText = MutableStateFlow("")
    val searchClientText = _searchClientText.asStateFlow()

    private val _searchClientBy = MutableStateFlow("names")
    val searchClientBy = _searchClientBy.asStateFlow()

    private val userRepository = UserRepositoryImpl(context)
    private val tokenRepository = TokenRepositoryImpl(context)

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

    fun setSelectedVisitDate(visitDate: String) {
        _state.update { it.copy( visitDate = visitDate) }

        assignedGangsAndZones()

    }

    fun assignedGangsAndZones(){
        Log.d("D5MAP2","userId: ${state.value.userId} visitDate: ${state.value.visitDate}")
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            )}
            withContext(Dispatchers.IO){
                val deferredList = listOf(
                    async {getAssignedGangsByUserIdUseCase.execute(state.value.userId, state.value.visitDate)},
                    async {getAssignedZonesByUserIdUseCase.execute(state.value.userId, state.value.visitDate).filter { zone -> zone.totalQuantityOfClients > 0 }}
                )

                val result = deferredList.awaitAll()

                val gangList = result[0] as List<Gang>
                val zoneList = result[1] as List<Zone>


//            val deferredJobGangs = viewModelScope.async {getAssignedGangsByUserIdUseCase.execute(state.value.userId, state.value.visitDate)}
//            val gangList = deferredJobGangs.await()
                Log.d("D5MAP2","gangList: ${gangList.size}")
                if (gangList.isNotEmpty()){

//                val deferredJobZones = viewModelScope.async {getAssignedZonesByUserIdUseCase.execute(state.value.userId, state.value.visitDate).filter { zone -> zone.totalQuantityOfClients > 0 }}
//                val zoneList = deferredJobZones.await()

                    Log.d("D5MAP2","zoneList: ${zoneList.size}")

                    if (zoneList.isNotEmpty()){

                        _state.update { it.copy(
                            gangs = gangList,
                            zones = zoneList,
                            truckId = gangList[0].truckId,
                            gangId = gangList[0].id,
                            zoneCenterId = zoneList[0].id,
                        )}


                    }
                }
            }

            _state.update { it.copy(
                isLoading = false
            )}

        }
    }

    fun setSelectedZoneCenter(zoneCenterId: Int, zoneCenterName: String) {
        viewModelScope.launch {
            _state.update { it.copy(
                zoneCenterId = zoneCenterId, zoneCenterName = zoneCenterName
            ) }
        }
    }
    fun setSelectedGang(gangId: Int, gangName: String) {
        viewModelScope.launch {
            _state.update { it.copy(
                gangId = gangId,
                gangName = gangName
            ) }
        }
    }

//    fun closeSession(){
//        viewModelScope.launch {
//            var isLogout = removeRefreshTokenUseCase
//            _state.update { it.copy(
//                isOffline = true
//            ) }
//        }
//    }

    private fun filterAndSearchByCriteria(text: String){

        viewModelScope.launch {

            _state.update { it.copy(
                isLoading = true
            ) }

            val deferredJobDailyRoutes = async {getDailyRoutesByCriteriaUseCase.execute(state.value.userId, state.value.gangId, state.value.visitDate, searchClientText.value, searchClientBy.value).filter { it.routeDailyRouteIsEnabled }}
            val dailyRoutesList = deferredJobDailyRoutes.await()
            Log.d("D5MAP2","dailyRoutesList: ${dailyRoutesList.size}")
            if (dailyRoutesList.isNotEmpty()){
                _state.update { it.copy(
//                    dailyRoutes = dailyRoutesList,
                    filteredDailyRoutes = dailyRoutesList
                )}
            }

            _state.update { it.copy(
//                filteredDailyRoutes = _state.value.dailyRoutes.filter { dailyRoute ->
//                    when (searchClientBy.value) {
//                        "names" -> dailyRoute.routePersonNames.contains(text, ignoreCase = true)
//                        "code" -> dailyRoute.routePersonCode.contains(text, ignoreCase = true)
//                        "document" -> dailyRoute.routePersonDocumentNumber.contains(text, ignoreCase = true)
//                        else -> false // Si el criterio de búsqueda no es válido, no se incluye en el filtro
//                    }
//                },
                isLoading = false
            ) }

            onSearchTextChange("")

        }
    }

    fun onSearchTextChange(text: String) {
        _searchClientText.value = text
//        viewModelScope.launch {
//            _searchClientText.debounce(1000L)
//                .collect { debouncedText ->
//                    if (debouncedText.length >= 3) {
//
//                        filterAndSearchByCriteria(debouncedText)
//                    }
//                    else{
//                        _state.update { it.copy(filteredDailyRoutes = emptyList()) }
//                    }
//                }
//        }
    }
    fun updateSearchBy(newSearchBy: String) {
        _searchClientBy.value = newSearchBy
    }

    fun onClickButtonSearch(){

        if (searchClientText.value.length >= 3) {
            filterAndSearchByCriteria(searchClientText.value)
        }
        else{
            _state.update { it.copy(filteredDailyRoutes = emptyList()) }
        }

    }

    init {
        val nextMondayDate = getCurrentOrNextMondayDate()
        viewModelScope.launch {
            userRepository.getUserId()?.let {userId ->
                _state.update {
                    it.copy(
                        userId = userId.toInt(), visitDate =  nextMondayDate
                    )
                }
            }
        }
        assignedGangsAndZones()

    }

    fun onClickButtonToggleSearchPanel(){
        _state.update {
            it.copy(
                showSearchPanel = !_state.value.showSearchPanel
            )
        }
    }

//    fun getToken(){
//        viewModelScope.launch {
//            tokenRepository.getToken()?.let {token ->
//                _state.update {
//                    it.copy(
//                        token = token
//                    )
//                }
//            }
//        }
//    }

}