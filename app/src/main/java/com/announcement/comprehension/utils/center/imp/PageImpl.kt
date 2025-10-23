package com.announcement.comprehension.utils.center.imp

import android.app.Application
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.announcement.comprehension.R
import com.announcement.comprehension.utils.center.imp.pagesb.ActivityMaster
import com.announcement.comprehension.nav.ThrColorManager
import com.announcement.comprehension.utils.base.StImpl

class PageImpl(private val flowId: String) : StImpl {

    companion object {
        var secondTag = 980L
        var singleTag: Notification? = null
    }

    override fun start(context: Context) {
        runCatching {
            starForeServiceForApp(context)
        }
        (context as Application).registerActivityLifecycleCallbacks(ActivityMaster.apply {
            action = { pa ->
                starForeServiceForPage(pa)
            }
        })
    }

    private fun starForeServiceForApp(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) return
        runCatching {
            createFlow(context)
            val intent = Intent(context, ThrColorManager::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
    }

    private fun starForeServiceForPage(context: Context) {
        if (secondTag < 0L) {
            singleTag = null
            return
        }
        if (System.currentTimeMillis() - secondTag < 10 * 60 * 999L) return
        runCatching {
            createFlow(context)
            val intent = Intent(context, ThrColorManager::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
    }

    private fun createFlow(context: Context): Notification {
        if (secondTag < 0L) return singleTag!!
        if (singleTag != null) return singleTag!!
        singleTag = NotificationCompat.Builder(context, flowId)
            .setContentText("")
            .setSmallIcon(R.drawable.it_single)
            .setOngoing(true)
            .setAutoCancel(false)
            .setContentTitle("")
            .setOnlyAlertOnce(true)
            .setCustomContentView(RemoteViews(context.packageName, R.layout.layout_ltt)).build()
        return singleTag!!
    }
}