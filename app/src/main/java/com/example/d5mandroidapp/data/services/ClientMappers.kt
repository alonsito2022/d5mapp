package com.example.d5mandroidapp.data.services
import com.example.d5mandroidapp.GetClientDebtQuery
import com.example.d5mandroidapp.OrderWithDebtListByClientQuery
import com.example.d5mandroidapp.data.models.Client
import com.example.d5mandroidapp.data.models.OrderWithDebt

fun GetClientDebtQuery.ClientWithDebt.toClient(): Client {
    return Client(
        id ?: 0,
        code ?: "",
        names ?: "",
        phone ?: "",
        documentType ?: "",
        documentTypeReadable ?: "",
        documentNumber ?: "",
        observation ?: "",
        isEnabled ?: false,
        debt ?: 0.0,
        typeTradeId ?: 0,
        typeTradeName ?: "",
        creditLine ?: "",
        isBlocked ?: false,
        isSuspended ?: false,
        isObserved ?: false
    )
}

fun OrderWithDebtListByClientQuery.AccountStatusByClient.toOrderWithDebt(): OrderWithDebt{
    return OrderWithDebt(
        id?: 0,
        documentNumber?: "",
        documentTypeReadable?: "",
        totalSale?: 0.0,
        totalPaid?: 0.0,
        totalPending?: 0.0,
        expirationDays?: 0.0,
        shippingDate?: "",
        operationDate?: "",
        0.0,
        false
    )
}