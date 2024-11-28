package com.example.cofeecafe

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
class Tea_Fragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<CoffeeDataClass>
    private lateinit var imageList: Array<Int>
    private lateinit var titleList: Array<String>
    private lateinit var priceList: Array<String>
    private lateinit var sizeList: Array<String> // Change to Array<String>
    private lateinit var myAdapter: MyAdapter
    private lateinit var descList: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tea_, container, false)

        descList = Array(5) { getString(R.string.CoffeeDescription) }

        // Initialize the arrays
        imageList = arrayOf(
            R.drawable.kulhhadhai,
            R.drawable.blacktea,
            R.drawable.normaltea,
            R.drawable.greentea,
            R.drawable.herbeltea
        )
        titleList = arrayOf(
            "Kulhad Chai", "Black Tea", "Normal Tea", "Green Tea", "Herbel Tea"
        )
        priceList = arrayOf(
            "40.00/rs",
            "30.00/rs",
            "20.00/rs",
            "40.00/rs",
            "50.00/rs"
        )

        sizeList = arrayOf("S", "M", "L")

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.tearv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        // Initialize the dataList and populate it
        dataList = arrayListOf()
        getData()

        myAdapter = MyAdapter(dataList)

        // Set the adapter to the RecyclerView
        recyclerView.adapter = myAdapter

        // Set click listener for RecyclerView items
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
            val dataClass = CoffeeDataClass(imageList[i],titleList[i] ,priceList[i]  ,descList[i], sizeList[i % 3])
            dataList.add(dataClass)
        }
        recyclerView.adapter = MyAdapter(dataList)
    }
}
