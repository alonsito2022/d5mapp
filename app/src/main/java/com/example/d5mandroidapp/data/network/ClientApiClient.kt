package com.example.d5mandroidapp.data.network

import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.models.DetailedClient
import com.example.d5mandroidapp.data.models.OrderWithDebt
import com.example.d5mandroidapp.data.models.ProductTariff
import com.example.d5mandroidapp.data.models.SimpleAddress

interface ClientApiClient {
    suspend fun getClientsFiltered(query1: String, query2: String): List<Client>
    suspend fun getClientsByUser(userId: Int): List<Client>
    suspend fun getClientById(id: Int): DetailedClient
    suspend fun getAddressesByClientId(id: Int): List<SimpleAddress>
    suspend fun getClientWithDebtById(clientId: Int): Client?
    suspend fun getOrderWithDebtByClientId(clientId: Int): List<OrderWithDebt>
}