package com.example.d5mandroidapp.ui.viewmodels
import android.content.Context
import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.example.d5mandroidapp.apollo.ApolloClientUpdater
import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.models.OperationDetail
import com.example.d5mandroidapp.data.models.OrderWithDebt
import com.example.d5mandroidapp.data.models.ProductTariff
import com.example.d5mandroidapp.data.states.ClientState
import com.example.d5mandroidapp.domain.GetClientByIdUseCase
import com.example.d5mandroidapp.domain.GetClientWithDebtByIdUseCase
import com.example.d5mandroidapp.domain.GetClientsByUserUseCase
import com.example.d5mandroidapp.domain.GetOrdersWithDebtByClientIdUseCase
import com.example.d5mandroidapp.domain.GetSimpleAddressesByClientIdUseCase
import com.example.d5mandroidapp.domain.SavePaymentListUseCase
import com.example.d5mandroidapp.storage.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val getClientsByUserUseCase: GetClientsByUserUseCase,
    private val getSimpleAddressesByClientIdUseCase: GetSimpleAddressesByClientIdUseCase,
    private val getClientWithDebtByIdUseCase: GetClientWithDebtByIdUseCase,
    private val getOrdersWithDebtByClientIdUseCase: GetOrdersWithDebtByClientIdUseCase,
    private val savePaymentListUseCase: SavePaymentListUseCase,
    @ApplicationContext context: Context
//    private val apolloClientUpdater: ApolloClientUpdater

): ViewModel() {

    private val _state = MutableStateFlow(ClientState())
    val state = _state.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchBy = MutableStateFlow("names")
    val searchBy = _searchBy.asStateFlow()

    private val userRepository = UserRepositoryImpl(context)
    private var allClients: List<Client> = emptyList()

    init {
        viewModelScope.launch {
            try {
                val userIdString = userRepository.getUserId()
                Log.d("D5MAP", "ClientViewModel init - userIdString: $userIdString")
                if (userIdString != null && userIdString.isNotEmpty()) {
                    val userIdInt = userIdString.toInt()
                    Log.d("D5MAP", "ClientViewModel init - userId: $userIdInt")
                    _state.update { it.copy(userId = userIdInt) }
                    // Cargar todos los clientes del usuario al inicializar
                    loadClientsByUser(userIdInt)
                } else {
                    Log.d("D5MAP", "ClientViewModel init - userId is null or empty")
                }
            } catch (e: Exception) {
                Log.e("D5MAP", "ClientViewModel init - Error: ${e.message}", e)
            }
        }
    }

    private suspend fun loadClientsByUser(userId: Int) {
        if (userId > 0) {
            Log.d("D5MAP", "loadClientsByUser - starting with userId: $userId")
            try {
                _state.update { it.copy(isLoading = true) }
                val loadedClients = withContext(Dispatchers.IO) {
                    getClientsByUserUseCase.execute(userId)
                }
                allClients = loadedClients
                Log.d("D5MAP", "loadClientsByUser - Clients loaded: ${allClients.size}")
                _state.update { 
                    it.copy(
                        clients = allClients,
                        isLoading = false
                    ) 
                }
                Log.d("D5MAP", "loadClientsByUser - State updated with ${_state.value.clients.size} clients")
            } catch (e: Exception) {
                Log.e("D5MAP", "loadClientsByUser - Error loading clients: ${e.message}", e)
                _state.update { it.copy(isLoading = false) }
            }
        } else {
            Log.d("D5MAP", "loadClientsByUser - userId is not valid: $userId")
        }
    }
    
    fun loadClients() {
        viewModelScope.launch {
            val userId = _state.value.userId
            if (userId > 0) {
                loadClientsByUser(userId)
            } else {
                userRepository.getUserId()?.let { userIdString ->
                    val userIdInt = userIdString.toInt()
                    _state.update { it.copy(userId = userIdInt) }
                    loadClientsByUser(userIdInt)
                }
            }
        }
    }

    private fun filterAndSearchByCriteria(text: String){

        viewModelScope.launch {

            _state.update { it.copy(
                isLoading = true
            ) }
            withContext(Dispatchers.IO){
                // Si no hay clientes cargados, cargarlos primero
                if (allClients.isEmpty() && _state.value.userId > 0) {
                    loadClientsByUser(_state.value.userId)
                }
                
                // Filtrar localmente según el texto de búsqueda
                val filteredClients = if (text.length >= 3) {
                    allClients.filter { client ->
                        when (searchBy.value) {
                            "names" -> client.names.contains(text, ignoreCase = true)
                            "code" -> client.code.contains(text, ignoreCase = true)
                            "document" -> client.documentNumber.contains(text, ignoreCase = true)
                            else -> false
                        }
                    }
                } else {
                    // Si no hay texto de búsqueda o es menor a 3 caracteres, mostrar todos los clientes
                    allClients
                }
                
                _state.update { it.copy(
                    clients = filteredClients
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

//    init {
//        viewModelScope.launch {
//            apolloClientUpdater.apolloClient.collect { client ->
//                // El cliente Apollo ha sido actualizado
//                // Puedes realizar acciones adicionales si es necesario
//            }
//        }
//    }

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
                        // Si no hay texto o es menor a 3 caracteres, mostrar todos los clientes
                        _state.update { it.copy(clients = allClients) }
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

    fun setUser(userId: Int) {
        _state.update { it.copy(
            userId = userId
        ) }
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
                                Optional.presentIfNotNull(payments),
                                state.value.userId
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

