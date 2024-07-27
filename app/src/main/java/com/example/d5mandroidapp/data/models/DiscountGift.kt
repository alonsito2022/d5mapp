package com.example.d5mandroidapp.data.models

data class DiscountGift(
    val productIds: List<Int>,
    val totalDiscountGift: Double= 0.0,
    val bonusId: Int = 0,
    val bonusConjunction: Int = 0,
)