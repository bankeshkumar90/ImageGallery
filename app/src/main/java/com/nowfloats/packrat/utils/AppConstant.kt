package com.nowfloats.packrat.utils

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.provider.Settings

class AppConstant {
    companion object{
        const val VIEW_DELAY: Long = 200L
        const val APP_FOLDER_NAME = "PackRat"
        const val APP_BASE_URL = "app_end_point_will_go_here"
        const val IMAGE_LIST = "imagePathList"
        const val IMAGE_URI = "image_uri"
        const val REQ_CAMERA_CODE = 100
        const val REQ_GALLERY_CODE = 101
        const val REQUEST_TYPE = "request_type"
    }

    fun getRandomCollectionId(context: Context):String{
        val androidId = Settings.Secure.getString(context.getContentResolver(),
            Settings.Secure.ANDROID_ID);
        return androidId+"_"+System.currentTimeMillis()+"_" +( 0 until 1000).random()
    }
}