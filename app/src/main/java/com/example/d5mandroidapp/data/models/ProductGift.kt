package com.example.d5mandroidapp.data.models

data class ProductGift(
    val productGiftId: Int = 0,
    val productGiftCode: String = "",
    val productGiftName: String = "",
    val cashPrice: Double= 0.0,
    val totalQuantityGift: Double= 0.0,
    val productGiftActiveType: String = "",
    val productGiftTariffId: Int = 0,
    val unitGiftId: Int = 0,
    val unitGiftName: String = "",
    val unitGiftDescription: String = "",
    val bonusId: Int = 0,
    val bonusConjunction: Int = 0
)