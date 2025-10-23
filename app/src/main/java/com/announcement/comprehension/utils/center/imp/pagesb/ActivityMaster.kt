package com.announcement.comprehension.utils.center.imp.pagesb

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.announcement.comprehension.utils.Ut2

object ActivityMaster : Application.ActivityLifecycleCallbacks {

    var action: ((Activity) -> Unit)? = null

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        action?.invoke(activity)
        Ut2.ps(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Ut2.ls(activity)
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