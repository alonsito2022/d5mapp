package com.example.d5mandroidapp.storage

import android.content.Context

class TokenRepositoryImpl(private val context: Context) : TokenRepository {

    override suspend fun setToken(token: String?) {
        with(context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit()) {
            putString("token", token)
            apply()
        }
    }

    override fun getToken(): String? {
        return context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).getString("token", null)
    }

    override fun clearToken() {
        with(context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit()) {
            remove("token")
            apply()
        }
    }

    override suspend fun setRefreshToken(token: String?) {
        with(context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit()) {
            putString("refreshToken", token)
            apply()
        }
    }

    override fun getRefreshToken(): String? {
        return context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).getString("refreshToken", null)
    }

    override fun clearRefreshToken() {
        with(context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit()) {
            remove("refreshToken")
            apply()
        }
    }
}