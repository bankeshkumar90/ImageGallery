package com.nowfloats.packrat.addjobs.childobjects


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addjobs.metaDataBeanItem
import com.nowfloats.packrat.clickInterface.ChildItemActionListener
import com.nowfloats.packrat.utils.CustomInputFilter

class PropertyAdapter(
    var propertyList: ArrayList<metaDataBeanItem>,
    var childItemActionListener: ChildItemActionListener
): RecyclerView.Adapter<PropertyAdapter.ViewHolder>() {
    lateinit var vHolder :ViewHolder
    override fun onBindViewHolder(holder: PropertyAdapter.ViewHolder, position: Int) {
        vHolder = holder
        val propertyItem = propertyList[position]
        holder.etLabel?.text = propertyItem.productName
        holder.etValue?.text = propertyItem.productValue
        if(!propertyItem.productRegex.isNullOrEmpty()){
            holder.etValue .filters += propertyItem.productRegex?.let { CustomInputFilter(it) }
        }else{
            if(propertyItem.productName.equals("Others", true))
                holder.etLabel.isEnabled = true
        }
        holder.delteIcon.setOnClickListener(View.OnClickListener {
            //remove layout - before removing update Parent Data by listner
            childItemActionListener.onClickCross(position, holder, propertyList)
            holder.etValue.setText("")
            holder.etLabel.setText("")
            //propertyList.removeAt(position)
            var n = propertyList
            System.out.println("propertyList->$n")
            notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_locations, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val etLabel = itemView.findViewById<TextView>(R.id.etLabel)
        val etValue = itemView.findViewById<TextView>(R.id.etValue)
        val delteIcon = itemView.findViewById<ImageView>(R.id.deleteIcon)
        var llItemView = itemView.findViewById<LinearLayout>(R.id.llProductItem)
    }

    fun updateList(propertyListItem: ArrayList<metaDataBeanItem>){
        propertyList = propertyListItem
     }

    fun getAllItemDetails(pos:Int){
        val propertyListValue = ArrayList<metaDataBeanItem>()
        for (i in 0 until propertyList.size){
            var  propertyDataClass = metaDataBeanItem()
        }
    }



}