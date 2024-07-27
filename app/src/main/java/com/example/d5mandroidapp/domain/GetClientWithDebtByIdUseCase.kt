package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.network.ClientApiClient

class GetClientWithDebtByIdUseCase(
    private val clientApiClient: ClientApiClient
) {
    suspend fun execute(clientId: Int): Client?{
        return clientApiClient.getClientWithDebtById(clientId)
    }
}