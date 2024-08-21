package com.example.d5mandroidapp.data.models
data class Operation(
    val id: Int = 0,
    val operationDate: String = "",
    val operationStatusName: String = "",
    val paymentTypeReadable: String = "",
    val clientNames: String = "",
    val documentTypeToGenerateReadable: String = "",
    val baseCost: Double = 0.0,
    val igvCost: Double = 0.0,
    val totalSale: Double = 0.0,
    val baseAmountPerception: Double = 0.0
) {
}