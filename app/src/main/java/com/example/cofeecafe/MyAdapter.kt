package com.example.cofeecafe
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter (private val dataList: ArrayList<CoffeeDataClass>) :  RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var onItemClick : ((CoffeeDataClass) -> Unit)?= null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
      val imageView : ImageView =itemView.findViewById(R.id.itemImg)
        val titleView: TextView = itemView.findViewById(R.id.itemTitle)
        val priceView: TextView = itemView.findViewById(R.id.itrmPrice)


        fun bind(data: CoffeeDataClass) {
            imageView.setImageResource(data.Image)
            titleView.text = data.Title
            priceView.text = data.Price

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coffee_items, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.imageView.setImageResource(currentItem.Image)
        holder.titleView.text = currentItem.Title
        holder.priceView.text = currentItem.Price

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}