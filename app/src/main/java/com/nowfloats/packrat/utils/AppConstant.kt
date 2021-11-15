package com.nowfloats.packrat.utils

import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.content.ContentUris
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.ContextCompat.getSystemService

import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


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
        const val CREATED_BY = "bankesh@zibal.com"
        const val CREATED_BY_NAME = "bankesh"
        const val IN_PROGRESS = "Uploading...."
        const val INTERNET_CONNECTION_MESSAGE = "Please check you internet connection"
        const val IMAGE_PREIVEW_TAG = "IMAGE_PREVIEW"
        const val DASHBOARD_TAG = "DASHBOARD"
        const val ADD_PRODUCT_TAG = "ADD_PRODUCT"
        const val COLLECTION_ID = "COLLECTION_ID"
        const val NETWORK_CALL_DELAY: Long = 2000L

        @JvmStatic
        @SuppressLint("NewApi")
        fun getPath(context: Context, uri: Uri): String? {
            val isKitKat: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    return if ("primary".equals(type, ignoreCase = true)) {
                        Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    } else { // non-primary volumes e.g sd card
                        var filePath = "non"
                        //getExternalMediaDirs() added in API 21
                        val extenal = context.externalMediaDirs
                        for (f in extenal) {
                            filePath = f.absolutePath
                            if (filePath.contains(type)) {
                                val endIndex = filePath.indexOf("Android")
                                filePath = filePath.substring(0, endIndex) + split[1]
                            }
                        }
                        filePath
                    }
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

         fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                                  selectionArgs: Array<String>?): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(
                column
            )
            try {
                cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs,
                    null)
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } catch (e: java.lang.Exception) {
            } finally {
                cursor?.close()
            }
            return null
        }

          fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

          fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

          fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }
    }

    fun getRandomCollectionId(context: Context):String{
        val androidId = Settings.Secure.getString(context.getContentResolver(),
            Settings.Secure.ANDROID_ID);
        return androidId+"_"+System.currentTimeMillis()+"_" +( 0 until 1000).random()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return capabilities?.hasCapability(NET_CAPABILITY_INTERNET) == true
    }
}