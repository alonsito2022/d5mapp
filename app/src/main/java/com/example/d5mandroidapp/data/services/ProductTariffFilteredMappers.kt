package com.example.d5mandroidapp.data.services

import com.example.d5mandroidapp.ProductGiftListByProductPurchasedListQuery
import com.example.d5mandroidapp.ProductTariffFilteredListQuery
import com.example.d5mandroidapp.ProductGiftListByProductPurchasedQuery
import com.example.d5mandroidapp.DiscountGiftListByProductPurchasedListQuery
import com.example.d5mandroidapp.data.models.DiscountGift
import com.example.d5mandroidapp.data.models.ProductGift
import com.example.d5mandroidapp.data.models.ProductTariff

fun ProductTariffFilteredListQuery.SearchProductTariffsByCriterium.toProductTariffFiltered(): ProductTariff {
    return ProductTariff(
        id ?: 0,
        productId ?: 0,
        productName ?: "",
        productCode ?: "",
        productActiveType ?: "",
        unitId ?: 0,
        unitName?:"",
        cashSalePrice?: "",
        creditSalePrice?: "",
        isSelected ?: false,
        cashSalePriceWithoutIgv ?: 0.0,
        creditSalePriceWithoutIgv ?: 0.0,
        stock ?: 0.0,
        quantity?: 0.0,
        typeTradeId ?: 0,
        typeTradeName?: "",
        exemptFromIgv?: false,
        subjectPerception?: false
    )
}

fun ProductGiftListByProductPurchasedQuery.ProductGiftsByProductPurchasedId.toProductGift(): ProductGift {
    return ProductGift(
        productGiftId?:0,
        productGiftCode?:"",
        productGiftName?:"",
        cashPrice?:0.0,
        totalQuantityGift?:0.0,
        productGiftActiveType?:"",
        productGiftTariffId?:0,
        unitGiftId?:0,
        unitGiftName?:"",
        unitGiftDescription?:"",
        bonusId?:0,
        bonusConjunction?:0
    )
}
fun ProductGiftListByProductPurchasedListQuery.ProductGiftsByProductsPurchased.toProductGift2(): ProductGift {
    return ProductGift(
        productGiftId?:0,
        productGiftCode?:"",
        productGiftName?:"",
        cashPrice?:0.0,
        totalQuantityGift?:0.0,
        productGiftActiveType?:"",
        productGiftTariffId?:0,
        unitGiftId?:0,
        unitGiftName?:"",
        unitGiftDescription?:"",
        bonusId?:0,
        bonusConjunction?:0
    )
}
fun DiscountGiftListByProductPurchasedListQuery.DiscountGiftsByProductsPurchased.toDiscountGift(): DiscountGift {
    return DiscountGift(
        productIds?: listOf(),
        totalDiscountGift?:0.0,
        bonusId?:0,
        bonusConjunction?:0
    )
}