package com.example.d5mandroidapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.d5mandroidapp.data.states.GeneratedOrderState
import com.example.d5mandroidapp.domain.GetGeneratedOrderUseCase
import com.example.d5mandroidapp.storage.UserRepositoryImpl

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
@HiltViewModel
class GeneratedOrderViewModel @Inject constructor(
    private val getGeneratedOrderUseCase: GetGeneratedOrderUseCase,
    @ApplicationContext context: Context
) : ViewModel() {
    private val userRepository = UserRepositoryImpl(context)
    private val _state = MutableStateFlow(GeneratedOrderState())
    val state = _state.asStateFlow()

    private fun getCurrentOrNextMondayDate(): String {
        val currentDate = Calendar.getInstance()
        val isSunday = currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
        if (isSunday) currentDate.add(Calendar.DAY_OF_YEAR, 1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(currentDate.time)
    }

    fun getTotalGeneratedSales(): Double {
        return state.value.operations.filter { it.operationStatusName == "GENERADO" }.sumOf { it.totalSale }
    }

    fun getTotalApprovedSales(): Double {
        return state.value.operations.filter { it.operationStatusName == "APROBADO" }.sumOf { it.totalSale }
    }

    fun getTotalCancelledSales(): Double {
        return state.value.operations.filter { it.operationStatusName == "ANULADO" }.sumOf { it.totalSale }
    }

    init {
        val nextMondayDate = getCurrentOrNextMondayDate()
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(
                isLoading = true
            )}
            userRepository.getUserId()?.let {userId ->
                _state.update {
                    it.copy(
                        userId = userId.toInt(), visitDate =  nextMondayDate
                    )
                }
            }

            val deferredJobGeneratedOrders = async { getGeneratedOrderUseCase.execute( state.value.visitDate, state.value.userId, "00")}
            val newGeneratedOrders = deferredJobGeneratedOrders.await()
            if (newGeneratedOrders.isNotEmpty()){
                _state.update { it.copy(
                    operations = newGeneratedOrders
                )}
            }

            _state.update { it.copy(
                isLoading = false
            )}

        }
    }
}