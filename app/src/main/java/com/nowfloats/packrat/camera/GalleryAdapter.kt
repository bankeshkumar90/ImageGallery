package com.nowfloats.packrat.camera

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nowfloats.packrat.R
import java.util.*


class GalleryAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private var imageList: ArrayList<ImageModel>? = null

    private val IMAGE_LIST = 0
    private val IMAGE_PICKER = 1
    companion object {
         var onItemClickListener: OnItemClickListener? = null
    }

    fun setdatainList(context: Context,imageList: ArrayList<ImageModel>?){
        this.context =context
        this.imageList =imageList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        /*val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_image_list, parent, false)
        return ImageListViewHolder(view)*/
        return if (viewType == IMAGE_LIST) {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.gallery_image_list, parent, false)
            ImageListViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.gallery_image_picker, parent, false)
            ImagePickerViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 1) IMAGE_PICKER else IMAGE_LIST
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.getItemViewType() === IMAGE_LIST) {
//        Log.e("image_path",)
        val viewHolder = holder as ImageListViewHolder
            println("image_path  " + imageList!!.get(position).image)
        Glide.with(context)
            .load("/storage/emulated/0/DCIM/Camera/IMG_20201124_074737.jpg")
//            .load(imageList!!.get(position).image)
            .placeholder(R.color.codeGray)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(viewHolder.image)
//            val bmImg = BitmapFactory.decodeFile("/storage/emulated/0/DCIM/Camera/IMG_20201124_074737.jpg")
//            viewHolder.image.setImageBitmap(bmImg)
//            viewHolder.image.setImageResource(imageList!!.get(position).image)
            viewHolder.checkBox.isChecked = imageList!![position].isSelected
        } else {
            val viewHolder = holder as ImagePickerViewHolder
            viewHolder.image.setImageResource(imageList!!.get(position).resImg)
            viewHolder.title.setText(imageList!!.get(position).title)
        }
    }

    override fun getItemCount(): Int {
        return imageList!!.size
    }

    class ImageListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var checkBox: CheckBox

        init {
            image = itemView.findViewById(R.id.image_list)
            checkBox = itemView.findViewById(R.id.circle)
            itemView.setOnClickListener { v ->
                onItemClickListener!!.onItemClick(
                    getAdapterPosition(),
                    v
                )
            }
        }
    }

    class ImagePickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var title: TextView

        init {
            image = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            itemView.setOnClickListener { v ->
                onItemClickListener!!.onItemClick(
                    getAdapterPosition(),
                    v
                )
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        GalleryAdapter.onItemClickListener = onItemClickListener
    }

    public interface OnItemClickListener {
        fun onItemClick(position: Int, v: View?)
    }


}