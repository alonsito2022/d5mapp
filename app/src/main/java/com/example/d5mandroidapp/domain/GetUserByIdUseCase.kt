package com.example.d5mandroidapp.domain

import com.example.d5mandroidapp.data.models.User
import com.example.d5mandroidapp.data.network.UserApiClient

class GetUserByIdUseCase(
    private val userApiClient: UserApiClient
) {
    suspend fun execute(userId: String): User?{
        return userApiClient.getUserById(userId)
    }
}