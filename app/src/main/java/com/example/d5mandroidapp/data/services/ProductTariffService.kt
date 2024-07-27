package com.example.d5mandroidapp.data.services

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.d5mandroidapp.ProductTariffFilteredListQuery
import com.example.d5mandroidapp.ProductTariffListQuery
import com.example.d5mandroidapp.data.models.ProductGift
import com.example.d5mandroidapp.data.models.ProductTariff
import com.example.d5mandroidapp.data.network.ProductTariffApiClient
import com.example.d5mandroidapp.ProductGiftListByProductPurchasedQuery
import com.example.d5mandroidapp.ProductGiftListByProductPurchasedListQuery
import com.example.d5mandroidapp.DiscountGiftListByProductPurchasedListQuery
import com.example.d5mandroidapp.data.models.DiscountGift

class ProductTariffService(private val apolloClient: ApolloClient): ProductTariffApiClient {

    override suspend fun getProductTariffs(): List<ProductTariff> {
        return apolloClient
            .query(ProductTariffListQuery())
            .execute()
            .data
            ?.allProductTariffs
            ?.map { it!!.toProductTariff() }
            ?: emptyList()
    }

    override suspend fun getProductTariffsFiltered(searchQuery: String, searchBy: String, typeTradeId: Int): List<ProductTariff> {
        return apolloClient
            .query(ProductTariffFilteredListQuery(searchQuery, searchBy, typeTradeId))
            .execute()
            .data
            ?.searchProductTariffsByCriteria
            ?.map { it!!.toProductTariffFiltered() }
            ?: emptyList()
//        return emptyList()
    }

    override suspend fun getProductGiftsByProductPurchasedId(
        productPurchasedId: Int,
        typeTradeId: Int,
        quantity: Double
    ): List<ProductGift> {
        return apolloClient
            .query(ProductGiftListByProductPurchasedQuery(productPurchasedId, typeTradeId, quantity))
            .execute()
            .data
            ?.productGiftsByProductPurchasedId
            ?.map { it!!.toProductGift() }
            ?: emptyList()
    }

    override suspend fun getProductGiftsByProductPurchasedList(
        productIds: Optional<List<Int>?>,
        quantities: Optional<List<Int>?>,
        subtotals: Optional<List<Double>?>,
        typeTradeId: Int
    ): List<ProductGift> {
        return apolloClient
            .query(ProductGiftListByProductPurchasedListQuery(
                productIds,
                quantities,
                subtotals,
                typeTradeId))
            .execute()
            .data
            ?.productGiftsByProductsPurchased
            ?.map { it!!.toProductGift2() }
            ?: emptyList()
    }

    override suspend fun getDiscountGiftsByProductPurchasedList(
        productIds: Optional<List<Int>?>,
        quantities: Optional<List<Int>?>,
        subtotals: Optional<List<Double>?>,
        typeTradeId: Int
    ): List<DiscountGift> {
        return apolloClient
            .query(DiscountGiftListByProductPurchasedListQuery(
                productIds,
                quantities,
                subtotals,
                typeTradeId))
            .execute()
            .data
            ?.discountGiftsByProductsPurchased
            ?.map { it!!.toDiscountGift() }
            ?: emptyList()
    }

}