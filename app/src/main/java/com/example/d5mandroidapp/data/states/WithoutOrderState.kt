package com.example.d5mandroidapp.data.states;

data class WithoutOrderState(
    val userId: Int = 0,
    val observation: String = "CLIENTE NO TIENE DINERO",
    val dailyRouteId: String = "",
    val message: String = "",
    val success: Boolean = false,
    val error: Boolean = false,
    val isOpenConfirmDialog: Boolean = false,
    val selectReason: Boolean = false,
    val reasonList: List<String> = listOf(
        "CLIENTE NO TIENE DINERO",
        "CONDICION DE VENTA EQUIVOCADA",
        "DEFECTO DE CALIDAD",
        "DIRECCION EQUIVOCADA",
        "DUEÑO AUSENTE",
        "FALTO TIEMPO PARA REPARTO",
        "FUERA DE ZONA",
        "LOCAL CERRADO",
        "NO HIZO PEDIDO",
        "NO SALIO ALMACEN",
        "PEDIDO DUPLICADO",
        "PEDIDO INCOMPLETO",
        "PEDIDO NO CONFORME"
    )
)