package com.example.d5mandroidapp.apollo

import android.content.Context
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.d5mandroidapp.storage.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApolloClientUpdater @Inject constructor(
    private val tokenRepository: TokenRepository,
    @ApplicationContext private val context: Context
)  {
    private val _apolloClient = MutableStateFlow<ApolloClient?>(null)
    val apolloClient: StateFlow<ApolloClient?> = _apolloClient.asStateFlow()

    init {
        updateClient()
    }

    fun updateClient() {
        val token = tokenRepository.getToken()
        Log.d("D5MAP2", "Updating ApolloClient with token: $token")
        val authorizationInterceptor = AuthorizationInterceptor(token ?: "")
        val newClient = ApolloClient.Builder()
            .serverUrl(ApolloConfig.BASE_URL)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(authorizationInterceptor)
                    .build()
            )
            .build()
        _apolloClient.value = newClient
    }
}