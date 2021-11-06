package com.nowfloats.packrat.addjobs

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.roomdatabase.ProductFormData
import kotlinx.android.synthetic.main.product_data.view.*

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_data, parent, false)
        return PickerViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: PickerViewHolder, @SuppressLint("RecyclerView") position: Int) {
//        pholder = holder
        adpterposion = position
        holder.setData(position)
    }

    override fun getItemCount(): Int {
        return productList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    //updates the latest data of the database
    fun updateList(objectMetaData: metaDataBeanItem) {
        productList?.add(objectMetaData)
//        this.viewList.add(positionview)
//        notifyItemInserted(viewList!!.size - 1);
//        notifyDataSetChanged()
        notifyItemChanged(adpterposion)
    }

    fun setData(listview: ArrayList<metaDataBeanItem>) {
        productList = listview
        notifyDataSetChanged()
    }

    fun deleteview(position: Int) {
        productList!!.removeAt(position)
        notifyItemInserted(position)
    }

    @SuppressLint("NewApi")
    fun setFormView(hint: String, holder: PickerViewHolder, position: Int) {
        pholder = holder

    }


    class PickerViewHolder(itemView: View, private val clickListener: ProdClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var add_prop_id: LinearLayout
        var add_form: LinearLayout
        var add_product_count: TextView

        init {
            add_prop_id = itemView.findViewById(R.id.add_prop_id)
            add_form = itemView.findViewById(R.id.ln_form)
            add_product_count = itemView.findViewById(R.id.add_product_count)
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

    fun getProductFormData(): ProductFormData {
        var productName = ""
        var productprice = ""
        var productBarCode = ""
        var productQuantity = ""
        var productOthers = ""
        lateinit var productFormData: ProductFormData


        var childcount = pholder?.add_form!!.childCount
        for (i in 0 until childcount) {
            var viewparent = pholder?.add_form!!.getChildAt(i)
            if (viewparent is LinearLayout) {
                var subchildcount = viewparent.childCount
                for (i in 0 until subchildcount) {
                    var viewchildfirst = viewparent.getChildAt(0)
                    var viewchildsecond = viewparent.getChildAt(1)
                    if (viewchildfirst is EditText && viewchildfirst.text.toString() contentEquals ("Product")) {
                        if (viewchildsecond is EditText)
                            productName = viewchildsecond.text.toString()
                    } else if (viewchildfirst is EditText && viewchildfirst.text.toString() contentEquals ("Price")) {
                        if (viewchildsecond is EditText)
                            productprice = viewchildsecond.text.toString()
                    } else if (viewchildfirst is EditText && viewchildfirst.text.toString() contentEquals ("Barcode")) {
                        if (viewchildsecond is EditText)
                            productBarCode = viewchildsecond.text.toString()
                    } else if (viewchildfirst is EditText && viewchildfirst.text.toString() contentEquals ("Quantity")) {
                        if (viewchildsecond is EditText)
                            productQuantity = viewchildsecond.text.toString()
                    } else if (viewchildfirst is EditText && viewchildfirst.text.toString() contentEquals ("Others")) {
                        if (viewchildsecond is EditText)
                            productOthers = viewchildsecond.text.toString()
                    }
                }
                productFormData = ProductFormData(
                    productName,
                    productprice,
                    productBarCode,
                    productQuantity,
                    productOthers
                )
            }
        }
        return productFormData
    }
}