package com.announcement.comprehension.info

import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup

class MySpanSizeLookup(private val flatList: MutableList<Any>) : SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return if (flatList[position] is String) {
            2
        } else {
            1
        }
    }
}