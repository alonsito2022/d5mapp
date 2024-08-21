package com.example.d5mandroidapp.domain
import com.example.d5mandroidapp.data.models.Operation
import com.example.d5mandroidapp.data.network.OrderApiClient

class GetGeneratedOrderUseCase(
    private val orderApiClient: OrderApiClient
) {
    suspend fun execute(searchDate: String, employeeId: Int, operationStatus: String): List<Operation>{
        return orderApiClient.getGeneratedOrderList(searchDate, employeeId, operationStatus).sortedBy { it.id }
    }
}