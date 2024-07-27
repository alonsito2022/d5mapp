package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.VerifiedToken
import com.example.d5mandroidapp.data.network.UserApiClient

class VerifyTokenUseCase(
    private val userApiClient: UserApiClient
) {
    suspend fun execute(token: String): VerifiedToken?{
        return userApiClient.verifyToken(token)
    }
}