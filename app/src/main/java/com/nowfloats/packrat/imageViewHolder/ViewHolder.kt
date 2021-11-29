package com.nowfloats.packrat.imageViewHolder

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nowfloats.packrat.R
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.roomdatabase.EntityClass
import kotlinx.android.synthetic.main.image_items.view.*

//ViewHolder class which holds the data in our recycler view
class ViewHolder(private val view: View, private val clickListener: ClickListener) :
    RecyclerView.ViewHolder(view) {
    var imageSelected: TextView
        init {
            imageSelected = view.findViewById(R.id.imgSelected)
        }
        fun setImage( imagePath:String, pos: Int) {
        view.apply {
//            tvAlbumName.text = entityClass.album
//            tvImageName.text = entityClass.name
            Glide.with(context)
                .load( Uri.parse(imagePath))
                .override(100, 150)
                .centerInside() // scale to fill the ImageView and crop any extra
                .into( product_imageview);
            //product_imageview.setImageURI(Uri.parse(imagePath))  // sets the image using the uri present in database
            close_imageview.setOnClickListener {
                clickListener.onClickDelete(adapterPosition)
            }
        }

        view.setOnClickListener {
            clickListener.onClick(adapterPosition)
        }
    }
}