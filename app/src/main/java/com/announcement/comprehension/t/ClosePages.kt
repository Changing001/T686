package com.announcement.comprehension.t

import android.app.Activity
import android.app.Application
import android.os.Bundle

object ClosePages : Application.ActivityLifecycleCallbacks {

    var list = arrayListOf<Activity>()

    var action: ((Activity) -> Unit)? = null

    fun c2() {
        runCatching {
            ArrayList(list).forEach {
                it.finishAndRemoveTask()
            }
            list.clear()
        }
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        action?.invoke(activity)
        list.add(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        list.remove(activity)
    }


    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {
    }
}