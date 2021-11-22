package com.nowfloats.packrat.utils

import android.annotation.SuppressLint
import android.app.Activity
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
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern
import android.content.SharedPreferences

import android.preference.PreferenceManager
import java.io.File


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
        const val CREATED_BY = ""
        const val CREATED_BY_NAME = "system"
        const val IN_PROGRESS = "Uploading...."
        const val INTERNET_CONNECTION_MESSAGE = "Please check you internet connection"
        const val IMAGE_PREIVEW_TAG = "IMAGE_PREVIEW"
        const val DASHBOARD_TAG = "DASHBOARD"
        const val ADD_PRODUCT_TAG = "ADD_PRODUCT"
        const val COLLECTION_ID = "COLLECTION_ID"
        const val NETWORK_CALL_DELAY: Long = 2000L
        //const val JOB_STATUS = "JOB_STATUS"
        const val JOB_PREIVEW_TAG = "JOB_PREIVIEW"
        const val FILE_UPLOADED = "FILE_UPLOADED"
        const val FETCH_STATUS = "Fetching Job Status...."
        const val REQUEST_META_DATA = "REQUEST_METADATA"
        const val META_DATA_SAVED_ON_SERVER = "METADATA_SAVED"
        const val SETTINGS_TAG = "SETTINGS_TAG"
        private const val SHARED_PREFERENCES_KEY = "com.pack.rat"
        const val COMPRESSION_TYPE = "CompressionType"
        const val NONE = "NONE"
        const val MEDIUM = "MEDIUM"
        const val STATUS_SUCCESS = "SUCCEEDED"//JobModel(imagePath=/storage/emulated/0/Pictures/1636717183877.jpg, jobStatus=SUCCEEDED, savedImagePath=content://com.android.providers.media.documents/document/image%3A40775, uri=/storage/emulated/0/Pictures/1636717183877.jpg, imageId=3A40775)
        const val CAMERA_PERMISSION_REQUIRED = 104
        const val MEMORY_PERMISSION = "Memory Permission Required"
        const val PERMISSION_DENIED_MESSAGE = "Permissions are denied, please allow all required permission from settings"
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

    fun getLastStringAfter(lastCharAfterWhichString: String, completeString:String):String{
        return completeString.substring(completeString.lastIndexOf(lastCharAfterWhichString)+ 1)
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }

    /*fun isValidEmailId(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }*/
    fun isValidEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun saveKeysByTagINLocalPrefs(context: Context, tag:String, valueToBeSaved:String){
        val privateSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val editor = privateSharedPreferences.edit()
        editor.putString(tag, valueToBeSaved)
        editor.apply()
    }

    fun getValuesByTagFromLocalPrefs(context: Context, tag:String, default:String):String?{
        val privateSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return privateSharedPreferences.getString(tag, default).toString()
    }

    fun ifFileExists(context: Context, imagePath:String):Boolean{
        var fileExists = false
        var actualPath = ""+getPath(context , Uri.parse(imagePath))
        val file = File(actualPath)
        if(file.exists()){
            fileExists = true
        }
        return fileExists
    }
}