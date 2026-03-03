package com.example.d5mandroidapp.data.services

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.d5mandroidapp.ClientAddressesListQuery
import com.example.d5mandroidapp.ClientFilteredListQuery
import com.example.d5mandroidapp.GetClientDebtQuery
import com.example.d5mandroidapp.GetClientsByUserQuery
import com.example.d5mandroidapp.OrderWithDebtListByClientQuery
import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.models.DetailedClient
import com.example.d5mandroidapp.data.models.OrderWithDebt
import com.example.d5mandroidapp.data.models.SimpleAddress
import com.example.d5mandroidapp.data.network.ClientApiClient

class ClientService(
    private val apolloClient: ApolloClient
): ClientApiClient {

    override suspend fun getClientsFiltered(query1: String, query2: String): List<Client> {
        return apolloClient
            .query(ClientFilteredListQuery(query1, query2))
            .execute()
            .data
            ?.searchClientsByCriteria
            ?.map { it!!.toClientFiltered() }
            ?: emptyList()
    }

    override suspend fun getClientsByUser(userId: Int): List<Client> {
        return apolloClient
            .query(GetClientsByUserQuery(Optional.present(userId)))
            .execute()
            .data
            ?.allClientsByUserId
            ?.map { it!!.toClient() }
            ?: emptyList()
    }

    override suspend fun getClientById(id: Int): DetailedClient {
        TODO("Not yet implemented")
    }

    override suspend fun getAddressesByClientId(id: Int): List<SimpleAddress> {
        return apolloClient
            .query(ClientAddressesListQuery(id))
            .execute()
            .data
            ?.addressesByClientId
            ?.map { it!!.toSimpleAddress() }
            ?: emptyList()
    }

    override suspend fun getClientWithDebtById(clientId: Int): Client? {
        return apolloClient
            .query(GetClientDebtQuery(clientId))
            .execute()
            .data
            ?.clientWithDebt
            ?.toClient()
    }

    override suspend fun getOrderWithDebtByClientId(clientId: Int): List<OrderWithDebt> {
        return  apolloClient
            .query(OrderWithDebtListByClientQuery(clientId))
            .execute()
            .data
            ?.accountStatusByClient
            ?.map { it!!.toOrderWithDebt() }
            ?: emptyList()
    }

}