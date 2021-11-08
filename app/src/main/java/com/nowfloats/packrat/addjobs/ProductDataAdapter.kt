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
        if(productList[position].productVisible==false){
            holder.ll_pname.visibility = View.GONE
        }else
            holder.ll_pname.visibility = View.VISIBLE

        if(productList[position].priceVisible==false){
            holder.ll_pprice.visibility = View.GONE
        }else
            holder.ll_pprice.visibility = View.VISIBLE

        if(productList[position].barcodeVisbile==false){
            holder.ll_barcode.visibility = View.GONE
        }else
            holder.ll_barcode.visibility = View.VISIBLE

        if(productList[position].quantityVisible==false){
            holder.llPquantity.visibility = View.GONE
        }else
            holder.llPquantity.visibility = View.VISIBLE

        if(productList[position].othersVisible==false){
            holder.llPOther.visibility = View.GONE
        }else
            holder.llPOther.visibility = View.VISIBLE

        holder.priceDelete.setOnClickListener(View.OnClickListener {
            productList[position].priceVisible = false
            productList[position].price = ""
            notifyDataSetChanged()
        })
        holder.productDelete.setOnClickListener(View.OnClickListener {
            productList[position].productVisible = false
            productList[position].productName = ""
            notifyDataSetChanged()
        })
        holder.barcodeDelete.setOnClickListener(View.OnClickListener {
            productList[position].barcodeVisbile = false
            productList[position].barcode = ""
            notifyDataSetChanged()
        })
        holder.quantityDelete.setOnClickListener(View.OnClickListener {
            productList[position].quantityVisible = false
            productList[position].quantity = ""
            notifyDataSetChanged()
        })
        holder.othersDelete.setOnClickListener(View.OnClickListener {
            productList[position].othersVisible = false
            productList[position].othersName = ""
            notifyDataSetChanged()
         })

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

    @SuppressLint("NewApi")
    fun setFormView(selectedValue: String, holder: PickerViewHolder, position: Int) {
        pholder = holder
        if(selectedValue.equals(context.resources.getString(R.string.product))){
            productList[position].productVisible = true
            productList[position].productName = selectedValue
            pholder?.labelProduct?.setText(selectedValue)
         }
        if(selectedValue.equals(context.resources.getString(R.string.price))){
            productList[position].priceVisible = true
            productList[position].price = selectedValue
            pholder?.labelPrice?.setText(selectedValue)
         }
        if(selectedValue.equals(context.resources.getString(R.string.barcode))){
            productList[position].barcodeVisbile = true
            productList[position].barcode = selectedValue
            pholder?.labelBarcode?.setText(selectedValue)
         }
        if(selectedValue.equals(context.resources.getString(R.string.quantity))){
            productList[position].quantityVisible = true
            productList[position].quantity = selectedValue
            pholder?.labelQuantity?.setText(selectedValue)
         }
        if(selectedValue.equals(context.resources.getString(R.string.other))){
            productList[position].othersVisible = true
            productList[position].othersName = selectedValue
            pholder?.labelOthers?.setText(selectedValue)
         }
        notifyDataSetChanged()
    }


    class PickerViewHolder(itemView: View, private val clickListener: ProdClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var add_prop_id: LinearLayout
        var add_form: LinearLayout
        var add_product_count: TextView

        var ll_pname:LinearLayout
        var ll_pprice : LinearLayout
        var ll_barcode:LinearLayout
        var llPquantity : LinearLayout
        var llPOther:LinearLayout

        var productDelete: ImageView
        var priceDelete: ImageView
        var barcodeDelete: ImageView
        var quantityDelete: ImageView
        var othersDelete: ImageView

        var labelProduct: EditText
        var labelPrice: EditText
        var labelBarcode: EditText
        var labelQuantity: EditText
        var labelOthers: EditText

        init {
            add_prop_id = itemView.findViewById(R.id.add_prop_id)
            add_form = itemView.findViewById(R.id.ln_form)
            add_product_count = itemView.findViewById(R.id.add_product_count)

            ll_pname = itemView.findViewById(R.id.ll_pname)
            ll_pprice = itemView.findViewById(R.id.ll_pprice)
            ll_barcode = itemView.findViewById(R.id.ll_barcode)
            llPquantity = itemView.findViewById(R.id.llPquantity)
            llPOther = itemView.findViewById(R.id.llPOther)

            productDelete = itemView.findViewById(R.id.productDelete)
            priceDelete = itemView.findViewById(R.id.priceDelete)
            barcodeDelete = itemView.findViewById(R.id.barcodeDelete)
            quantityDelete = itemView.findViewById(R.id.quantityDelete)
            othersDelete = itemView.findViewById(R.id.othersDelete)

            labelProduct = itemView.findViewById(R.id.labelProduct)
            labelPrice = itemView.findViewById(R.id.labelPrice)
            labelBarcode = itemView.findViewById(R.id.labelBarcode)
            labelQuantity = itemView.findViewById(R.id.labelQuantity)
            labelOthers = itemView.findViewById(R.id.labelOthers)

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