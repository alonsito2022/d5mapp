package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.ProductTariff
import com.example.d5mandroidapp.data.network.ProductTariffApiClient

class GetProductTariffsFilteredUseCase(
    private val productTariffApiClient: ProductTariffApiClient
) {
    suspend fun execute(searchQuery: String, searchBy: String, typeTradeId: Int): List<ProductTariff>{
        return productTariffApiClient.getProductTariffsFiltered(searchQuery, searchBy, typeTradeId).sortedBy { it.id }
    }
}