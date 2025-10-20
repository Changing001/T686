package com.announcement.comprehension

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

object Util {
//    inline fun log(call:() -> String){
//        Log.i("HHHH",call.invoke())
//    }

    fun formatTimeToMMSS(seconds: Long): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", minutes, secs)
    }



    fun shareFile(mActivity: BaseActivity,filePath: String){
        runCatching {
            val mContext: Context = mActivity.applicationContext
            val uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".file_provider", File(filePath))
            // 创建分享 Intent
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("Image/*")
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mActivity.startActivity(Intent.createChooser(shareIntent, "Share Image"))
        }.onFailure { it.printStackTrace() }

    }


    fun saveImageToGallery(context: Context, imageFile: File) {
        runCatching {
            val filename = imageFile.name
            val mimeType = "image/${imageFile.extension.lowercase()}"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val resolver = context.contentResolver
            val uri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let {
                resolver.openOutputStream(it).use { outStream ->
                    imageFile.inputStream().use { inStream ->
                        inStream.copyTo(outStream!!)
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)
                }

                Toast.makeText(context, "The image has been saved to the album", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(context, "Save failed", Toast.LENGTH_SHORT).show()
            }
        }.onFailure { it.printStackTrace() }

    }




}