package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.SimpleAddress
import com.example.d5mandroidapp.data.network.ClientApiClient

class GetSimpleAddressesByClientIdUseCase(
    private val clientApiClient: ClientApiClient
) {
    suspend fun execute(id: Int): List<SimpleAddress>{
        return clientApiClient.getAddressesByClientId(id).sortedBy { it.id }
    }
}