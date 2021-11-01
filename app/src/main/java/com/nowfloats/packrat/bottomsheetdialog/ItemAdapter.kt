package com.nowfloats.packrat.bottomsheetdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R

class ItemAdapter(private val mItems: List<Item>, private var mListener: ItemListener?) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder?>() {
    fun setListener(listener: ItemListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mItems[position])
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView: ImageView
        var textView: TextView
        var item: Item? = null
        fun setData(item: Item) {
            this.item = item
            imageView.setImageResource(item.getDrawableResource())
            textView.setText(item.getTitle())
        }

        override fun onClick(v: View) {
            if (mListener != null) {
                mListener!!.onItemClick(item)
            }
        }

        init {
            itemView.setOnClickListener(this)
            imageView = itemView.findViewById<View>(R.id.imageView) as ImageView
            textView = itemView.findViewById<View>(R.id.textView) as TextView
        }
    }

    interface ItemListener {
        fun onItemClick(item: Item?)
    }
}