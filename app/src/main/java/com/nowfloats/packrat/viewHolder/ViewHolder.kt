package com.nowfloats.packrat.viewHolder

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.clickInterface.ClickListener
import com.nowfloats.packrat.room.EntityClass
import kotlinx.android.synthetic.main.all_images.view.*

//ViewHolder class which holds the data in our recycler view
class ViewHolder(private val view: View, private val clickListener: ClickListener) :
    RecyclerView.ViewHolder(view) {

    fun setImage(entityClass: EntityClass) {
        view.apply {
            tvAlbumName.text = entityClass.album
            tvImageName.text = entityClass.name
            imgCapImage.setImageURI(Uri.parse(entityClass.path))  // sets the image using the uri present in database
        }

        view.setOnClickListener {
            clickListener.onClick(adapterPosition)
        }
    }
}