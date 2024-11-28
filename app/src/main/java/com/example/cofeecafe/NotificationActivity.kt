package com.example.cofeecafe

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList : ArrayList<NotificationDataClass>
    lateinit var imageList : Array<Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification)

        imageList = arrayOf(
            R.drawable.discountcoffee,
            R.drawable.discountcoffee,
            R.drawable.discountcoffee,
            R.drawable.discountcoffee,
            R.drawable.discountcoffee,
            R.drawable.discountcoffee,
            R.drawable.discountcoffee,
            R.drawable.discountcoffee,
            R.drawable.discountcoffee
        )
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.notificationrv)
        recyclerView.layoutManager = LinearLayoutManager(this) // Use 'this' instead of requireContext()
        recyclerView.setHasFixedSize(true)

        // Initialize the dataList and populate it
        dataList = arrayListOf()
        getData()

    }


    private fun getData() {
        for (i in imageList.indices) {
            val dataClass = NotificationDataClass(imageList[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = NotiAdapter(dataList)

    }
    }