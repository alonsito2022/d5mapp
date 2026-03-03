package com.example.d5mandroidapp.data.models

data class Client(
    val id: Int,
    val code: String,
    val names: String,
    val phone: String,
    val documentType: Any? = null,
    val documentTypeReadable: String,
    val documentNumber: String,
    val observation: String,
    val isEnabled: Boolean,
    val debt: Double,
    val typeTradeId: Int,
    val typeTradeName: String,

    val creditLine: Any,

    val isBlocked: Boolean,
    val isSuspended: Boolean,
    val isObserved: Boolean,
    val visitDatesCurrentWeek: List<String> = emptyList(),

    ) {
    fun doesMatchSearchQuery(query: String, searchBy: String): Boolean {

        if (searchBy == "names") {
            return names.contains(query, ignoreCase = true)
        }
        else if (searchBy == "documentNumber") {
            return documentNumber.contains(query, ignoreCase = true)
        }
        return false
    }
}