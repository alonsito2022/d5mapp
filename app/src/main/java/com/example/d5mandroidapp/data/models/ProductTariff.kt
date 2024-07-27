package com.example.d5mandroidapp.data.models

data class ProductTariff (
    val id: Int = 0,
    val productID: Int = 0,
    val productName: String = "",
    val productCode: String = "",
    val productActiveType: String = "",
    val unitId: Int = 0,
    val unitName: String = "",
    val cashSalePrice: Any? = null,
    val creditSalePrice: Any,
    var isSelected: Boolean,
    val cashSalePriceWithoutIgv: Double,
    val creditSalePriceWithoutIgv: Double,
    val stock: Double,
    var quantity: Double,
    val typeTradeId: Int = 0,
    val typeTradeName: String = "",
    val exemptFromIgv: Boolean = false,
    val subjectPerception: Boolean = false
)

