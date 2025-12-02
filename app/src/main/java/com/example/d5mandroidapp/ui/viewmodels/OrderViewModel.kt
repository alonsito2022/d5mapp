package com.example.d5mandroidapp.ui.viewmodels

import android.content.Context
import android.util.Log
import kotlin.math.round
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.example.d5mandroidapp.data.models.DiscountGift
import com.example.d5mandroidapp.data.models.OperationDetail
import com.example.d5mandroidapp.data.models.Order
import com.example.d5mandroidapp.data.models.ProductTariff
import com.example.d5mandroidapp.data.states.SaleOrderState
import com.example.d5mandroidapp.domain.GetDiscountGiftsByProductsPurchasedUseCase
import com.example.d5mandroidapp.domain.GetProductGiftsByProductPurchasedIdUseCase
import com.example.d5mandroidapp.domain.GetProductGiftsByProductsPurchasedUseCase
import com.example.d5mandroidapp.domain.GetProductTariffsFilteredUseCase
import com.example.d5mandroidapp.domain.SaveOrderUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getProductTariffsFilteredUseCase: GetProductTariffsFilteredUseCase,
    private val getProductGiftsByProductPurchasedIdUseCase: GetProductGiftsByProductPurchasedIdUseCase,
    private val getProductGiftsByProductsPurchasedUseCase: GetProductGiftsByProductsPurchasedUseCase,
    private val getDiscountGiftsByProductsPurchasedUseCase: GetDiscountGiftsByProductsPurchasedUseCase,
    private val saveOrderUseCase: SaveOrderUseCase,
    @ApplicationContext context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(SaleOrderState())
    val state = _state.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchBy = MutableStateFlow("code")
    val searchBy = _searchBy.asStateFlow()

    private fun addOperationDetailItem(productTariff: ProductTariff){
        var cashPrice = 0.0
        var creditPrice = 0.0

        if(productTariff.exemptFromIgv) {
            cashPrice = productTariff.cashSalePrice.toString().toDouble()
            creditPrice = productTariff.creditSalePrice.toString().toDouble()
        }else{
            cashPrice = productTariff.cashSalePrice.toString().toDouble()
            creditPrice = productTariff.creditSalePrice.toString().toDouble()
        }

        val operationDetail = OperationDetail(
            productTariffId = productTariff.id.toInt(),
            stock = productTariff.stock,
            cashPrice = cashPrice,
            creditPrice = creditPrice,
            quantity = 0,
            productID = productTariff.productID,
            productName = productTariff.productName,
            productCode = productTariff.productCode,
            productActiveType = productTariff.productActiveType,
            cashSubtotal = 0.0,
            creditSubtotal = 0.0,
            bonusId = 0,
            bonusConjunction = 0,
            percentageDiscount  = 0.0,
            amountDiscount   = 0.0,
            productExemptFromIgv   = productTariff.exemptFromIgv,
            productSubjectPerception   = productTariff.subjectPerception
        )
        viewModelScope.launch(Dispatchers.Default) {
            productTariff.quantity = 1.0
            _state.update { it.copy(
                operationDetails = it.operationDetails.filter { o -> o.productActiveType != "02" }.toMutableList().apply { add(operationDetail) }
            )}
        }
//        searchDiscounts(state.value.selectedPaymentType)
//        onChangeStatusProductTariff(productTariff.id.toInt() , true)
    }

    fun deleteOperationDetailItemFromTariffList(productTariffId: Int){
        viewModelScope.launch(Dispatchers.Default) {
            _state.update { it.copy(
                operationDetails = state.value.operationDetails.filterNot { it.productTariffId == productTariffId }
            )}
        }

    }
    fun deleteOperationDetailItemFromDetailList(productTariffId: Int){
        val selectedFound = state.value.operationDetails.find { it.productTariffId == productTariffId }
        if (selectedFound != null) {

            viewModelScope.launch {
                _state.update { it.copy(
                    operationDetails = it.operationDetails.toMutableList().apply { remove(selectedFound) }
                )}
            }
//            searchDiscounts(state.value.selectedPaymentType)
//            onChangeStatusProductTariff(productTariffId , false)
        }
//        viewModelScope.launch(Dispatchers.Default) {
//            _state.update { it.copy(
//                operationDetails = state.value.operationDetails.filterNot { it.productTariffId == productTariffId }
//            )}
//        }
        searchDiscounts(state.value.selectedPaymentType)
        onChangeStatusProductTariff(productTariffId, false)
    }

    private fun onChangeStatusProductTariff(productTariffId: Int, status: Boolean) {
//        val searchProductTariff = state.value.productTariffs.find { it.id.toInt() == productTariffId }
//        if (searchProductTariff != null) {
//            val index = state.value.productTariffs.indexOf(searchProductTariff)
//            searchProductTariff.isSelected = status
//
//            viewModelScope.launch {
//                _state.update { it.copy(
//                    productTariffs = it.productTariffs.toMutableList().apply { set(index, searchProductTariff) }
//                )}
//            }
//
//        }
        viewModelScope.launch {
            val updatedProductTariffs = state.value.productTariffs.map {
                if (it.id.toInt() == productTariffId) it.copy(isSelected = status) else it
            }
            _state.update { it.copy(productTariffs = updatedProductTariffs) }
        }
    }

