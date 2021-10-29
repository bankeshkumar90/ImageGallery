package com.nowfloats.packrat.homescreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nowfloats.packrat.R
import com.nowfloats.packrat.bottomsheetdialog.BottomViewDialog
import kotlinx.android.synthetic.main.activity_main.*

class DashBoardFragment:Fragment() {
    private lateinit var bottomViewDialog: BottomViewDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.dashboard_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewsAndListeners()
    }
    private fun initViewsAndListeners() {
        ll_shelfView.setOnClickListener {
            dispatchTakePictureIntent()
        }
        ll_ProductView.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }
    @SuppressLint("WrongConstant")
    private fun dispatchTakePictureIntent() {
        bottomViewDialog = BottomViewDialog()
        bottomViewDialog.setStyle( 0, R.style.BottomSheetDialog)
        bottomViewDialog.show(fragmentManager!!, BottomViewDialog.TAG)

    }
}