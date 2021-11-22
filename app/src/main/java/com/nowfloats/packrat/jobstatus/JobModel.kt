package com.nowfloats.packrat.jobstatus

import android.net.Uri

data class JobModel (var imagePath: String = "",
                     var jobStatus:String ="",
                     var savedImagePath:String = "",
                     var savedTimeStamp:String = "",

                     var uri: Uri =  Uri.parse(""),
                     var imageId :String = "")