package com.example.cofeecafe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<CoffeeDataClass>
    private lateinit var imageList: Array<Int>
    private lateinit var titleList: Array<String>
    private lateinit var priceList: Array<String>
    private lateinit var sizeList: Array<String>
    private lateinit var myAdapter: MyAdapter
    private lateinit var descList: Array<String>
    private lateinit var bottomNavbar: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        bottomNavbar = findViewById(R.id.bottom_navigation)

        bottomNavbar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_menu -> {
                    startActivity(Intent(this, MenuActivity::class.java))
                    true
                }
                R.id.bottom_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.bottom_order ->{
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.bottom_account ->{
                    val intent = Intent(this, AccountActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Initialize data lists
        descList = Array(20) { getString(R.string.CoffeeDescription) }

        imageList = arrayOf(
            R.drawable.americano,
            R.drawable.caffemacchiato,
            R.drawable.cafemocha,
            R.drawable.cappuccino,
            R.drawable.latte,
            R.drawable.flatewhite,
            R.drawable.affogato,
            R.drawable.coldbrew,
            R.drawable.icedlatte,
            R.drawable.nitrocold,
            R.drawable.vietnamessicecoffee,
            R.drawable.frappe,
            R.drawable.icedamericano,
            R.drawable.redeye,
            R.drawable.kulhhadhai,
            R.drawable.blacktea,
            R.drawable.normaltea,
            R.drawable.greentea,
            R.drawable.herbeltea
        )

        titleList = arrayOf(
            "Americano", "Caffe macchiato", "Cafe mocha", "Cappuccino", "Latte",
            "Flat white", "Affogato", "Cold brew", "Iced latte", "Nitro cold",
            "Vietnamese Iced Coffee", "Frappe", "Iced Americano", "Red eye",
            "Kulhad Chai", "Black Tea", "Normal Tea", "Green Tea", "Herbal Tea"
        )

        priceList = arrayOf(
            "80.00/rs", "80.00/rs", "80.00/rs", "80.00/rs", "80.00/rs",
            "80.00/rs", "80.00/rs", "80.00/rs", "80.00/rs", "80.00/rs",
            "80.00/rs", "80.00/rs", "80.00/rs", "80.00/rs", "40.00/rs",
            "30.00/rs", "20.00/rs", "40.00/rs", "50.00/rs"
        )

        sizeList = arrayOf("S", "M", "L")


        recyclerView = findViewById(R.id.shopmenurv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)


        dataList = arrayListOf()
        getData()


        myAdapter = MyAdapter(dataList)

        recyclerView.adapter = myAdapter


        myAdapter.onItemClick = { coffeeData ->
            val intent = Intent(this, DetailActivity::class.java)

            intent.putExtra("image", coffeeData.Image)
            intent.putExtra("title", coffeeData.Title)
            intent.putExtra("price", coffeeData.Price)
            intent.putExtra("description", coffeeData.description)
            startActivity(intent)
        }
    }

    private fun getData() {
        for (i in imageList.indices) {
            val dataClass = CoffeeDataClass(imageList[i], titleList[i], priceList[i], descList[i], sizeList[i % 3]) // Example size assignment
            dataList.add(dataClass)
        }
    }
}
