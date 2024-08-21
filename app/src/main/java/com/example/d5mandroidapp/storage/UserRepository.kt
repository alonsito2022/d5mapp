package com.example.d5mandroidapp.storage

interface UserRepository {
    fun setUserId(id: String?)
    fun getUserId(): String?
    fun setUserEmail(email: String?)
    fun getUserEmail(): String?
    fun clearUserId()
    fun clearUserEmail()
}