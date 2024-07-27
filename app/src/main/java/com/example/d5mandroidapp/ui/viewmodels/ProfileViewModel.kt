package com.example.d5mandroidapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.d5mandroidapp.data.models.User
import com.example.d5mandroidapp.data.states.UserState
import com.example.d5mandroidapp.domain.GetUserByIdUseCase
import com.example.d5mandroidapp.storage.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    @ApplicationContext context: Context
): ViewModel() {

    private val _state = MutableStateFlow(UserState())
    private val userRepository = UserRepositoryImpl(context)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getUserId()?.let { setUser(it) }
        }
    }

    private suspend fun setUser(userId: String) {
        _state.update { it.copy(
            isLoading = true
        ) }

        _state.update { it.copy(
            selectedUser = getUserByIdUseCase.execute(userId)?: User(),
            isLoading = false
        ) }
    }
}