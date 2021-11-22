package com.nowfloats.packrat.addjobs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.imageViewHolder.ViewHolder
import com.nowfloats.packrat.roomdatabase.EntityClass

class AddProductTopAdapter(
    private var imageList: List<EntityClass>,
    private val clickListener: ClickListener
) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_items, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = imageList[position]
        //holder.setImage(dataModel)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    //updates the latest data of the database
    fun updateList(imageList: List<EntityClass>) {
        this.imageList = imageList
        notifyDataSetChanged()
    }
}