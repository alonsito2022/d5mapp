package com.example.d5mandroidapp.data.services

import com.example.d5mandroidapp.ClientAddressesListQuery
import com.example.d5mandroidapp.data.models.SimpleAddress

fun ClientAddressesListQuery.AddressesByClientId.toSimpleAddress(): SimpleAddress {
    return SimpleAddress(
        id ?: 0,
        address ?: "",
        latitude ?: 0.0,
        longitude ?: 0.0,
        districtId ?: "",
        districtName ?: "",
        provinceId ?: "",
        provinceName ?: "",
        departmentId ?: "",
        departmentName ?: ""
    )
}