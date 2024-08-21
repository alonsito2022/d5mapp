package com.example.d5mandroidapp.domain

import com.apollographql.apollo3.api.Optional
import com.example.d5mandroidapp.data.network.PaymentApiClient

class SavePaymentListUseCase(
    private val paymentApiClient: PaymentApiClient
) {
    suspend fun execute(orderWithDebtIds: Optional<List<Int>?>, payments: Optional<List<Double>?>, userId: Int): String? {
        return paymentApiClient.savePaymentList(orderWithDebtIds, payments, userId)
    }
}