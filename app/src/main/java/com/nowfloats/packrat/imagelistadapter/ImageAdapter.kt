package com.nowfloats.packrat.imagelistadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.roomdatabase.EntityClass
import com.nowfloats.packrat.imageViewHolder.ViewHolder

//adapter for our recycler view
class ImageAdapter(
    private val clickListener: ClickListener,
    private var imagePathList: ArrayList<String>
) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_items, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val dataModel = imageList[position]
        holder.setImage( imagePathList[position], position)
    }

    override fun getItemCount(): Int {
        return imagePathList.size
    }

    //updates the latest data of the database
    fun updateList(imageList: List<EntityClass>) {
        //this.imageList = imageList
        notifyDataSetChanged()
    }
    //updates the latest data of the database
    fun updateImageList(imageList: ArrayList<String>) {
        this.imagePathList = imageList
        notifyDataSetChanged()
    }
}