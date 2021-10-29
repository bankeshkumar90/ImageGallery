package com.nowfloats.packrat.addjobs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.imageViewHolder.ProductViewHolder
import com.nowfloats.packrat.roomdatabase.ProductEntityClass

class ProductDataAdapter(private val clickListener: ProdClickListener) :
    RecyclerView.Adapter<ProductViewHolder>() {

    private var prodList = ArrayList<ProductEntityClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_data, parent, false)
        return ProductViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val dataModel = prodList[position]
        holder.setData(dataModel)
    }

    override fun getItemCount(): Int {
        return prodList.size
    }

    //updates the latest data of the database
    fun updateList(prodListentity: ProductEntityClass) {
        this.prodList.add(prodListentity)
        notifyDataSetChanged()
    }

}