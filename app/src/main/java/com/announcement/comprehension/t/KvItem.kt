package com.announcement.comprehension.t

class KvItem(private val key: String, private val keyPlus: Pair<String, String>) {

    private fun createKey(): String {
        return key.replace(keyPlus.first, keyPlus.second)
    }

    fun save(list: ArrayList<String>) {
        ColorApp.mmkv.encode(createKey(), list[0])
    }

    fun read(): String {
        return ColorApp.mmkv.decodeString(createKey(), "") ?: ""
    }

}