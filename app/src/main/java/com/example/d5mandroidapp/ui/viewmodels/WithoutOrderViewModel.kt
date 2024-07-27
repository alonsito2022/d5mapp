package com.example.d5mandroidapp.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.d5mandroidapp.data.models.WithoutOrder
import com.example.d5mandroidapp.data.states.WithoutOrderState
import com.example.d5mandroidapp.domain.UpdateWithoutOrderUseCase
import com.example.d5mandroidapp.storage.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithoutOrderViewModel @Inject constructor(
    private val updateWithoutOrderUseCase: UpdateWithoutOrderUseCase,
    @ApplicationContext context: Context
): ViewModel()  {

    private val _state = MutableStateFlow(WithoutOrderState())
    val state = _state.asStateFlow()
    private val userRepository = UserRepositoryImpl(context)
    init {
        viewModelScope.launch {
            userRepository.getUserId()?.let { setUser(it.toInt()) }
        }
    }
    fun setUser(userId: Int) {
        _state.update { it.copy(
            userId = userId
        ) }
    }

    fun setDailyRouteId(dailyRouteId: Int){
        _state.update { it.copy(
            dailyRouteId = dailyRouteId.toString()
        ) }
    }

    fun onObservationTextChange(observation: String){
        _state.update { it.copy(
            observation = observation
        ) }
    }

    fun showConfirmDialog() {
        _state.update { it.copy(isOpenConfirmDialog = true) }
    }

    fun hideConfirmDialog() {
        _state.update { it.copy(isOpenConfirmDialog = false) }
    }

    fun setSuccessOrError() {
        _state.update { it.copy(
            error = false,
            success = false
        ) }
    }

    fun saveWithoutOrder(){

        if (state.value.observation.isNotEmpty()){
            Log.d("WithoutOrderViewModel","observation : ${state.value.observation}")
            Log.d("WithoutOrderViewModel","userId : ${state.value.userId}")
            Log.d("WithoutOrderViewModel","dailyRouteId : ${state.value.dailyRouteId}")
            val withoutOrder = WithoutOrder(
                state.value.userId.toString(),
                state.value.observation,
                state.value.dailyRouteId
            )

            viewModelScope.launch {
                _state.update { it.copy(
                    message = updateWithoutOrderUseCase.execute(withoutOrder).toString(),
                    success = true,
                    observation = ""
                ) }
            }

        }else{
            Log.d("WithoutOrderViewModel","Verifique observacion")
            viewModelScope.launch {
                _state.update { it.copy(
                    message = "Verifique observacion",
                    error = true
                ) }
            }
        }
    }

    fun onChangeSelectReason(status: Boolean){
        viewModelScope.launch {
            _state.update{it.copy(
                selectReason = status
            ) }
        }
    }

}