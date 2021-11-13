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
import com.nowfloats.packrat.clickInterface.ImageCaptureListner
import com.nowfloats.packrat.clickInterface.OnImageDialogSelector
import com.nowfloats.packrat.imagepreiveiwfragment.ImagePreview
import com.nowfloats.packrat.utils.AppConstant
import kotlinx.android.synthetic.main.layout_modal_bottom_sheet.*

class BottomViewDialog(selectListner: OnImageDialogSelector) : BottomSheetDialogFragment( ) {
      var clickListner = selectListner
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
            //clickListner.onCameraClick()
            dismiss()
            clickListner.onDialogTypeSelected(AppConstant.REQ_CAMERA_CODE)

            //openCameraScreen(AppConstant.REQ_CAMERA_CODE)
        }
        add_gallery.setOnClickListener {
            //handle click event
            //clickListner.onGallery()
            dismiss()
            clickListner.onDialogTypeSelected(AppConstant.REQ_GALLERY_CODE)
            //openCameraScreen(AppConstant.REQ_GALLERY_CODE)
        }
    }

    private fun openCameraScreen(requestCode: Int){
        dismiss()
        val bundle = Bundle()
        bundle.putInt(AppConstant.REQUEST_TYPE, requestCode)
        var imagePreview = ImagePreview()
        imagePreview.arguments = bundle
        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        ft.replace(R.id.fram_dashboard, imagePreview)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(null)
        ft.commit()
    }

}