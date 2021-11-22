package com.nowfloats.packrat.bottomsheetdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addjobs.metaDataBeanItem

class ItemAdapter(private val mItems: ArrayList<metaDataBeanItem>, private var mListener: ItemListener?,var recylerPosition: Int) :
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
        var item: metaDataBeanItem? = null
        fun setData(item: metaDataBeanItem) {
            this.item = item
           // imageView.setImageResource(item.icPreview24dp)
            textView.setText(item.productName)
        }

        override fun onClick(v: View) {
            if (mListener != null) {
                mListener!!.onItemClick(item, recylerPosition)
            }
        }

        init {
            itemView.setOnClickListener(this)
            imageView = itemView.findViewById<View>(R.id.imageView) as ImageView
            textView = itemView.findViewById<View>(R.id.textView) as TextView
        }
    }

    interface ItemListener {
        fun onItemClick(item: metaDataBeanItem?, position: Int)
    }
}