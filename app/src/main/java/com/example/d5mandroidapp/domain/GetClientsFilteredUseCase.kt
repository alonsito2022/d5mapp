package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.network.ClientApiClient

class GetClientsFilteredUseCase(
    private val clientApiClient: ClientApiClient
) {
    suspend fun execute(query1: String, query2: String): List<Client>{
        return clientApiClient.getClientsFiltered(query1, query2).sortedBy { it.id }
    }
}