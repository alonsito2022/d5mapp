package com.example.d5mandroidapp.storage

import android.content.Context

class UserRepositoryImpl(private val context: Context) : UserRepository {
    override fun setUserId(id: String?) {
        with(context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit()) {
            putString("userId", id)
            apply()
        }
    }

    override fun getUserId(): String? {
        return context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).getString("userId", null)

    }

    override fun setUserEmail(email: String?) {
        with(context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit()) {
            putString("userEmail", email)
            apply()
        }
    }

    override fun getUserEmail(): String? {
        return context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).getString("userEmail", null)
    }

    override fun clearUserId() {
        with(context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit()) {
            remove("userId")
            apply()
        }
    }

    override fun clearUserEmail() {
        with(context.applicationContext.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit()) {
            remove("userEmail")
            apply()
        }
    }
}