//private fun addOperationDetailItem(productTariff: ProductTariff) {
//    val operationDetail = OperationDetail(
//        productTariffId = productTariff.id.toInt(),
//        stock = productTariff.stock,
//        cashPrice = productTariff.cashSalePrice.toString().toDouble(),
//        creditPrice = productTariff.creditSalePrice.toString().toDouble(),
//        quantity = 0,
//        productID = productTariff.productID,
//        productName = productTariff.productName,
//        productCode = productTariff.productCode,
//        productActiveType = productTariff.productActiveType,
//        cashSubtotal = 0.0,
//        creditSubtotal = 0.0,
//        bonusId = 0,
//        bonusConjunction = 0,
//        percentageDiscount = 0.0,
//        amountDiscount = 0.0,
//        productExemptFromIgv = productTariff.exemptFromIgv,
//        productSubjectPerception = productTariff.subjectPerception
//    )
//    viewModelScope.launch {
//        productTariff.quantity = 1.0
//        _state.update { it.copy(
//            operationDetails = it.operationDetails
//                .filter { o -> o.productActiveType != "02" }
//                .toMutableList().apply { add(operationDetail) }
//        )}
//        searchDiscounts(state.value.selectedPaymentType)
//        onChangeStatusProductTariff(productTariff.id.toInt(), true)
//    }
//}
//
//    fun deleteOperationDetailItem(productTariffId: Int) {
//        viewModelScope.launch {
//            _state.update { it.copy(
//                operationDetails = it.operationDetails.filterNot { it.productTariffId == productTariffId }
//            )}
//            searchDiscounts(state.value.selectedPaymentType)
//            onChangeStatusProductTariff(productTariffId, false)
//        }
//    }
//
//    private fun onChangeStatusProductTariff(productTariffId: Int, status: Boolean) {
//        viewModelScope.launch {
//            val updatedProductTariffs = state.value.productTariffs.map {
//                if (it.id.toInt() == productTariffId) it.copy(isSelected = status) else it
//            }
//            _state.update { it.copy(productTariffs = updatedProductTariffs) }
//        }
//    }

    fun onClickProductTariffItem(productTariff: ProductTariff, status: Boolean) {
        Log.d("D5MAP","productTariff : ${productTariff.productName} current isSelected : ${productTariff.isSelected} to ${status}")
        viewModelScope.launch(Dispatchers.Default) {
            val updatedProductTariffs = state.value.productTariffs.map {
                if (it.id == productTariff.id) it.copy(isSelected = status) else it
            }
            _state.update { it.copy(productTariffs = updatedProductTariffs) }
        }
//        val selectedFound = state.value.operationDetails.find { it.productTariffId == productTariff.id }
//        if (selectedFound != null) {
//
//        }
//        state.value.productTariffs
        if(status) addOperationDetailItem(productTariff)
        else deleteOperationDetailItemFromTariffList(productTariff.id.toInt())

//        state.value.operationDetails.forEach { operationDetail ->
//            Log.d("D5MAP","operationDetail : ${operationDetail.productTariffId} productActiveType : ${operationDetail.productActiveType}")
//        }
//        state.value.productTariffs.forEach { tariff ->
//            Log.d("D5MAP","productTariffId : ${tariff.id} isSelected : ${tariff.isSelected}")
//        }
    }

    fun saveOrder(){

        val productTariffIds = mutableListOf<Int>()
        val quantities = mutableListOf<Int>()
        val bonusIds = mutableListOf<Int>()
        val prices = mutableListOf<Double>()
        val discountPercentages = mutableListOf<Double>()
        val discountAmounts = mutableListOf<Double>()

        state.value.operationDetails.forEach { operationDetail ->
            if (operationDetail.quantity>0){
                productTariffIds.add(operationDetail.productTariffId)
                quantities.add(operationDetail.quantity)
                bonusIds.add(operationDetail.bonusId)
                discountPercentages.add(operationDetail.percentageDiscount)
                discountAmounts.add(operationDetail.amountDiscount)
                if (state.value.selectedPaymentType == "CREDITO")
                    prices.add(operationDetail.creditPrice)
                else prices.add(operationDetail.cashPrice)
            }else{
                Log.d("D5MAP","operationDetail : ${operationDetail.quantity}")
            }
        }

        if (quantities.isNotEmpty()){
            Log.d("D5MAP","quantities.size : ${quantities.size}")
            if (state.value.totalSale>0){
                val order = Order(
                    state.value.userId,
                    state.value.clientId,
                    state.value.addressId,
                    state.value.typeTradeId.toString(),
                    state.value.dailyRouteId,
                    state.value.selectedPaymentType,
                    state.value.selectedDocumentType,
                    Optional.presentIfNotNull(productTariffIds),
                    Optional.presentIfNotNull(quantities),
                    Optional.presentIfNotNull(bonusIds),
                    Optional.presentIfNotNull(prices),
                    Optional.presentIfNotNull(discountPercentages),
                    Optional.presentIfNotNull(discountAmounts),
                    state.value.baseCost,
                    state.value.igvCost,
                    state.value.totalSale,
                    state.value.freeCost,
                    state.value.discountCost,
                    state.value.exoneratedCost,
                    state.value.perceptionCost,
                    state.value.totalToPay
                )
                Log.d("D5MAP2","order ${order}")
                viewModelScope.launch {

                    try {
                        _state.update { it.copy(
                            message = saveOrderUseCase.execute(order).toString(),
                            success = true
                        ) }
                    } catch (exception: Exception) {
                        Log.d("D5MAP2","exception ${exception.message}")
                        _state.update { it.copy(
                            exception = exception,
                            error = true,
                            message = exception.message!!
                        ) }
                    }
                }
            }else{

                viewModelScope.launch {
                    _state.update { it.copy(
                        message = "Verifique precios",
                        error = true
                    ) }
                }
            }



        }else{

            viewModelScope.launch {
                _state.update { it.copy(
                    message = "Verifique cantidades",
                    error = true
                ) }
            }
        }



    }

    fun setSuccessOrError() {
        _state.update { it.copy(
            error = false,
            success = false
        ) }
    }

    fun setUser(userId: String) {
        _state.update { it.copy(
            userId = userId
        ) }
    }

    fun setClient(clientId: String) {
        _state.update { it.copy(
            clientId = clientId
        ) }
    }

    fun setAddress(addressId: Int, addressName: String) {
        _state.update { it.copy(
            addressId = addressId.toString(),
            addressName = addressName
        ) }
    }

    fun setDailyRouteIdAndTypeTradeId(dailyRouteId: String, typeTradeId: Int){
        _state.update { it.copy(
            dailyRouteId = dailyRouteId,
            typeTradeId = typeTradeId.toString()
        ) }
    }

    fun showProductTariffDialog() {
        _state.update { it.copy(
            isOpenDialog = true
        ) }
    }

    fun hideProductTariffDialog() {
        _state.update { it.copy(
            isOpenDialog = false
        ) }
    }

    private fun filterAndSearchByCriteria(text: String){


        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }
            try {
                val productTariffIds =
                    state.value.operationDetails.map { it.productTariffId }.toSet()
                val updatedProductTariffs = getProductTariffsFilteredUseCase.execute(
                    text,
                    searchBy.value,
                    state.value.typeTradeId.toInt()
                ).toMutableList().filter { it.productActiveType == "01" }
                    .filter { it.typeTradeId == state.value.typeTradeId.toInt() }
                    .onEach { productTariff ->
                        if (productTariffIds.contains(productTariff.id.toInt())) {  // Handle potential conversion
                            productTariff.isSelected = true
                        }
                    }


                _state.update {
                    it.copy(
                        productTariffs = updatedProductTariffs,
                        isLoading = false
                    )
                }

                onSearchTextChange("")
            }catch (exception: Exception) {
                // Handle GraphQL request failure here
                _state.update { it.copy(isLoading = false, error = true, exception = exception) } // Update state with error
                // Optionally, display an error message to the user using a UI component or event
            }
        }
    }

    fun onSelectedPaymentType(text: String){

        viewModelScope.launch {
            _state.update{it.copy(
                selectedPaymentType = text
            ) }

            searchDiscounts(text)
        }
    }

    fun onSelectedDocumentType(text: String){
        viewModelScope.launch {
            _state.update{it.copy(
                selectedDocumentType = text
            ) }
        }
    }

    @OptIn(FlowPreview::class)
    fun onSearchTextChange(text: String) {
        _searchText.value = text
//        viewModelScope.launch {
//            _searchText.debounce(1000L)
//                .collect { debouncedText ->
//                    if (debouncedText.length >= 3) {
//
//                        filterAndSearchByCriteria(debouncedText)
//                    }
//                    else{
//                        _state.update { it.copy(productTariffs = emptyList()) }
//                    }
//                }
//        }
    }

    fun onClickButtonSearch(){
        viewModelScope.launch {
            if (searchText.value.length >= 3) {
                filterAndSearchByCriteria(searchText.value)
            }
            else{
                _state.update { it.copy(productTariffs = emptyList()) }
            }
        }
    }

    fun updateSearchBy(newSearchBy: String) {
        _searchBy.value = newSearchBy
    }

    private fun calculateTotalSum(paymentType: String) {
        var baseCost: Double = 0.0
        var igvCost: Double = 0.0
        var totalSale: Double = 0.0
        var freeCost: Double = 0.0
        var discountCost: Double = 0.0
        var exoneratedCost: Double = 0.0
        var totalToPay: Double = 0.0
        var perceptionCost: Double = 0.0

        if (paymentType == "CREDITO"){
            totalSale = round((_state.value.operationDetails.filter { operationDetail -> operationDetail.productActiveType != "02" && !operationDetail.productExemptFromIgv }.sumOf { it.creditSubtotal }) * 100) / 100
            discountCost = round((_state.value.operationDetails.filter { operationDetail -> operationDetail.productActiveType != "02" }.sumOf { it.amountDiscount }) * 100) / 100
            exoneratedCost = round((_state.value.operationDetails.filter { operationDetail -> operationDetail.productActiveType != "02" && operationDetail.productExemptFromIgv }.sumOf { it.creditSubtotal }) * 100) / 100
            perceptionCost = round((_state.value.operationDetails.filter { operationDetail -> operationDetail.productActiveType != "02" && operationDetail.productSubjectPerception }.sumOf { it.creditSubtotal * 0.02 }) * 100) / 100
            freeCost = round((_state.value.operationDetails.filter { operationDetail -> operationDetail.productActiveType == "02" }.sumOf { it.quantity * it.creditPrice }) * 100) / 100
        }else{
            totalSale = round((_state.value.operationDetails.filter { operationDetail -> operationDetail.productActiveType != "02" && !operationDetail.productExemptFromIgv }.sumOf { it.cashSubtotal }) * 100) / 100
            discountCost = round((_state.value.operationDetails.filter { operationDetail -> operationDetail.productActiveType != "02" }.sumOf { it.amountDiscount }) * 100) / 100

            exoneratedCost = round((_state.value.operationDetails.filter { operationDetail -> operationDetail.productActiveType != "02" && operationDetail.productExemptFromIgv }.sumOf { it.cashSubtotal  }) * 100) / 100
            perceptionCost = round((_state.value.operationDetails.filter { operationDetail -> operationDetail.productActiveType != "02" && operationDetail.productSubjectPerception }.sumOf { it.cashSubtotal * 0.02 }) * 100) / 100
            freeCost = round((_state.value.operationDetails.filter { operationDetail -> operationDetail.productActiveType == "02" }.sumOf { it.cashSubtotal }) * 100) / 100
            Log.d("D5MAP2","totalSale : ${totalSale}")
        }

        baseCost = totalSale / 1.18
        igvCost = totalSale - baseCost
        totalSale += exoneratedCost
        totalToPay = totalSale + perceptionCost
        _state.update { it.copy(
            totalSale = totalSale,
            igvCost = round(igvCost * 100) / 100,
            baseCost =  round(baseCost * 100) / 100,
            freeCost =  round(freeCost * 100) / 100,
            discountCost =  round(discountCost * 100) / 100,
            exoneratedCost =  round(exoneratedCost * 100) / 100,
            perceptionCost =  round(perceptionCost * 100) / 100,
            totalToPay =  round(totalToPay * 100) / 100,
        )}

    }

    fun searchProductGiftsBonus(paymentType: String){
        var operationDetailsWithoutBonus: MutableList<OperationDetail> = mutableListOf()
        val productIds: Optional<List<Int>?>
        val quantities: Optional<List<Int>?>
        val subtotals: Optional<List<Double>?>

        operationDetailsWithoutBonus = _state.value.operationDetails.filter { it.productActiveType != "02" }.toMutableList()

        productIds = Optional.presentIfNotNull(operationDetailsWithoutBonus.map { it.productID })
        quantities = Optional.presentIfNotNull(operationDetailsWithoutBonus.map { it.quantity })
        subtotals = Optional.presentIfNotNull(operationDetailsWithoutBonus.map { it.quantity * it.cashPrice })


        val modifiedOperationDetails = mutableListOf<OperationDetail>()
        modifiedOperationDetails.addAll(operationDetailsWithoutBonus)
        var searchProductWithBonus = false
        var searchProductsWithBonus = false
        viewModelScope.launch(Dispatchers.IO) {

            // bonus by items with product gift
            val deferredJobProductGiftsByItems = async {getProductGiftsByProductsPurchasedUseCase.execute(productIds, quantities, subtotals, state.value.typeTradeId.toInt())}
            val newProductGiftByItemsList = deferredJobProductGiftsByItems.await()
            if (newProductGiftByItemsList.isNotEmpty()){
                newProductGiftByItemsList.onEach { productGift ->
                    val detail = OperationDetail(
                        productTariffId = productGift.productGiftTariffId,
                        stock = 0.0,
                        cashPrice = productGift.cashPrice,
                        creditPrice = 0.0,
                        quantity = productGift.totalQuantityGift.toInt(),
                        productID = productGift.productGiftId,
                        productName = productGift.productGiftName,
                        productCode = productGift.productGiftCode,
                        productActiveType = productGift.productGiftActiveType,
                        cashSubtotal = productGift.totalQuantityGift * productGift.cashPrice,
                        creditSubtotal = 0.0,
                        bonusId = productGift.bonusId,
                        bonusConjunction = productGift.bonusConjunction,
                        percentageDiscount = 0.0,
                        amountDiscount = 0.0,
                        productExemptFromIgv   = false,
                        productSubjectPerception   = false
                    )
                    modifiedOperationDetails.add(detail)
                }
                searchProductsWithBonus = true
            }
            else{
                searchProductsWithBonus = true
            }
            // bonus by item with product gift
            operationDetailsWithoutBonus.onEach { operationDetail ->

                val deferredJobProductGifts = async {getProductGiftsByProductPurchasedIdUseCase.execute(operationDetail.productID, state.value.typeTradeId.toInt(), operationDetail.quantity.toDouble())}
                val newProductGiftByItemList = deferredJobProductGifts.await()
                if (newProductGiftByItemList.isNotEmpty()){
                    newProductGiftByItemList.onEach { productGift ->
                        val detail = OperationDetail(
                            productTariffId = productGift.productGiftTariffId,
                            stock = 0.0,
                            cashPrice = productGift.cashPrice,
                            creditPrice = 0.0,
                            quantity = productGift.totalQuantityGift.toInt(),
                            productID = productGift.productGiftId,
                            productName = productGift.productGiftName,
                            productCode = productGift.productGiftCode,
                            productActiveType = productGift.productGiftActiveType,
                            cashSubtotal = productGift.totalQuantityGift * productGift.cashPrice,
                            creditSubtotal = 0.0,
                            bonusId = productGift.bonusId,
                            bonusConjunction = productGift.bonusConjunction,
                            percentageDiscount = 0.0,
                            amountDiscount = 0.0,
                            productExemptFromIgv   = false,
                            productSubjectPerception   = false
                        )
                        modifiedOperationDetails.add(detail)
                    }
                }
                searchProductWithBonus = true
            }

            _state.update { it.copy(
                operationDetails = modifiedOperationDetails,
            ) }
            if (searchProductWithBonus || searchProductsWithBonus) {
                withContext(Dispatchers.Default){
                    calculateTotalSum(paymentType)
                }

            }
        }
    }

