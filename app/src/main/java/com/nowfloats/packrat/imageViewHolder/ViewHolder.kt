package com.nowfloats.packrat.imageViewHolder

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.roomdatabase.EntityClass
import kotlinx.android.synthetic.main.image_items.view.*

//ViewHolder class which holds the data in our recycler view
class ViewHolder(private val view: View, private val clickListener: ClickListener) :
    RecyclerView.ViewHolder(view) {

    fun setImage(entityClass: EntityClass) {
        view.apply {
//            tvAlbumName.text = entityClass.album
//            tvImageName.text = entityClass.name
            product_imageview.setImageURI(Uri.parse(entityClass.path))  // sets the image using the uri present in database
            close_imageview.setOnClickListener {
                clickListener.onClickDelete(entityClass.id)
            }
        }

        view.setOnClickListener {
            clickListener.onClick(adapterPosition)
        }
    }
}