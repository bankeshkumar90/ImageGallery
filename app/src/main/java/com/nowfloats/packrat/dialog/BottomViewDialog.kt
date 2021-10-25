package com.nowfloats.packrat.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nowfloats.packrat.R
import com.nowfloats.packrat.camera.CameraActivity
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
            startActivity(Intent(context, CameraActivity::class.java))
            Toast.makeText(context, "First Button Clicked", Toast.LENGTH_SHORT).show()
        }
        add_gallery.setOnClickListener {
            //handle click event
            startActivity(Intent(context, CameraActivity::class.java))
            Toast.makeText(context, "Second Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}