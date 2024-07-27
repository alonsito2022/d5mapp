package com.example.d5mandroidapp.data.states

import com.example.d5mandroidapp.data.models.DiscountGift
import com.example.d5mandroidapp.data.models.OperationDetail
import com.example.d5mandroidapp.data.models.ProductGift
import com.example.d5mandroidapp.data.models.ProductTariff

data class SaleOrderState(
    var productTariffs: List<ProductTariff> = emptyList(),
    var productGifts: List<ProductGift> = emptyList(),
    var discountGifts: List<DiscountGift> = emptyList(),
    var operationDetails: List<OperationDetail> = emptyList(),
    val isLoading: Boolean = false,
    val isOpenDialog: Boolean = false,
    val selectedPaymentType: String = "CONTADO",
    val selectedDocumentType: String = "BOLETA",
    val dailyRouteId: String = "",
    val typeTradeId: String = "",
    val userId: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val addressId: String = "",
    val addressName: String = "",

    val discountCost: Double = 0.0,
    val exoneratedCost: Double = 0.0,
    val baseCost: Double = 0.0,
    val igvCost: Double = 0.0,
    val totalSale: Double = 0.0,
    val freeCost: Double = 0.0,
    val perceptionCost: Double = 0.0,
    val totalToPay: Double = 0.0,

    val message: String = "",
    val success: Boolean = false,
    val error: Boolean = false,
    val exception: Exception = IllegalStateException("No exception occurred")
)
