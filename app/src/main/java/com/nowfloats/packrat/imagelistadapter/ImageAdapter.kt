package com.nowfloats.packrat.imagelistadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addjobs.ProductDataAdapter
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
    var positionSeleted = 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val dataModel = imageList[position]
        holder.setImage( imagePathList[position], position)
        if(positionSeleted==position){
            holder.imageSelected.visibility = View.VISIBLE
        }else
            holder.imageSelected.visibility = View.INVISIBLE

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
    @SuppressLint("NewApi")
    fun setImageSelected(position: Int) {
        positionSeleted = position
        notifyDataSetChanged()
    }

    fun deleteImage(position: Int){
        try {
            imagePathList.removeAt(position)
            notifyDataSetChanged()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}