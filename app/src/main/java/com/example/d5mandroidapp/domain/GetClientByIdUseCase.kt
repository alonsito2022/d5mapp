package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.models.DetailedClient
import com.example.d5mandroidapp.data.network.ClientApiClient

class GetClientByIdUseCase(
    private val clientApiClient: ClientApiClient
) {
    suspend fun execute(id: Int): DetailedClient {
        return clientApiClient.getClientById(id)
    }
}