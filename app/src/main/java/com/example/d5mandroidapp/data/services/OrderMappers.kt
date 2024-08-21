package com.example.d5mandroidapp.data.services
import com.example.d5mandroidapp.GeneratedOrderListQuery
import com.example.d5mandroidapp.data.models.Message
import com.example.d5mandroidapp.data.models.Operation
import com.example.d5mandroidapp.data.models.Order

//fun SaveOrderMutation.CreateOrder.toOrder(order: Order): Order {
//    return Order(
//        order.userId,
//        order.clientId,
//        order.addressId,
//        order.typeTradeId,
//        order.dailyRouteId,
//        order.selectedPaymentType,
//        order.selectedDocumentType,
//        order.productTariffIds,
//        order.quantities,
//        order.bonusIds,
//        order.prices,
//    )
//}
fun GeneratedOrderListQuery.SalesByEmployee.toOperation(): Operation {
    return Operation(
        id?:0,
        (operationDate?:"").toString(),
        (operationStatusName?:"").toString(),
        (paymentTypeReadable?:"").toString(),
        clientNames?:"",
        documentTypeToGenerateReadable?:"",
        baseCost?:0.0,
        igvCost?:0.0,
        totalSale?:0.0,
        baseAmountPerception?:0.0

    )
}