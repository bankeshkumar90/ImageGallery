package com.nowfloats.packrat.bottomsheetdialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nowfloats.packrat.R
import com.nowfloats.packrat.camera.CameraFragment
import com.nowfloats.packrat.camera.GalleryActivity
import kotlinx.android.synthetic.main.layout_modal_bottom_sheet.*

class BottomViewDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "BottomSheetDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_modal_bottom_sheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        add_camera.setOnClickListener {
            //handle click event
//            startActivity(Intent(context, CameraFragment::class.java))

            dismiss()
            val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
            ft.replace(R.id.fram_dashboard, CameraFragment())
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(null)
            ft.commit()
        }
        add_gallery.setOnClickListener {
            //handle click event
            startActivity(Intent(context, GalleryActivity::class.java))
        }
    }

}