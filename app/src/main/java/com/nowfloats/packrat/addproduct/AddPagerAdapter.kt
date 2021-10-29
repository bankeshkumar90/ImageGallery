package com.nowfloats.packrat.addproduct

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nowfloats.packrat.R
import com.nowfloats.packrat.roomdatabase.EntityClass

class AddPagerAdapter(
    fm: FragmentManager,
    private val context: Context,
    private var imageList: List<EntityClass>
) : FragmentPagerAdapter(fm) {

    private val tabTitles = imageList//arrayOf("Tab 1", "Tab 2", "Tab 3")

    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(position: Int): Fragment {
        return AddProductFragment.newInstance(position + 1)
    }

    fun getTabView(position: Int): View {
        val v = LayoutInflater.from(context).inflate(R.layout.image_items, null)
        val product_imageview = v.findViewById<View>(R.id.product_imageview) as ImageView
        val cross_imageview = v.findViewById<View>(R.id.close_imageview) as ImageView
        product_imageview.setImageURI(Uri.parse(imageList.get(position).path))
        cross_imageview.setOnClickListener {
            Toast.makeText(context,"position "+position,Toast.LENGTH_LONG).show()
        }
//        tabName.text = tabTitles[position]
        return v
    }

}