//    private fun searchDiscounts(paymentType: String){
//
//        var operationDetailsWithoutBonus: MutableList<OperationDetail> = mutableListOf()
//        val productIds: Optional<List<Int>?>
//        val quantities: Optional<List<Int>?>
//        val subtotals: Optional<List<Double>?>
//
//        operationDetailsWithoutBonus = _state.value.operationDetails.filter { it.productActiveType != "02" }.toMutableList()
//        productIds = Optional.presentIfNotNull(operationDetailsWithoutBonus.map { it.productID })
//        quantities = Optional.presentIfNotNull(operationDetailsWithoutBonus.map { it.quantity })
//        subtotals = Optional.presentIfNotNull(operationDetailsWithoutBonus.map { it.quantity * it.cashPrice })
//        // bonus by items with discount gift
//        viewModelScope.launch {
//            val deferredJobDiscountGifts = viewModelScope.async {
//                getDiscountGiftsByProductsPurchasedUseCase.execute(productIds, quantities, subtotals, state.value.typeTradeId.toInt())
//            }
//            val discountGiftList = deferredJobDiscountGifts.await()
//            if(discountGiftList.isNotEmpty()){
//                discountGiftList.onEach { discountGift ->
//                    operationDetailsWithoutBonus.forEach { detail ->
//                        if (discountGift.productIds.contains(detail.productID)) {
//
//                            var discountAmount: Double = 0.0
//                            detail.percentageDiscount = discountGift.totalDiscountGift
//                            if (paymentType == "CREDITO"){
//                                discountAmount = detail.creditPrice * discountGift.totalDiscountGift * 0.01
//                                detail.amountDiscount = detail.quantity * discountAmount
//                                detail.creditSubtotal = (detail.quantity * detail.creditPrice) - detail.amountDiscount
//                            }
//                            else{
//                                discountAmount = detail.cashPrice * discountGift.totalDiscountGift * 0.01
//                                detail.amountDiscount = detail.quantity * discountAmount
//                                detail.cashSubtotal = (detail.quantity * detail.cashPrice) - detail.amountDiscount
//                            }
//                            val index2 = operationDetailsWithoutBonus.indexOf(detail)
//                            if (index2 != -1) {
//                                _state.update { it.copy(
//                                    operationDetails= state.value.operationDetails.toMutableList().apply { set(index2, detail) }
//                                )}
//                            }
//                        }
//                    }
//                }
//            }
//            else{
//                operationDetailsWithoutBonus.forEach { detail ->
//                    detail.percentageDiscount = 0.0
//                    detail.amountDiscount = 0.0
//                    detail.cashSubtotal = detail.quantity * detail.cashPrice
//                    detail.creditSubtotal = detail.quantity * detail.creditPrice
//                    val index2 = operationDetailsWithoutBonus.indexOf(detail)
//                    Log.d("D5MAP","index2 : ${index2}")
//                    Log.d("D5MAP","detail : ${detail}")
//                    if (index2 != -1 && state.value.operationDetails.size > index2) {
//                        _state.update { it.copy(
//                            operationDetails= state.value.operationDetails.toMutableList().apply { set(index2, detail) }
//                        )}
//                    }
//                }
//            }
//            searchProductGiftsBonus(state.value.selectedPaymentType)
//        }
//    }
    private fun searchDiscounts(paymentType: String) {
        var operationDetailsWithoutBonus: MutableList<OperationDetail> = mutableListOf()
        val productIds: Optional<List<Int>?>
        val quantities: Optional<List<Int>?>
        val subtotals: Optional<List<Double>?>

        operationDetailsWithoutBonus = _state.value.operationDetails.filter { it.productActiveType != "02" }.toMutableList()
        productIds = Optional.presentIfNotNull(operationDetailsWithoutBonus.map { it.productID })
        quantities = Optional.presentIfNotNull(operationDetailsWithoutBonus.map { it.quantity })
        subtotals = Optional.presentIfNotNull(operationDetailsWithoutBonus.map { it.quantity * it.cashPrice })

        // bonus by items with discount gift
        viewModelScope.launch {
            val deferredJobDiscountGifts = viewModelScope.async {
                getDiscountGiftsByProductsPurchasedUseCase.execute(productIds, quantities, subtotals, state.value.typeTradeId.toInt())
            }
            val discountGiftList = deferredJobDiscountGifts.await()
            if (discountGiftList.isNotEmpty()) {
                discountGiftList.forEach { discountGift ->
                    operationDetailsWithoutBonus = operationDetailsWithoutBonus.map { detail ->
                        if (discountGift.productIds.contains(detail.productID)) {
                            val discountAmount: Double
                            detail.percentageDiscount = discountGift.totalDiscountGift
                            if (paymentType == "CREDITO") {
                                discountAmount = detail.creditPrice * discountGift.totalDiscountGift * 0.01
                                detail.amountDiscount = detail.quantity * discountAmount
                                detail.creditSubtotal = (detail.quantity * detail.creditPrice) - detail.amountDiscount
                            } else {
                                discountAmount = detail.cashPrice * discountGift.totalDiscountGift * 0.01
                                detail.amountDiscount = detail.quantity * discountAmount
                                detail.cashSubtotal = (detail.quantity * detail.cashPrice) - detail.amountDiscount
                            }
                        }
                        detail
                    }.toMutableList()

                    _state.update { it.copy(operationDetails = operationDetailsWithoutBonus) }
                }
            } else {
                operationDetailsWithoutBonus = operationDetailsWithoutBonus.map { detail ->
                    detail.percentageDiscount = 0.0
                    detail.amountDiscount = 0.0
                    detail.cashSubtotal = detail.quantity * detail.cashPrice
                    detail.creditSubtotal = detail.quantity * detail.creditPrice
                    detail
                }.toMutableList()

                _state.update { it.copy(operationDetails = operationDetailsWithoutBonus) }
            }
            searchProductGiftsBonus(state.value.selectedPaymentType)
        }
    }
    fun onTextChangeOperationDetailQuantity(textQuantity: String, operationDetail: OperationDetail) {

        var newQuantity = 0
        if (textQuantity.isNotEmpty()) {
            newQuantity = textQuantity.toInt()
        }

        operationDetail.quantity = newQuantity
        val index = state.value.operationDetails.indexOf(operationDetail)
        viewModelScope.launch {
            val updatedOperationDetails = state.value.operationDetails.toMutableList().apply { set(index, operationDetail) }
            _state.update { it.copy(operationDetails = updatedOperationDetails) }
        }
        searchDiscounts(state.value.selectedPaymentType)
    }

    fun onClickDecreaseOperationDetailQuantity(operationDetail: OperationDetail){

        if (operationDetail.quantity > 0){
            operationDetail.quantity = operationDetail.quantity - 1
            val index = state.value.operationDetails.indexOf(operationDetail)
            viewModelScope.launch {
                val updatedOperationDetails = state.value.operationDetails.toMutableList().apply { set(index, operationDetail) }
                _state.update { it.copy(operationDetails = updatedOperationDetails) }
            }
            searchDiscounts(state.value.selectedPaymentType)
        }
    }

    fun onClickIncreaseOperationDetailQuantity(operationDetail: OperationDetail){

        if (operationDetail.quantity < operationDetail.stock){
            operationDetail.quantity = operationDetail.quantity + 1
            val index = state.value.operationDetails.indexOf(operationDetail)
            viewModelScope.launch {
                val updatedOperationDetails = state.value.operationDetails.toMutableList().apply { set(index, operationDetail) }
                _state.update { it.copy(operationDetails = updatedOperationDetails) }
            }
            searchDiscounts(state.value.selectedPaymentType)
        }
    }
