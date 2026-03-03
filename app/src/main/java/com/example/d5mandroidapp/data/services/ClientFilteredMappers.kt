package com.example.d5mandroidapp.data.services

import com.example.d5mandroidapp.ClientFilteredListQuery
import com.example.d5mandroidapp.GetClientsByUserQuery
import com.example.d5mandroidapp.data.models.Client

fun ClientFilteredListQuery.SearchClientsByCriterium.toClientFiltered(): Client {
    return Client(
        id?: 0,
        code?: "",
        names?: "",
        phone?: "",
        documentType?: "",
        documentTypeReadable?: "",
        documentNumber?: "",
        observation?: "",
        isEnabled?: false,
        debt?: 0.0,
        typeTradeId?: 0,
        typeTradeName?: "",
        creditLine ?: "",
        isBlocked ?: false,
        isSuspended ?: false,
        isObserved ?: false
    )
}

fun GetClientsByUserQuery.AllClientsByUserId.toClient(): Client {
    return Client(
        id?: 0,
        code?: "",
        names?: "",
        phone?: "",
        documentType?: "",
        documentTypeReadable?: "",
        documentNumber?: "",
        observation?: "",
        isEnabled?: false,
        debt?: 0.0,
        typeTradeId?: 0,
        typeTradeName?: "",
        creditLine ?: "",
        isBlocked ?: false,
        isSuspended ?: false,
        isObserved ?: false,
        visitDatesCurrentWeek = visitDatesCurrentWeek?.mapNotNull { it } ?: emptyList()
    )
}