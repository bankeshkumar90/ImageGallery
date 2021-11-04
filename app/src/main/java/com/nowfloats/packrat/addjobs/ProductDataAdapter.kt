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
    private val clickListener: ProdClickListener
) :
    RecyclerView.Adapter<ProductDataAdapter.PickerViewHolder>() {
    private lateinit var ln: LinearLayout

    public var viewList = ArrayList<Int>()
    private var pholder: PickerViewHolder? = null
    private var adpterposion: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_data, parent, false)
        return PickerViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: PickerViewHolder, position: Int) {
//        pholder = holder
        adpterposion = position
        holder.setData(position)
    }

    override fun getItemCount(): Int {
        return viewList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    //updates the latest data of the database
    fun updateList(positionview: Int) {
        viewList?.add(adpterposion)
//        this.viewList.add(positionview)
//        notifyItemInserted(viewList!!.size - 1);
//        notifyDataSetChanged()
        notifyItemChanged(adpterposion)
    }

    fun setData(listview: ArrayList<Int>) {
        viewList = listview
        notifyDataSetChanged()
    }

    fun deleteview(position: Int) {
        viewList!!.removeAt(position)
        notifyItemInserted(viewList!!.size - 1);
    }

    @SuppressLint("NewApi")
    fun setFormView(hint: String, holder: PickerViewHolder, position: Int) {
        pholder = holder
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


        val imageView = ImageView(context)
        var layoutparam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1.8f
        )
        layoutparam.gravity = Gravity.CENTER
        imageView.layoutParams = layoutparam
        imageView.setPadding(0, 0, 5, 0)
        imageView.setImageResource(R.drawable.ic_remove_selected)
        imageView.setOnClickListener {
            if (pholder?.add_form != null) {
                println("values>>>1>$hint $position  ${pholder!!.add_product_count.text}")
                pholder!!.add_form.removeView(linearLayoutParent);
                notifyDataSetChanged()
            }
        }
        linearLayoutParent.addView(imageView)
//        if (pholder?.add_form != null) {
//            var viewsub  = LayoutInflater.from(context).inflate(R.layout.product_itemview,  null)
        println("values>>>1>$hint $position  ${pholder!!.add_product_count.text} ")
        pholder!!.add_form.addView(linearLayoutParent);
//            notifyItemChanged(position)
//        }
    }

    /*fun addExtraTextView() {
        (itemView as ViewGroup).addView(extraTextView, layoutParams)
        viewAdded = true
    }

    fun removeExtraTextView() {
        (itemView as ViewGroup).removeView(extraTextView)
        viewAdded = false
    }*/


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