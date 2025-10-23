package com.announcement.comprehension.utils

import android.app.Activity

object Ut2 {

    var list = arrayListOf<Activity>()

    fun c2() {
        runCatching {
            ArrayList(list).forEach {
                it.finishAndRemoveTask()
            }
            list.clear()
        }
    }

    fun ps(activity: Activity) {
        list.add(activity)
    }

    fun ls(activity: Activity) {
        list.remove(activity)
    }
}