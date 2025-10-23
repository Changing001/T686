package com.announcement.comprehension.nav

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.announcement.comprehension.utils.center.imp.PageImpl

class ThrColorManager : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        runCatching {
            startForeground(upTime(), PageImpl.singleTag)
        }
        return START_STICKY
    }


    private fun upTime(): Int {
        PageImpl.secondTag = System.currentTimeMillis()
        return if (PageImpl.secondTag > 0) {
            1968
        } else {
            1996
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}