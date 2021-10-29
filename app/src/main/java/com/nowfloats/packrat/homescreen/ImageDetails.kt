package com.nowfloats.packrat.homescreen

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nowfloats.packrat.R
import kotlinx.android.synthetic.main.activity_image_details.*

class ImageDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)

        /*
        get the image path url from the intent and set it on the UI
         */
        val url = intent.getStringExtra("uri")
        val uri = Uri.parse(url)
        imgDisplayImage.setImageURI(uri)
    }
}