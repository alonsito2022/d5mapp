package com.example.d5mandroidapp.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.example.d5mandroidapp.LoginMutation
import com.example.d5mandroidapp.VerifyTokenMutation
import com.example.d5mandroidapp.apollo.apolloClient
import com.example.d5mandroidapp.data.states.AuthErrorType
import com.example.d5mandroidapp.data.states.AuthState
import com.example.d5mandroidapp.storage.TokenRepository
import com.example.d5mandroidapp.storage.UserRepository
import com.example.d5mandroidapp.utils.JWTUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            // Validación de campos
            if (email.isBlank() || password.isBlank()) {
                _authState.update {
                    AuthState.Error(
                        message = "Por favor, completa todos los campos",
                        errorType = AuthErrorType.EMPTY_FIELDS
                    )
                }
                return@launch
            }

            // Validación de formato de email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _authState.update {
                    AuthState.Error(
                        message = "Por favor, ingresa un correo electrónico válido",
                        errorType = AuthErrorType.INVALID_EMAIL_FORMAT
                    )
                }
                return@launch
            }

            _authState.update { AuthState.Loading }

            try {
                val response = apolloClient.mutation(
                    LoginMutation(email = email, password = password)
                ).execute()

                if (response.hasErrors()) {
                    val errorMessage = response.errors?.firstOrNull()?.message
                        ?: "Error desconocido al iniciar sesión"
                    
                    val errorType = when {
                        errorMessage.contains("credentials", ignoreCase = true) ||
                        errorMessage.contains("invalid", ignoreCase = true) ||
                        errorMessage.contains("incorrect", ignoreCase = true) -> {
                            AuthErrorType.INVALID_CREDENTIALS
                        }
                        else -> AuthErrorType.SERVER_ERROR
                    }

                    _authState.update {
                        AuthState.Error(
                            message = "Error al iniciar sesión: $errorMessage",
                            errorType = errorType
                        )
                    }
                    Log.w("AuthViewModel", "Error en login: $errorMessage")
                    return@launch
                }

                val token = response.data?.tokenAuth?.token
                val refreshToken = response.data?.tokenAuth?.refreshToken

                if (token == null || token.isEmpty()) {
                    _authState.update {
                        AuthState.Error(
                            message = "No se pudo iniciar sesión: el servidor no devolvió un token válido",
                            errorType = AuthErrorType.SERVER_ERROR
                        )
                    }
                    Log.w("AuthViewModel", "Token nulo o vacío en respuesta")
                    return@launch
                }

                // Guardar tokens
                tokenRepository.setToken(token)
                tokenRepository.setRefreshToken(refreshToken)

                // Guardar información del usuario
                val user = response.data?.tokenAuth?.user
                if (user != null) {
                    userRepository.setUserId(user.id)
                    userRepository.setUserEmail(user.email)
                }

                _authState.update { AuthState.Success(isAuthenticated = true) }
                Log.i("AuthViewModel", "Login exitoso para: $email")

            } catch (e: ApolloException) {
                Log.e("AuthViewModel", "Error de red en login", e)
                _authState.update {
                    AuthState.Error(
                        message = "No se pudo conectar al servidor. Verifica tu conexión a internet.",
                        errorType = AuthErrorType.NETWORK_ERROR
                    )
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error inesperado en login", e)
                _authState.update {
                    AuthState.Error(
                        message = "Ocurrió un error inesperado. Por favor, intenta nuevamente.",
                        errorType = AuthErrorType.UNKNOWN_ERROR
                    )
                }
            }
        }
    }

    fun checkAuthenticationStatus() {
        viewModelScope.launch {
            _authState.update { AuthState.Loading }

            try {
                val token = tokenRepository.getToken()

                if (token == null || token.isEmpty()) {
                    _authState.update {
                        AuthState.Success(isAuthenticated = false)
                    }
                    return@launch
                }

                // Verificar expiración del token
                try {
                    val expirationTimestamp = JWTUtils.getExpirationTime(token)
                    val expirationDate = Date(expirationTimestamp * 1000)
                    val timeRemaining = expirationDate.time - System.currentTimeMillis()

                    if (timeRemaining <= 0) {
                        // Token expirado
                        clearAuthData()
                        _authState.update {
                            AuthState.Error(
                                message = "Tu sesión ha expirado. Por favor, inicia sesión nuevamente.",
                                errorType = AuthErrorType.TOKEN_EXPIRED
                            )
                        }
                        return@launch
                    }

                    // Verificar token con el servidor
                    val response = apolloClient.mutation(
                        VerifyTokenMutation(token = token)
                    ).execute()

                    if (response.hasErrors()) {
                        val errorMessage = response.errors?.firstOrNull()?.message
                            ?: "Token inválido"
                        Log.w("AuthViewModel", "Error verificando token: $errorMessage")
                        clearAuthData()
                        _authState.update {
                            AuthState.Error(
                                message = "Tu sesión no es válida. Por favor, inicia sesión nuevamente.",
                                errorType = AuthErrorType.TOKEN_INVALID
                            )
                        }
                        return@launch
                    }

                    val verifyToken = response.data?.verifyToken
                    if (verifyToken == null) {
                        clearAuthData()
                        _authState.update {
                            AuthState.Error(
                                message = "No se pudo verificar tu sesión. Por favor, inicia sesión nuevamente.",
                                errorType = AuthErrorType.TOKEN_INVALID
                            )
                        }
                        return@launch
                    }

                    _authState.update { AuthState.Success(isAuthenticated = true) }
                    Log.i("AuthViewModel", "Token verificado exitosamente")

                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Error verificando token JWT", e)
                    // Si hay error al decodificar el JWT, intentar verificar con el servidor
                    try {
                        val response = apolloClient.mutation(
                            VerifyTokenMutation(token = token)
                        ).execute()

                        if (response.hasErrors() || response.data?.verifyToken == null) {
                            clearAuthData()
                            _authState.update {
                                AuthState.Error(
                                    message = "Tu sesión no es válida. Por favor, inicia sesión nuevamente.",
                                    errorType = AuthErrorType.TOKEN_INVALID
                                )
                            }
                        } else {
                            _authState.update { AuthState.Success(isAuthenticated = true) }
                        }
                    } catch (ex: Exception) {
                        Log.e("AuthViewModel", "Error verificando token con servidor", ex)
                        clearAuthData()
                        _authState.update {
                            AuthState.Error(
                                message = "No se pudo verificar tu sesión. Por favor, inicia sesión nuevamente.",
                                errorType = AuthErrorType.TOKEN_INVALID
                            )
                        }
                    }
                }

            } catch (e: ApolloException) {
                Log.e("AuthViewModel", "Error de red verificando autenticación", e)
                // En caso de error de red, asumir que no está autenticado
                _authState.update {
                    AuthState.Success(isAuthenticated = false)
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error inesperado verificando autenticación", e)
                _authState.update {
                    AuthState.Success(isAuthenticated = false)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            clearAuthData()
            _authState.update { AuthState.Success(isAuthenticated = false) }
        }
    }

    fun clearError() {
        _authState.update { AuthState.Idle }
    }

    private fun clearAuthData() {
        tokenRepository.clearToken()
        tokenRepository.clearRefreshToken()
        userRepository.clearUserId()
        userRepository.clearUserEmail()
    }
}

