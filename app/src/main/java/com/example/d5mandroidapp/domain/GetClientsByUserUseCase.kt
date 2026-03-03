package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.network.ClientApiClient

class GetClientsByUserUseCase(
    private val clientApiClient: ClientApiClient
) {
    suspend fun execute(userId: Int): List<Client> {
        return clientApiClient.getClientsByUser(userId).sortedBy { it.id }
    }
}

