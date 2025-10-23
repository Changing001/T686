package com.announcement.comprehension.utils

import com.announcement.comprehension.ColorApp

class TnCenter(private val key: String, private val keyPlus: Pair<String, String>) {

    private fun createKey(): String {
        return key.replace(keyPlus.first, keyPlus.second)
    }

    fun save(list: ArrayList<String>) {
        ColorApp.kvCenter.encode(createKey(), list[0])
    }

    fun read(): String {
        return ColorApp.kvCenter.decodeString(createKey(), "") ?: ""
    }
}