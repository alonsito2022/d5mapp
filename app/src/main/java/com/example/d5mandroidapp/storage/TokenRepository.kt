package com.example.d5mandroidapp.storage

interface TokenRepository {
    suspend fun setToken(token: String?)
    fun getToken(): String?
    fun clearToken()
    suspend fun setRefreshToken(token: String?)
    fun getRefreshToken(): String?
    fun clearRefreshToken()
}