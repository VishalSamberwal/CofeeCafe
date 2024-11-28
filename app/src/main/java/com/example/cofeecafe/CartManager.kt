package com.example.cofeecafe

object CartManager {
    private val _cartItems = mutableListOf<CartItem>()

    fun addItemToCart(item: CartItem) {
        val existingItem = _cartItems.find { it.name == item.name }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            _cartItems.add(item)
        }
    }

    fun updateItemQuantity(item: CartItem, newQuantity: Int) {
        val existingItem = _cartItems.find { it.name == item.name }
        if (existingItem != null) {
            if (newQuantity < 1) {
                _cartItems.remove(existingItem)
            } else {
                existingItem.quantity = newQuantity
            }
        }
    }

    fun removeItem(item: CartItem) {
        _cartItems.remove(item)
    }

    fun getCartItems(): List<CartItem> {
        return _cartItems.toList()
    }

    fun clearCart() {
        _cartItems.clear()
    }
}
