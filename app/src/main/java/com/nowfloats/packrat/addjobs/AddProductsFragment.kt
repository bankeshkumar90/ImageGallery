package com.nowfloats.packrat.addjobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nowfloats.packrat.R

class AddProductsFragment : Fragment() {

    private var mPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPage = arguments!!.getInt(ARG_PAGE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.product_item, container, false)
//        val textView = view as TextView
//        textView.text = "Fragment " + mPage
        return view
    }

    companion object {
        val ARG_PAGE = "ARG_FRAG_PAGE"

        fun newInstance(page: Int): AddProductsFragment {
            val args = Bundle()
            args.putInt(ARG_PAGE, page)
            val fragment = AddProductsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}
