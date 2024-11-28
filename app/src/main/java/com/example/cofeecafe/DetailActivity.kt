package com.example.cofeecafe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class DetailActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val image = intent.getIntExtra("image", 0)
        val title = intent.getStringExtra("title")
        val price = intent.getStringExtra("price")
        val description = intent.getStringExtra("description")

        val imageView: ImageView = findViewById(R.id.descriptionimg)
        val titleView: TextView = findViewById(R.id.desctitle)
        val priceView: TextView = findViewById(R.id.descprice)
        val descriptionView: TextView = findViewById(R.id.coffeedescription)
        val buynowbtn: AppCompatButton = findViewById(R.id.addtocartbtn)

        imageView.setImageResource(image)
        titleView.text = title ?: "No title provided"
        priceView.text = price ?: "No price provided"
        descriptionView.text = description ?: "No description provided"

        Log.d("DetailActivity", "Title: $title, Price: $price")

        buynowbtn.setOnClickListener {
            val item = CartItem(
                resImg = image,
                name = title ?: "",
                price = cleanPriceString(price),
                quantity = 1
            )
            CartManager.addItemToCart(item)

            Toast.makeText(this, "$title added to cart", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, CartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun cleanPriceString(price: String?): Double {
        return price?.replace("[^\\d.]".toRegex(), "")?.toDoubleOrNull() ?: 0.0
    }
}
