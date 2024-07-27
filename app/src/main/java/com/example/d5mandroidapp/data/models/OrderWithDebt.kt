package com.example.d5mandroidapp.data.models

data class OrderWithDebt(
    val id: Int = 0,
    val documentNumber: String = "",
    val documentTypeReadable: String = "",
    val totalSale: Double = 0.0,
    val totalPaid: Double = 0.0,
    val totalPending: Double = 0.0,
    val expirationDays: Double = 0.0,
    val shippingDate: Any?,
    val operationDate: Any?,

    var totalToPaid: Double = 0.0,
    var isSelected: Boolean,
)