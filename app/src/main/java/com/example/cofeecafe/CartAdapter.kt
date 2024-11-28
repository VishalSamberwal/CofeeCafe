package com.example.cofeecafe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private var cartItems: MutableList<CartItem>,
    private val onQuantityChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_items, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)

        holder.itemView.findViewById<Button>(R.id.increase_quantity).setOnClickListener {
            updateQuantity(holder, cartItem, 1)
        }

        holder.itemView.findViewById<Button>(R.id.decquantiti).setOnClickListener {
            updateQuantity(holder, cartItem, -1)
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    private fun updateQuantity(holder: CartViewHolder, cartItem: CartItem, change: Int) {
        val newQuantity = cartItem.quantity + change

        if (newQuantity > 0) {
            cartItem.quantity = newQuantity
            holder.itemView.findViewById<TextView>(R.id.item_quantity).text = newQuantity.toString()
            onQuantityChanged()
        } else {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                cartItems.removeAt(position)
                notifyItemRemoved(position)
                CartManager.removeItem(cartItem)
                onQuantityChanged()
            }
        }
    }

    fun setItems(newCartItems: MutableList<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cartItem: CartItem) {
            itemView.apply {
                findViewById<TextView>(R.id.itemTitle).text = cartItem.name
                findViewById<TextView>(R.id.itemPrice).text = "$${String.format("%.2f", cartItem.price)}"
                findViewById<TextView>(R.id.item_quantity).text = cartItem.quantity.toString()
                findViewById<ImageView>(R.id.itemImg).setImageResource(cartItem.resImg)
            }
        }
    }
}
