package com.example.d5mandroidapp.data.states

import com.example.d5mandroidapp.data.models.OperationDetail
import com.example.d5mandroidapp.data.models.Operation

data class GeneratedOrderState (
    var operations: List<Operation> = emptyList(),
    val isLoading: Boolean = false,
    val message: String = "",
    val visitDate: String = "",
    val success: Boolean = false,
    val error: Boolean = false,
    val userId: Int = 0,
    val gangId: Int = 0
)