package com.example.cofeecafe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView

class NotiAdapter(private val dataList: ArrayList<NotificationDataClass>) :
    RecyclerView.Adapter<NotiAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.notificationImage)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rvImage : ImageView = itemView.findViewById(R.id.notificationimgrv)
    }
}