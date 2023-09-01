package com.walker.fakeecommerce.data.cart

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(): ViewModel() {

    val cartUIState = mutableStateOf(CartUIState())

    fun onEvent(event: CartUIEvents) {
        when (event) {
            is CartUIEvents.AddProductToCart -> {
                event.product?.let {
                    val tempList = cartUIState.value.products.toMutableList()

                    val existingProduct = tempList.find { it.id == event.product.id }

                    existingProduct?.let {
                        existingProduct.quantity = event.product.quantity
                    } ?: run {
                        tempList.add(event.product)
                    }
                    cartUIState.value = cartUIState.value.copy(
                        products = tempList
                    )
                }
            }
            is CartUIEvents.RemoveProductFromCart -> {
                val tempList = cartUIState.value.products.toMutableList()

                tempList.remove(event.product)

                cartUIState.value = cartUIState.value.copy(
                    products = tempList
                )
            }
        }
    }
}