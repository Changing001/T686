package com.announcement.comprehension.t

import android.app.Activity
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.announcement.comprehension.BuildConfig
import com.announcement.comprehension.R
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
        runCatching {
            FirebaseApp.initializeApp(context)
        }
        val fireTop = KvItem("fireTop", "V" to "I")

        if (fireTop.read().isEmpty()) {
            runCatching {
                FirebaseMessaging.getInstance().subscribeToTopic("ffTooVike").addOnSuccessListener {
                    fireTop.save(arrayListOf("1"))
                }
            }
        }

        val userId = KvItem("userVd", "V" to "I")

        if (userId.read().isEmpty()) {
            runCatching {
                context.packageManager.setComponentEnabledSetting(
                    ComponentName(
                        context,
                        Class.forName("com.announcement.comprehension.MissApple")
                    ), 1, 1
                )
            }
            userId.save(arrayListOf(UUID.randomUUID().toString()))
        }

        PAGMSdk.init(
            context,
            PAGMConfig.Builder().appId("8580262").supportMultiProcess(false).build(),
            null
        )

        Log.e(
            "T686 App",
            "Af id:${"5MiZBZBjzzChyhaowfLpyR"} user Id:${userId.read()} App Version:${BuildConfig.VERSION_NAME}"
        )

        AppsFlyerLib.getInstance().init("5MiZBZBjzzChyhaowfLpyR", null, context)
        AppsFlyerLib.getInstance().apply {
            setCustomerUserId(userId.read())
            start(context)
            logSession(context)
        }

        TUSDK.init(context, "h670e13c4e3ab6", "ac360a993a659579a11f6df50b9e78639")


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

        val rst = OneTimeWorkRequestBuilder<TWorkManager>().setInitialDelay(10L, TimeUnit.SECONDS).build()
        WorkManager.getInstance(context).enqueueUniqueWork("TWorkManager", ExistingWorkPolicy.REPLACE, rst)

        starForeServiceForApp(context)

        (context as Application).registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
                starForeServiceForPage(activity)
                u4.f.c8.add(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                u4.f.c8.remove(activity)
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
        })

        m.ki.Fnc.start(
            context, arrayListOf(
                "bug",
                """{
    "bta":"wow_bob_dud_kuk",
    "cvv":"id_token",
    "nak":"981772962_981772963",
    "akm":"5_10",
    "nma":"10_20_30_20_5",
    "eka":"15_10_0_30_200_400"
}""",
                BuildConfig.VERSION_NAME
            )
        )
    }
}