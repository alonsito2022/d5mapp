package com.example.d5mandroidapp.data.repositories

//class ProductTariffRepository(private val apolloClient: ApolloClient) {
//    suspend fun getAllProductTariffs(): ProductTariffListResponse {
//        val query = ProductTariffListQuery()
//
//        try {
//            val response = apolloClient.query(query)
//            return response.dataAssertNoErrors
//        } catch (error: ApolloException) {
//            throw ProductTariffsFetchingError("Error fetching product tariffs", error)
//        }
//    }
//}