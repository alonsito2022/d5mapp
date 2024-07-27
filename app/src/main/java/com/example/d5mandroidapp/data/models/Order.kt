package com.example.d5mandroidapp.data.models

import com.apollographql.apollo3.api.Optional

data class Order(
    val userId: String = "",
    val clientId: String = "",
    val addressId: String = "",
    val typeTradeId: String = "",
    val dailyRouteId: String = "",
    val selectedPaymentType: String = "",
    val selectedDocumentType: String = "",
    val productTariffIds: Optional<List<Int>?>,
    val quantities: Optional<List<Int>?>,
    val bonusIds: Optional<List<Int>?>,
    val prices: Optional<List<Double>?>,
    val discountPercentages: Optional<List<Double>?>,
    val discountAmounts: Optional<List<Double>?>,
    val baseCost: Double = 0.0,
    val igvCost: Double = 0.0,
    val totalSale: Double = 0.0,
    val freeCost: Double = 0.0,
    val discountCost: Double = 0.0,
    val exoneratedCost: Double = 0.0,
    val perceptionCost: Double = 0.0,
    val totalToPay: Double = 0.0
)