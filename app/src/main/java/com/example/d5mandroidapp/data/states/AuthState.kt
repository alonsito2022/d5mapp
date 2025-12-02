package com.example.d5mandroidapp.data.states

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val isAuthenticated: Boolean) : AuthState()
    data class Error(val message: String, val errorType: AuthErrorType) : AuthState()
}

enum class AuthErrorType {
    NETWORK_ERROR,
    INVALID_CREDENTIALS,
    TOKEN_EXPIRED,
    TOKEN_INVALID,
    SERVER_ERROR,
    UNKNOWN_ERROR,
    EMPTY_FIELDS,
    INVALID_EMAIL_FORMAT
}

