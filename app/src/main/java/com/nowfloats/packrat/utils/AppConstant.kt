package com.nowfloats.packrat.utils

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.provider.Settings

class AppConstant {
    companion object{
        val APP_FOLDER_NAME = "PackRat"
        val APP_BASE_URL = "app_end_point_will_go_here"
    }

    fun getRandomCollectionId(context: Context):String{
        val androidId = Settings.Secure.getString(context.getContentResolver(),
            Settings.Secure.ANDROID_ID);
        return androidId+"_"+System.currentTimeMillis()+"_" +( 0 until 1000).random()
    }
}