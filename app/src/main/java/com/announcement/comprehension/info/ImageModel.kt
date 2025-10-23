package com.announcement.comprehension.info


import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageModel(val filePath: String, val totalTime:Long) {

    var number = 0;
    val file = File(filePath)
    val lastModified: Long = file.lastModified()
    val dateFormatted: String
    val dateKey: String

    init {
        val sdf = SimpleDateFormat("MMM dd yyyy HH:mm", Locale.getDefault())
        val dateOnlySdf = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
        dateFormatted = sdf.format(Date(lastModified))
        dateKey = dateOnlySdf.format(Date(lastModified))
    }
}