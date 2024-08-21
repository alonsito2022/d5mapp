package com.example.d5mandroidapp.data.services
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.d5mandroidapp.data.network.PaymentApiClient
import com.example.d5mandroidapp.SavePaymentListMutation

class PaymentService(
    private val apolloClient: ApolloClient
): PaymentApiClient {

    override suspend fun savePaymentList(
        orderWithDebtIds: Optional<List<Int>?>,
        payments: Optional<List<Double>?>,
        userId: Int
    ): String? {
        return apolloClient
            .mutation(SavePaymentListMutation(
                orderWithDebtIds,
                payments,
                userId
            ))
            .execute()
            .data
            ?.registerPayments
            ?.message
    }
}