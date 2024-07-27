package com.example.d5mandroidapp.data.services
import com.example.d5mandroidapp.ProductTariffListQuery
import com.example.d5mandroidapp.data.models.ProductTariff

fun ProductTariffListQuery.AllProductTariff.toProductTariff(): ProductTariff {
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
        stock?: 0.0,
        quantity?: 0.0,
    )
}