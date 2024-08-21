package com.example.d5mandroidapp.data.services

import com.apollographql.apollo3.ApolloClient
import com.example.d5mandroidapp.GetUserQuery
import com.example.d5mandroidapp.LogoutMutation
import com.example.d5mandroidapp.VerifyTokenMutation
import com.example.d5mandroidapp.data.models.Payload
import com.example.d5mandroidapp.data.models.User
import com.example.d5mandroidapp.data.models.VerifiedToken
import com.example.d5mandroidapp.data.network.UserApiClient

class UserService(private val apolloClient: ApolloClient): UserApiClient {
    override suspend fun getUserById(userId: String): User? {
        return apolloClient
            .query(GetUserQuery(userId))
            .execute()
            .data
            ?.userById
            ?.toUser()
    }

    override suspend fun verifyToken(token: String): VerifiedToken? {
        return apolloClient
            .mutation(VerifyTokenMutation(
                token=token
            ))
            .execute()
            .data
            ?.verifyToken?.toVerifiedToken()
//        return null
    }

    override suspend fun removeRefreshToken(userId: Int): Boolean? {
        return apolloClient
            .mutation(LogoutMutation(userId))
            .execute()
            .data
            ?.logout?.success
    }
}