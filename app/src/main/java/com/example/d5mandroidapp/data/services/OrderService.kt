package com.example.d5mandroidapp.data.services

import com.apollographql.apollo3.ApolloClient
import com.example.d5mandroidapp.SaveOrderMutation
import com.example.d5mandroidapp.UpdateWithoutOrderMutation
import com.example.d5mandroidapp.GeneratedOrderListQuery
import com.example.d5mandroidapp.data.models.Message
import com.example.d5mandroidapp.data.models.Operation
import com.example.d5mandroidapp.data.models.Order
import com.example.d5mandroidapp.data.models.WithoutOrder
import com.example.d5mandroidapp.data.network.OrderApiClient
import okhttp3.internal.ignoreIoExceptions

class OrderService(
    private val apolloClient: ApolloClient
): OrderApiClient {
    override suspend fun saveOrder(order: Order): String? {
        return apolloClient
            .mutation(SaveOrderMutation(
                order.userId,
                order.clientId,
                order.addressId,
                order.typeTradeId,
                order.dailyRouteId,
                order.selectedPaymentType,
                order.selectedDocumentType,
                order.productTariffIds,
                order.quantities,
                order.bonusIds,
                order.prices,
                order.discountPercentages,
                order.discountAmounts,
                order.baseCost,
                order.igvCost,
                order.totalSale,
                order.freeCost,
                order.discountCost,
                order.exoneratedCost,
                order.perceptionCost,
                order.totalToPay
            ))
            .execute()
            .data
            ?.createOrder
            ?.message
    }

    override suspend fun updateWithoutOrder(withoutOrder: WithoutOrder): String? {
        return apolloClient
            .mutation(UpdateWithoutOrderMutation(
                withoutOrder.userId,
                withoutOrder.observation,
                withoutOrder.dailyRouteId
            ))
            .execute()
            .data
            ?.withoutOrder
            ?.message
    }

    override suspend fun getGeneratedOrderList(
        searchDate: String,
        employeeId: Int,
        operationStatus: String
    ): List<Operation> {
        return apolloClient.query(GeneratedOrderListQuery(
            searchDate, employeeId, operationStatus
        ))
            .execute()
            .data
            ?.salesByEmployee
            ?.map { it!!.toOperation() }
            ?: emptyList()
    }

}