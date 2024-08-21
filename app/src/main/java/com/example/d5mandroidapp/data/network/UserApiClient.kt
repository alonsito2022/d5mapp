package com.example.d5mandroidapp.data.network

import com.example.d5mandroidapp.data.models.Payload
import com.example.d5mandroidapp.data.models.User
import com.example.d5mandroidapp.data.models.VerifiedToken

interface UserApiClient {
    suspend fun getUserById(userId: String): User?
    suspend fun verifyToken(token: String): VerifiedToken?
    suspend fun removeRefreshToken(userId: Int): Boolean?
}