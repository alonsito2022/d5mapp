package com.example.d5mandroidapp.data.network

import com.apollographql.apollo3.api.Optional

interface PaymentApiClient {
    suspend fun savePaymentList(orderWithDebtIds: Optional<List<Int>?>, payments:Optional<List<Double>?>): String?
}