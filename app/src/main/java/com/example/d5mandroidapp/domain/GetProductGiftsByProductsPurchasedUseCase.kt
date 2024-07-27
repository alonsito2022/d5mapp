package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.ProductGift
import com.example.d5mandroidapp.data.network.ProductTariffApiClient
import com.apollographql.apollo3.api.Optional

class GetProductGiftsByProductsPurchasedUseCase(
    private val productTariffApiClient: ProductTariffApiClient
) {
    suspend fun execute(
        productIds: Optional<List<Int>?>, quantities: Optional<List<Int>?>, subtotals: Optional<List<Double>?>, typeTradeId: Int): List<ProductGift>{
        return productTariffApiClient.getProductGiftsByProductPurchasedList(productIds, quantities, subtotals, typeTradeId).sortedBy { it.productGiftId }
    }
}