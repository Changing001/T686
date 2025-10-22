package com.announcement.comprehension.t

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

object ClosePages : Application.ActivityLifecycleCallbacks {

    var list= arrayListOf<Activity>()

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
        Log.e("T686 App", "onActivityCreated ${activity::class.java.simpleName}")
        action?.invoke(activity)
        list.add(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.e("T686 App", "onActivityDestroyed ${activity::class.java.simpleName}")
        list.remove(activity)
    }


    override fun onActivityStarted(activity: Activity) {
        Log.e("T686 App", "onActivityStarted ${activity::class.java.simpleName}")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.e("T686 App", "onActivityResumed ${activity::class.java.simpleName}")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.e("T686 App", "onActivityPaused ${activity::class.java.simpleName}")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.e("T686 App", "onActivityStopped ${activity::class.java.simpleName}")
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {
        Log.e("T686 App", "onActivitySaveInstanceState ${activity::class.java.simpleName}")
    }
}