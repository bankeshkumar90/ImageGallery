package com.nowfloats.packrat.addjobs

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addjobs.childobjects.PropertyAdapter
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.roomdatabase.ProductFormData
import com.nowfloats.packrat.utils.CustomInputFilter
import kotlinx.android.synthetic.main.product_data.view.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class ProductDataAdapter(
    var context: Context,
    private val clickListener: ProdClickListener,
    var productList: ArrayList<metaDataBeanItem>
) :
    RecyclerView.Adapter<ProductDataAdapter.PickerViewHolder>() {
    private lateinit var ln: LinearLayout

    //public var viewList = ArrayList<Int>()
    private var pholder: PickerViewHolder? = null
    private var adpterposion: Int = 0
    lateinit var childAdapter : PropertyAdapter
    lateinit  var propertyList : ArrayList<metaDataBeanItem>
    var viewHolderList= ArrayList<PickerViewHolder>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_data, parent, false)
        return PickerViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: PickerViewHolder, @SuppressLint("RecyclerView") position: Int) {
//        pholder = holder
        adpterposion = position
        holder.setData(position)
        viewHolderList.add(holder)
        holder.childRv?.layoutManager = LinearLayoutManager(holder.childRv.context, LinearLayout.VERTICAL, false)
        propertyList = ArrayList<metaDataBeanItem>()

    }

    override fun getItemCount(): Int {
        return productList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    //updates the latest data of the database
    fun updateList(objectMetaData: metaDataBeanItem, holder: PickerViewHolder?) {
        //save old product value here
        saveLatestItemData(holder)
        productList?.add(objectMetaData)
        notifyDataSetChanged()
    }

    fun setData(listview: ArrayList<metaDataBeanItem>) {
        productList = listview
        notifyDataSetChanged()
    }

    fun deleteview(position: Int) {
        try{
            productList!!.removeAt(position)
            notifyDataSetChanged()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun updateFormView(regexApiResponse: metaDataBeanItem, position: Int,holder: PickerViewHolder){
        saveLatestItemData(holder)
        if(ifAlreadyAdded(propertyList, regexApiResponse)){
            return
        }
        propertyList.add(regexApiResponse)
        childAdapter = PropertyAdapter(propertyList)
        holder.childRv?.adapter = childAdapter
        childAdapter.updateList(regexApiResponse)
    }
    @SuppressLint("NewApi")
    fun setFormView(selectedValue: metaDataBeanItem, holder: PickerViewHolder, position: Int) {
        pholder = holder
        updateFormView(selectedValue, position, holder)
    }

    fun saveLatestItemData(holder: PickerViewHolder?){
        try {
            var length = holder?.childRv?.adapter?.itemCount
            if (length != null) {
                for (i in 0 until length!!) {
                    val view = holder?.childRv?.findViewHolderForAdapterPosition(i)
                    val etLabel: EditText? = view?.itemView?.findViewById(R.id.etLabel)
                    val etValue: EditText? = view?.itemView?.findViewById(R.id.etValue)
                    propertyList[i].productValue = etValue?.text.toString()
                    propertyList[i].productName = etLabel?.text.toString()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    class PickerViewHolder(itemView: View, private val clickListener: ProdClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var add_prop_id: LinearLayout
        var add_form: LinearLayout
        var add_product_count: TextView

        var childRv:RecyclerView

        init {
            add_prop_id = itemView.findViewById(R.id.add_prop_id)
            add_form = itemView.findViewById(R.id.ln_form)
            add_product_count = itemView.findViewById(R.id.add_product_count)
            childRv = itemView.findViewById(R.id.childList)



            add_prop_id.setOnClickListener { v ->

            }
        }


        fun setData(position: Int) {
            itemView.apply {
                add_product_count.text = (position + 1).toString()
//            tvImageName.text = entityClass.name
                /*product_imageview.setImageURI(Uri.parse(entityClass.path))  // sets the image using the uri present in database
                close_imageview.setOnClickListener {
                    clickListener.onClickDelete(entityClass.id)
                }*/
                add_prop_id.setOnClickListener {
                    clickListener.onClickAdd(adapterPosition)
                }

                dlt_cross.setOnClickListener {
                    clickListener.onClickItemDelete(adapterPosition)
                }

            }
            itemView.setTag(position)
            itemView.setOnClickListener {
//            clickListener.onClick(adapterPosition)
            }
        }

    }

    inner class ValidateFilter : InputFilter {
        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
            for (i in start until end) {
                //val string = editText.text.toString().trim()
                val checkMe = source[i].toString()
                val pattern: Pattern = Pattern.compile("   [ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789_]*")
                val matcher: Matcher = pattern.matcher(checkMe)
                val valid: Boolean = matcher.matches()
                if (!valid) {
                    //textView.text = string
                }
            }
            return null
        }
    }

    fun ifAlreadyAdded(propertyList:ArrayList<metaDataBeanItem>, apiResponse: metaDataBeanItem):Boolean{
        var alreadyAdded = false
        for (item in propertyList){
            if(item.productName.equals(apiResponse.productName,true)){
                alreadyAdded = true
                return alreadyAdded
            }
        }
        return alreadyAdded
    }

}