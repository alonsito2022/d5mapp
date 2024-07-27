package com.example.d5mandroidapp.apollo
import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl(ApolloConfig.BASE_URL)
    .build()
