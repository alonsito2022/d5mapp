package com.example.d5mandroidapp.ui.viewmodels
import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.models.OperationDetail
import com.example.d5mandroidapp.data.models.OrderWithDebt
import com.example.d5mandroidapp.data.models.ProductTariff
import com.example.d5mandroidapp.data.states.ClientState
import com.example.d5mandroidapp.domain.GetClientByIdUseCase
import com.example.d5mandroidapp.domain.GetClientWithDebtByIdUseCase
import com.example.d5mandroidapp.domain.GetClientsFilteredUseCase
import com.example.d5mandroidapp.domain.GetOrdersWithDebtByClientIdUseCase
import com.example.d5mandroidapp.domain.GetSimpleAddressesByClientIdUseCase
import com.example.d5mandroidapp.domain.SavePaymentListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val getClientsFilteredUseCase: GetClientsFilteredUseCase,
    private val getSimpleAddressesByClientIdUseCase: GetSimpleAddressesByClientIdUseCase,
    private val getClientWithDebtByIdUseCase: GetClientWithDebtByIdUseCase,
    private val getOrdersWithDebtByClientIdUseCase: GetOrdersWithDebtByClientIdUseCase,
    private val savePaymentListUseCase: SavePaymentListUseCase

): ViewModel() {

    private val _state = MutableStateFlow(ClientState())
    val state = _state.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchBy = MutableStateFlow("names")
    val searchBy = _searchBy.asStateFlow()

    private fun filterAndSearchByCriteria(text: String){

        viewModelScope.launch {

            _state.update { it.copy(
                isLoading = true
            ) }
            withContext(Dispatchers.IO){
                _state.update { it.copy(
                    clients = getClientsFilteredUseCase.execute(text, searchBy.value)
                ) }
            }

            _state.update { it.copy(
                isLoading = false
            ) }
        }
    }

    fun setClient(clientId: String){
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(
                selectedClient = getClientWithDebtByIdUseCase.execute(clientId.toInt()),
                ordersWithDebt = getOrdersWithDebtByClientIdUseCase.execute(clientId.toInt())

            ) }
        }
    }

    fun searchAddresses(clientId: String){
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(
                addresses = getSimpleAddressesByClientIdUseCase.execute(clientId.toInt())
            )}
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        viewModelScope.launch {
            _searchText.debounce(1000L)
                .collect { debouncedText ->
                    if (debouncedText.length >= 3) {

                        filterAndSearchByCriteria(debouncedText)
                    }
                    else{
                        _state.update { it.copy(clients = emptyList()) }
                    }
                }
        }
    }

    fun updateSearchBy(newSearchBy: String) {
        _searchBy.value = newSearchBy
    }

    fun onTextChangeOrderWithDebtTotalToPaid(textTotalToPaid: String, orderWithDebt: OrderWithDebt){
        var newTotalToPaid = 0.0
        if (textTotalToPaid.isNotEmpty()) {
            newTotalToPaid = textTotalToPaid.toDouble()
        }
        val index = state.value.ordersWithDebt.indexOf(orderWithDebt)
        orderWithDebt.totalToPaid = newTotalToPaid

        viewModelScope.launch {
            _state.update { it.copy(
                ordersWithDebt = it.ordersWithDebt.toMutableList().apply { set(index, orderWithDebt) }
            )}
        }
    }

    fun onClickOrderWithDebtItem(orderWithDebt: OrderWithDebt, status: Boolean) {
        onChangeStatusOrderWithDebt(orderWithDebt.id.toInt() , status)
    }

    private fun onChangeStatusOrderWithDebt(orderWithDebtId: Int, status: Boolean) {
        val searchOrderWithDebt = state.value.ordersWithDebt.find { it.id.toInt() == orderWithDebtId }
        if (searchOrderWithDebt != null) {
            val index = state.value.ordersWithDebt.indexOf(searchOrderWithDebt)
            searchOrderWithDebt.isSelected = status
            viewModelScope.launch {
                _state.update { it.copy(
                    ordersWithDebt = it.ordersWithDebt.toMutableList().apply { set(index, searchOrderWithDebt) }
                )}
            }
        }
    }

    fun sendOrdersToPay(){
        val orderWithDebtIds = mutableListOf<Int>()
        val payments = mutableListOf<Double>()
        state.value.ordersWithDebt.forEach {orderWithDebt ->
            if (orderWithDebt.totalToPaid>0){
                orderWithDebtIds.add(orderWithDebt.id.toInt())
                payments.add(orderWithDebt.totalToPaid)
            }
        }

        if (payments.isNotEmpty()){
            Log.d("D5MAP","payments.size : ${payments.size}")

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            message = savePaymentListUseCase.execute(
                                Optional.presentIfNotNull(orderWithDebtIds),
                                Optional.presentIfNotNull(payments)
                            ).toString()
                        )
                    }
                }
                _state.update {
                    it.copy(
                        success = true
                    )
                }
            }

        }
        else{
            Log.d("D5MAP","Verifique ordenes a cobrar")
            viewModelScope.launch {
                _state.update { it.copy(
                    message = "Verifique ordenes a cobrar",
                    error = true
                ) }
            }
        }
    }

    fun setSuccessOrError() {
        _state.update { it.copy(
            error = false,
            success = false
        ) }
    }
}

