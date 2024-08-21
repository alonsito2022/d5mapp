package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.VerifiedToken
import com.example.d5mandroidapp.data.network.UserApiClient

class RemoveRefreshTokenUseCase(
    private val userApiClient: UserApiClient
) {
    suspend fun execute(userId: Int): Boolean?{
        return userApiClient.removeRefreshToken(userId)
    }
}