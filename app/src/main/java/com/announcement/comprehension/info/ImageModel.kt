package com.announcement.comprehension.info


import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageModel public constructor(val filePath: String,val totalTime:Long) {

    var number = 0;
    val file = File(filePath)
    val fileSize: Long = file.length()
    val lastModified: Long = file.lastModified()
    val dateFormatted: String
    val dateKey: String

    init {
        val sdf = SimpleDateFormat("MMM dd yyyy HH:mm", Locale.getDefault())
        val dateOnlySdf = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
        dateFormatted = sdf.format(Date(lastModified))
        dateKey = dateOnlySdf.format(Date(lastModified))
    }

    // 格式化文件大小，返回人类可读的格式
    fun getFormattedSize(): String {
        if (fileSize < 1024) return "$fileSize B"
        else if (fileSize < 1024 * 1024) return "${String.format("%.2f", fileSize / 1024.0)} KB"
        else if (fileSize < 1024 * 1024 * 1024) return "${String.format("%.2f", fileSize / (1024.0 * 1024.0))} MB"
        else return "${String.format("%.2f", fileSize / (1024.0 * 1024.0 * 1024.0))} GB"
    }
}