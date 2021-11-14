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
import java.util.logging.Handler
import java.util.regex.Matcher
import java.util.regex.Pattern


class ProductDataAdapter(
    var context: Context,
    private val clickListener: ProdClickListener,
    var parentProductList: ArrayList<ArrayList<metaDataBeanItem>>
) :
    RecyclerView.Adapter<ProductDataAdapter.PickerViewHolder>() {
    private lateinit var ln: LinearLayout

    //public var viewList = ArrayList<Int>()
    private var pholder: PickerViewHolder? = null
    private var adpterposion: Int = 0
    lateinit var childAdapter : PropertyAdapter
    var viewHolderList= ArrayList<PickerViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_data, parent, false)
        val holder = PickerViewHolder(view, clickListener)
        viewHolderList.add(holder)
        //setChildElementsAfterRoot(holder)
        return holder
    }

    override fun onBindViewHolder(holder: PickerViewHolder, @SuppressLint("RecyclerView") position: Int) {
//        pholder = holder
        adpterposion = position
        holder.setData(position)
        holder.childRv?.layoutManager = LinearLayoutManager(holder.childRv.context, LinearLayout.VERTICAL, false)
        //saveLatestItemData(holder)
    }

    override fun getItemCount(): Int {
        return parentProductList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    //updates the latest data of the database
    fun updateList(objectMetaData: ArrayList<metaDataBeanItem>, holder: PickerViewHolder?) {
        //save old product value here
        saveLatestItemData()

        /*childAdapter = PropertyAdapter(parentProductList[viewHolderList.size-1])
        holder?.childRv?.adapter = childAdapter
        childAdapter.notifyDataSetChanged()*/

        parentProductList?.add(objectMetaData)
        notifyDataSetChanged()

    }

    fun setData(listview: ArrayList<ArrayList<metaDataBeanItem>>) {
        parentProductList = listview
        notifyDataSetChanged()
    }

    fun deleteview(position: Int) {
        try{
            saveLatestItemData()
            viewHolderList.removeAt(position)
            parentProductList!!.removeAt(position)

            //have to delete from child object too
            //removeFromChild()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun setChildElementsAfterRoot(holder: PickerViewHolder){
        try {
            for(i in 0 until viewHolderList.size) {
                val holder = viewHolderList.get(i)
                childAdapter = PropertyAdapter(parentProductList[i])
                holder.childRv?.adapter = childAdapter
                childAdapter.notifyDataSetChanged()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun updateFormView(regexApiResponse: metaDataBeanItem, parentPosition: Int,holder: PickerViewHolder){
        saveLatestItemData()
        if(ifAlreadyAdded(parentProductList[parentPosition], regexApiResponse)){
            return
        }
        ifEmptyDataSet(parentPosition, regexApiResponse)
        if(!ifAlreadyAdded(parentProductList[parentPosition], regexApiResponse)){
            parentProductList[parentPosition].add(regexApiResponse)
        }

        childAdapter = PropertyAdapter(parentProductList[parentPosition])
        holder.childRv?.adapter = childAdapter
        childAdapter.notifyDataSetChanged()
    }
    @SuppressLint("NewApi")
    fun setFormView(selectedValue: metaDataBeanItem, holder: PickerViewHolder, position: Int) {
        pholder = holder
        updateFormView(selectedValue, position, holder)
    }

    fun saveLatestItemData(){
        try {
            for (index in 0 until parentProductList.size) {
                //val view = parentItemHolder.childRv.findViewHolderForLayoutPosition(i)//locationDatesList
                var viewHolder = viewHolderList.get(index)
                var length = viewHolder.childRv.adapter?.itemCount
                if(length!=null){
                    var savedPropertyList = ArrayList<metaDataBeanItem>()
                    for (j in 0 until length!!) {
                        var metaDataBeanItem = metaDataBeanItem()
                        val view = viewHolder.childRv.findViewHolderForAdapterPosition(j)
                        val etLabel: EditText? = view?.itemView?.findViewById(R.id.etLabel)
                        val etValue: EditText? = view?.itemView?.findViewById(R.id.etValue)
                        //stringList.add(etLabel?.text.toString())
                        metaDataBeanItem.productValue = etValue?.text.toString()
                        metaDataBeanItem.productName = etLabel?.text.toString()
                        if(!ifAlreadyAdded(savedPropertyList,metaDataBeanItem))
                            savedPropertyList.add(metaDataBeanItem)

                    }
                    if(savedPropertyList.size!=0) {
                        parentProductList[index] = savedPropertyList
                    }
                }
            }
        }catch (e: Exception){
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

    fun ifAlreadyAdded(addedProperty:ArrayList<metaDataBeanItem>, apiResponse: metaDataBeanItem):Boolean{
        var alreadyAdded = false
        for (item in addedProperty){
            if(item.productName.equals(apiResponse.productName,true)){
                alreadyAdded = true
                return alreadyAdded
            }
        }
        return alreadyAdded
    }

    fun removeFromChild(){
        try {
            //savedPropertyList.removeAt(position)
            childAdapter.notifyDataSetChanged()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun ifEmptyDataSet(parentPosition: Int, regexApiResponse: metaDataBeanItem){
        try {
            if (parentProductList[parentPosition][0].productName.equals("", true))
                parentProductList[parentPosition][0]= regexApiResponse
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun updateChild(){
        try {
            var position = viewHolderList.size - 1
            val holder = viewHolderList[position]
            childAdapter = PropertyAdapter(parentProductList[position])
            holder.childRv?.adapter = childAdapter
            childAdapter.notifyDataSetChanged()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}