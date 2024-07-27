package com.example.d5mandroidapp.data.services

import com.example.d5mandroidapp.GetUserQuery
import com.example.d5mandroidapp.VerifyTokenMutation
import com.example.d5mandroidapp.data.models.User
import com.example.d5mandroidapp.data.models.VerifiedToken

fun GetUserQuery.UserById.toUser(): User {
    return User(
        username ?: "",
        roleReadable ?: "",
        phone ?: "",
        document ?: "",
        address ?: "",
        firstName ?: "",
        lastName ?: ""
    )
}

fun VerifyTokenMutation.VerifyToken.toVerifiedToken(): VerifiedToken{
    return VerifiedToken(
        payload
    )
}