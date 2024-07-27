package com.example.d5mandroidapp.data.models

data class User (
    val username: String = "",
    val roleReadable: String = "",
    val phone: String = "",
    val document: String = "",
    val address: String = "",
    val firstName: String = "",
    val lastName: String = ""
) {
    constructor() : this(
        "","","","","","",""
    )
}