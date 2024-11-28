package com.example.cofeecafe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var checkoutButton: Button
    private lateinit var cartAdapter: CartAdapter
    private var cartItems: MutableList<CartItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.cart_items_recyclerview)
        checkoutButton = findViewById(R.id.checkout_button)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cartAdapter = CartAdapter(cartItems) {
            updateCartTotal()
        }
        recyclerView.adapter = cartAdapter

        loadCartItems()

        checkoutButton.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CheckOutActivity::class.java)
                intent.putParcelableArrayListExtra("cart_items", ArrayList(cartItems)) // Pass cart items
                startActivity(intent)
            }
        }
    }

    private fun loadCartItems() {
        cartItems = CartManager.getCartItems().toMutableList()
        Log.d("CartActivity", "Loaded items: $cartItems") // Log loaded cart items
        cartAdapter.setItems(cartItems)
        updateCartTotal()
    }

    private fun updateCartTotal() {
        val totalPrice = cartItems.sumOf { it.price * it.quantity }
        findViewById<TextView>(R.id.total_price).text = "Total: $${String.format("%.2f", totalPrice)}"
    }
}
