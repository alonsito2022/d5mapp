package com.example.d5mandroidapp.data.network

import com.example.d5mandroidapp.data.models.ProductGift
import com.example.d5mandroidapp.data.models.ProductTariff
import com.apollographql.apollo3.api.Optional
import com.example.d5mandroidapp.data.models.DiscountGift

interface ProductTariffApiClient {
    suspend fun getProductTariffs(): List<ProductTariff>
    suspend fun getProductTariffsFiltered(searchQuery: String, searchBy: String, typeTradeId: Int): List<ProductTariff>
    suspend fun getProductGiftsByProductPurchasedId(productPurchasedId: Int, typeTradeId: Int, quantity: Double): List<ProductGift>
    suspend fun getProductGiftsByProductPurchasedList(productIds: Optional<List<Int>?>, quantities: Optional<List<Int>?>, subtotals: Optional<List<Double>?>, typeTradeId: Int): List<ProductGift>
    suspend fun getDiscountGiftsByProductPurchasedList(productIds: Optional<List<Int>?>, quantities: Optional<List<Int>?>, subtotals: Optional<List<Double>?>, typeTradeId: Int): List<DiscountGift>
}