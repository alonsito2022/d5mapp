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
        payments: Optional<List<Double>?>
    ): String? {
        return apolloClient
            .mutation(SavePaymentListMutation(
                orderWithDebtIds,
                payments
            ))
            .execute()
            .data
            ?.registerPayments
            ?.message
    }
}