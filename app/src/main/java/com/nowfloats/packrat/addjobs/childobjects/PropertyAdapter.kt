package com.nowfloats.packrat.addjobs.childobjects


import android.text.InputType
import android.text.method.DigitsKeyListener
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
    //var filter =   "^[0-9]*\\.?[0-2]*"
    var filter = "^[0-9]*(\\.[0-9][0-9]?)?"//"([1-9][0-9]*|[0-9])(.[0-9])?"//"[0-9]+(\\.[0-9])?"//"^([1-9]\\d*|0)(\\.\\d)?$"
    override fun onBindViewHolder(holder: PropertyAdapter.ViewHolder, position: Int) {
        vHolder = holder
        val propertyItem = propertyList[position]
        if(!propertyItem.productRegex.isNullOrEmpty()){
            if(propertyItem.productRegex.equals("^\\d{0,8}(\\.\\d{1,2})?\$")||("^([0-9]){1,10}\$").equals(propertyItem.productRegex)|| filter.equals(propertyItem.productRegex)){
                holder.etValue.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                holder.etValue.keyListener = DigitsKeyListener.getInstance(".0123456789")
                //^\\d{0,9}\\.\\d{1,2}$
                //holder.etValue .filters += propertyItem.productRegex?.let { CustomInputFilter(filter) }
            }else
                holder.etValue.inputType = InputType.TYPE_CLASS_TEXT
            holder.etValue .filters += propertyItem.productRegex?.let { CustomInputFilter(it) }
        }else{
            if(propertyItem.productName.equals("Others", true) || propertyItem.productName.equals("", true))
                holder.etLabel.isEnabled = true
        }
        holder.delteIcon.setOnClickListener(View.OnClickListener {
            //remove layout - before removing update Parent Data by listner
            propertyList[position].productName = holder.etLabel.text.toString()
            propertyList[position].productValue = holder.etValue.text.toString()
            childItemActionListener.onBeforeClickCross(position, holder, propertyList)
            childItemActionListener.onClickCross(position, holder, propertyList)
            holder.etValue.setText("")
            holder.etLabel.setText("")
            propertyList.removeAt(position)
            var n = propertyList
            notifyItemChanged(position)
          })
        if(propertyItem.productName.equals("Others")) {
            propertyItem.productName =""
            holder.etLabel?.text = propertyItem.productName
            holder.etLabel.requestFocus()
        }
        else
            holder.etLabel?.text = propertyItem.productName
        holder.etValue?.text = propertyItem.productValue

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