//    fun onTextChangeOperationDetailQuantity(textQuantity: String, operationDetail: OperationDetail) {
//        val newQuantity = if (textQuantity.isNotEmpty()) textQuantity.toInt() else 0
//        val updatedOperationDetail = operationDetail.copy(quantity = newQuantity)
//
//        viewModelScope.launch {
//            val updatedOperationDetails = state.value.operationDetails.map {
//                if (it == operationDetail) updatedOperationDetail else it
//            }
//            _state.update { it.copy(operationDetails = updatedOperationDetails) }
//            searchDiscounts(state.value.selectedPaymentType)
//        }
//    }
//
//    fun onClickDecreaseOperationDetailQuantity(operationDetail: OperationDetail) {
//        if (operationDetail.quantity > 0) {
//            val updatedOperationDetail = operationDetail.copy(quantity = operationDetail.quantity - 1)
//
//            viewModelScope.launch {
//                val updatedOperationDetails = state.value.operationDetails.map {
//                    if (it == operationDetail) updatedOperationDetail else it
//                }
//                _state.update { it.copy(operationDetails = updatedOperationDetails) }
//                searchDiscounts(state.value.selectedPaymentType)
//            }
//        }
//    }
//
//    fun onClickIncreaseOperationDetailQuantity(operationDetail: OperationDetail) {
//        if (operationDetail.quantity < operationDetail.stock) {
//            val updatedOperationDetail = operationDetail.copy(quantity = operationDetail.quantity + 1)
//
//            viewModelScope.launch {
//                val updatedOperationDetails = state.value.operationDetails.map {
//                    if (it == operationDetail) updatedOperationDetail else it
//                }
//                _state.update { it.copy(operationDetails = updatedOperationDetails) }
//                searchDiscounts(state.value.selectedPaymentType)
//            }
//        }
//    }
}