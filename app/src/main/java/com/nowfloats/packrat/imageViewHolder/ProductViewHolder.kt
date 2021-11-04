package com.nowfloats.packrat.imageViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.roomdatabase.ProductEntityClass
import kotlinx.android.synthetic.main.product_data.view.*

class ProductViewHolder(private val view: View, private val clickListener: ProdClickListener) :
    RecyclerView.ViewHolder(view) {

    fun setData(prodentity: ProductEntityClass) {
        view.apply {
//            tvAlbumName.text = entityClass.album
//            tvImageName.text = entityClass.name
            /*product_imageview.setImageURI(Uri.parse(entityClass.path))  // sets the image using the uri present in database
            close_imageview.setOnClickListener {
                clickListener.onClickDelete(entityClass.id)
            }*/
            add_prop_id.setOnClickListener {
//                clickListener.onClickAdd(adapterPosition)
            }
        }

        view.setOnClickListener {
//            clickListener.onClick(adapterPosition)
        }
    }

}