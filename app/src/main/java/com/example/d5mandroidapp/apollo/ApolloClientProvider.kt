package com.example.d5mandroidapp.apollo
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.d5mandroidapp.apollo.ApolloConfig
import com.example.d5mandroidapp.apollo.AuthorizationInterceptor
import okhttp3.OkHttpClient

class ApolloClientProvider(val token: String) {
    val apolloClientWithAuthorizationInterceptor: ApolloClient by lazy {
        ApolloClient.Builder()
            .serverUrl(ApolloConfig.BASE_URL)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(AuthorizationInterceptor(token))
                    .build()
            )
            .build()
    }
}