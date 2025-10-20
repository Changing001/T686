package com.announcement.comprehension.t

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ForeSv : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        runCatching {
            RealTStart.timePoint = System.currentTimeMillis()
            startForeground(1968,  RealTStart.checkMsg)
        }
        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? = null
}