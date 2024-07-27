package com.example.d5mandroidapp.data.models

data class OperationDetail(
    var productTariffId: Int = 0,
    var stock: Double = 0.0,
    var cashPrice: Double = 0.0,
    var creditPrice: Double = 0.0,
    var quantity: Int = 0,
    var productID: Int = 0,
    var productName: String = "",
    var productCode: String = "",
    var productActiveType: String = "",
    var cashSubtotal: Double = 0.0,
    var creditSubtotal: Double = 0.0,
    var bonusId: Int = 0,
    var bonusConjunction: Int = 0,

    var percentageDiscount: Double = 0.0,
    var amountDiscount: Double = 0.0,

    var productExemptFromIgv: Boolean = false,
    var productSubjectPerception: Boolean = true
) {
}