package com.nowfloats.packrat.bottomsheetdialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nowfloats.packrat.R
import com.nowfloats.packrat.addjobs.AddProductViewModel
import com.nowfloats.packrat.camera.CameraFragment
import com.nowfloats.packrat.camera.GalleryActivity
import kotlinx.android.synthetic.main.layout_bottom_sheet_product.*
import kotlinx.android.synthetic.main.layout_modal_bottom_sheet.*

class BottomViewAddProductData : BottomSheetDialogFragment() {
    private lateinit var dataDialogModel: AddProductViewModel
    companion object {
        const val TAG = "BottomSheetDialogFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataDialogModel =  ViewModelProviders.of(requireActivity()).get(AddProductViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_bottom_sheet_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        add_pname.setOnClickListener {

        }
        add_price.setOnClickListener {
        }
        add_barcode.setOnClickListener {
        }
        add_quantity.setOnClickListener {
        }
        add_other.setOnClickListener {
        }
    }

}