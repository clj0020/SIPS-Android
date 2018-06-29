package com.madmensoftware.sips.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Log
import java.io.File.separator
import android.text.TextUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object PictureUtils {

    /** Finds the correct image file path from URI. **/
    @JvmStatic
    fun getFilePathFromURI(context: Context, contentUri: Uri): String? {
        //copy file and send new file path
        val fileName = getFileName(contentUri)
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + separator + fileName)
            copy(context, contentUri, copyFile)
            return copyFile.getAbsolutePath()
        }
        return null
    }

    /** Creates file name from URI. **/
    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    /** Copies existing file to a temporary file. **/
    fun copy(context: Context, srcUri: Uri, dstFile: File) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
            val outputStream = FileOutputStream(dstFile)
            IOUtils.copy(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}// This class is not publicly instantiable