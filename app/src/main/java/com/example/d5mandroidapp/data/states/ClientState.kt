package com.example.d5mandroidapp.data.states

import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.models.DetailedClient
import com.example.d5mandroidapp.data.models.OperationDetail
import com.example.d5mandroidapp.data.models.OrderWithDebt
import com.example.d5mandroidapp.data.models.SimpleAddress

data class ClientState(
    var clients: List<Client> = emptyList(),
    var addresses: List<SimpleAddress> = emptyList(),
    var ordersWithDebt: List<OrderWithDebt> = emptyList(),
    val isLoading: Boolean = false,
    val isOpenDialog: Boolean = false,
    val selectedClient: Client? = null,
    val visitDate: String = "2024-05-13",
    val message: String = "",
    val success: Boolean = false,
    val error: Boolean = false
)