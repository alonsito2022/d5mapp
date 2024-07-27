package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.ProductTariff
import com.example.d5mandroidapp.data.network.ProductTariffApiClient

class GetProductTariffsUseCase(
    private val productTariffApiClient: ProductTariffApiClient
) {
    suspend fun execute(): List<ProductTariff>{
        return productTariffApiClient.getProductTariffs().sortedBy { it.id }
    }
}