package com.example.d5mandroidapp.domain

import com.apollographql.apollo3.api.Optional
import com.example.d5mandroidapp.data.models.DiscountGift
import com.example.d5mandroidapp.data.network.ProductTariffApiClient

class GetDiscountGiftsByProductsPurchasedUseCase(
    private val productTariffApiClient: ProductTariffApiClient
) {
    suspend fun execute(
        productIds: Optional<List<Int>?>, quantities: Optional<List<Int>?>, subtotals: Optional<List<Double>?>, typeTradeId: Int): List<DiscountGift>{
        return productTariffApiClient.getDiscountGiftsByProductPurchasedList(productIds, quantities, subtotals, typeTradeId)
    }
}