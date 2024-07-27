package com.example.d5mandroidapp.data.models

data class SimpleAddress(
    val id: Int = 0,
    val address: String = "",
    var latitude: Double,
    var longitude: Double,
    var districtId: String = "",
    var districtName: String = "",
    var provinceId: String = "",
    var provinceName: String = "",
    var departmentId: String = "",
    var departmentName: String = ""
    ) {
}