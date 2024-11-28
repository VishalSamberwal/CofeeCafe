package com.example.cofeecafe

data class Order(
    val fullName: String = "",
    val city: String = "",
    val district: String = "",
    val landmark: String = "",
    val postalCode: String = "",
    val mobileNumber: String = "",
    val cartItems: List<CartItem>? = null,
    val selectedPaymentMethod: String = ""

)
