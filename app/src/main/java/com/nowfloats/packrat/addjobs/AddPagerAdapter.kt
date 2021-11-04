package com.nowfloats.packrat.addjobs

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nowfloats.packrat.R
import com.nowfloats.packrat.clickInterface.ClicTabItemListener
import com.nowfloats.packrat.roomdatabase.EntityClass

class AddPagerAdapter(
    fm: FragmentManager,
    private val context: Context,
    private var imageList: List<EntityClass>
) : FragmentStatePagerAdapter(fm) {
    private val fragments = ArrayList<Fragment>()

    override fun getCount(): Int {
        return imageList.size
    }


    override fun getItem(position: Int): Fragment {
//        var prolist: List<Int> = ArrayList<Int>()
//        return ProductDataFragment.newInstance(context, position, prolist)
        return fragments.get(position)
    }

    fun getTabView(position: Int, clickListener: ClicTabItemListener): View {
        val v = LayoutInflater.from(context).inflate(R.layout.image_items, null)
        val product_imageview = v.findViewById<View>(R.id.product_imageview) as ImageView
        val cross_imageview = v.findViewById<View>(R.id.close_imageview) as ImageView
        product_imageview.setImageURI(Uri.parse(imageList.get(position).path))
        cross_imageview.setOnClickListener {
            clickListener.onClickCross(imageList.get(position).id)
        }
        return v
    }
    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }
}