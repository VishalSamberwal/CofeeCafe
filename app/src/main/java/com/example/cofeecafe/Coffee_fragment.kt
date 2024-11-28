package com.example.cofeecafe

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Coffee_fragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<CoffeeDataClass>
    private lateinit var imageList: Array<Int>
    private lateinit var titleList: Array<String>
    private lateinit var priceList: Array<String>
    private lateinit var sizeList: Array<String>
    private lateinit var myAdapter: MyAdapter
    private lateinit var descList: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coffee_fragment, container, false)

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
            R.drawable.frappecino,
            R.drawable.frappe,
            R.drawable.icedamericano,
            R.drawable.redeye
        )

        titleList = arrayOf(
            "Americano", "Caffe macchiato", "Cafe mocha", "Cappuccino", "Latte",
            "Flate white", "Affogato", "Cold brew", "Iced latte", "Nitro cold",
            "Vietnamese Iced Coffee", "Frappe", "Frappe", "Iced Americano", "Red eye"
        )

        sizeList = arrayOf("S", "M", "L")

        priceList = arrayOf(
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs",
            "80.00/rs"
        )

        recyclerView = view.findViewById(R.id.hotcoffeerv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf()
        getData()

        myAdapter = MyAdapter(dataList)

        recyclerView.adapter = myAdapter

        myAdapter.onItemClick = { coffeeData ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("image", coffeeData.Image)
            intent.putExtra("title", coffeeData.Title)
            intent.putExtra("price", coffeeData.Price)
            intent.putExtra("description", coffeeData.description)
            startActivity(intent)
        }

        return view
    }

    private fun getData() {
        for (i in imageList.indices) {
            val dataClass = CoffeeDataClass(imageList[i], titleList[i], priceList[i], descList[i], sizeList[i % 3])
            dataList.add(dataClass)
        }
        recyclerView.adapter = MyAdapter(dataList)
    }
}
