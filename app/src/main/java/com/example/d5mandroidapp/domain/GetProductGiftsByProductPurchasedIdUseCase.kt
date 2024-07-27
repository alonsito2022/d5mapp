package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.ProductGift
import com.example.d5mandroidapp.data.network.ProductTariffApiClient

class GetProductGiftsByProductPurchasedIdUseCase(
    private val productTariffApiClient: ProductTariffApiClient
) {
    suspend fun execute(productPurchasedId: Int, typeTradeId: Int, quantity: Double): List<ProductGift>{
        return productTariffApiClient.getProductGiftsByProductPurchasedId(productPurchasedId, typeTradeId, quantity).sortedBy { it.productGiftId }
    }
}