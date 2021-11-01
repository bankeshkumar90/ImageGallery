package com.nowfloats.packrat.addjobs

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nowfloats.packrat.R
import com.nowfloats.packrat.clickInterface.ProdClickListener
import com.nowfloats.packrat.roomdatabase.ProductEntityClass
import com.nowfloats.packrat.roomdatabase.ProductFormData
import kotlinx.android.synthetic.main.product_data.view.*

class ProductDataAdapter(var context: Context, private val clickListener: ProdClickListener) :
    RecyclerView.Adapter<ProductDataAdapter.PickerViewHolder>() {
    private lateinit var ln: LinearLayout
    private var prodList = ArrayList<ProductEntityClass>()
    private var pholder: PickerViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_data, parent, false)
        return PickerViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: PickerViewHolder, position: Int) {
        pholder = holder
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

    fun deleteview(position: Int) {
        this.prodList.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NewApi")
    fun setFormView(hint: String) {
        val linearLayoutParent = LinearLayout(context)
        linearLayoutParent.orientation = LinearLayout.HORIZONTAL
        val linearLayoutParentParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, 0f
        )
        linearLayoutParent.layoutParams = linearLayoutParentParams
        linearLayoutParent.setPadding(0, 0, 0, 0)

        val editText = EditText(context)

        editText.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f
            )
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down)
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        editText.setPadding(20, 20, 20, 20)
        editText.setText(hint)
        editText.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.grey))
        editText.setTextColor(context.getColor(R.color.primary_color))
        editText.textSize = context.resources.getDimension(R.dimen.dp_8)
        editText.setHintTextColor(context.getColor(R.color.inventory_hint))
        linearLayoutParent.addView(editText)

        val editText1 = EditText(context)
        editText1.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f
            )
//        val drawable1 = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down)
//        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawabl, null)
        editText1.setPadding(20, 20, 20, 20)
//        editText1.setHint(hint)
        editText1.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.grey))
        editText1.setTextColor(context.getColor(R.color.primary_color))
        editText1.textSize = context.resources.getDimension(R.dimen.dp_8)
        editText1.setHintTextColor(context.getColor(R.color.inventory_hint))
        linearLayoutParent.addView(editText1)


        val textView = TextView(context)
        textView.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.8f
            )
//        val drawable1 = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down)
//        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawabl, null)
        textView.setPadding(0, 0, 5, 0)
        textView.setText("X")
        textView.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.grey))
        textView.setTextColor(context.getColor(R.color.primary_color))
        textView.textSize = context.resources.getDimension(R.dimen.dp_8)
        linearLayoutParent.addView(textView)
        if (pholder?.add_form != null) {
            pholder!!.add_form.addView(linearLayoutParent);
            notifyDataSetChanged()
        }
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

        fun setData(prodentity: ProductEntityClass) {
            itemView.apply {
//            tvAlbumName.text = entityClass.album
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
                    if (viewchildfirst is EditText && viewchildfirst.text.toString()contentEquals("Product")) {
                        if (viewchildsecond is EditText)
                            productName = viewchildsecond.text.toString()
                    } else if (viewchildfirst is EditText && viewchildfirst.text.toString()contentEquals("Price")) {
                        if (viewchildsecond is EditText)
                            productprice = viewchildsecond.text.toString()
                    } else if (viewchildfirst is EditText && viewchildfirst.text.toString()contentEquals("Barcode")) {
                        if (viewchildsecond is EditText)
                            productBarCode = viewchildsecond.text.toString()
                    } else if (viewchildfirst is EditText && viewchildfirst.text.toString()contentEquals("Quantity")) {
                        if (viewchildsecond is EditText)
                            productQuantity = viewchildsecond.text.toString()
                    } else if (viewchildfirst is EditText && viewchildfirst.text.toString()contentEquals("Others")) {
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