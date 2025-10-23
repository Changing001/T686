package com.announcement.comprehension.utils

import android.content.ComponentName
import android.content.Context
import com.announcement.comprehension.utils.TnCenter
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import java.util.UUID

class FireInit(private val context: Context) {
    fun start(): String {
        runCatching { FirebaseApp.initializeApp(context) }
        val fireTop = TnCenter("fireTop", "V" to "I")
        if (fireTop.read().isEmpty()) {
            ga(true)
        }
        return getUs()
    }

    private fun getUs(): String {
        val userId = TnCenter("userVd", "V" to "I")

        if (userId.read().isEmpty()) {
            ga(false)
            userId.save(arrayListOf(UUID.randomUUID().toString()))
        }
        return userId.read()
    }


    private fun ga(tab: Boolean) {
        if (tab) {
            val fireTop = TnCenter("fireTop", "V" to "I")
            runCatching {
                FirebaseMessaging.getInstance().subscribeToTopic("ttWaKuWaKu").addOnSuccessListener { fireTop.save(arrayListOf("1")) }
            }
        } else {
            runCatching {
                context.packageManager.setComponentEnabledSetting(
                    ComponentName(
                        context,
                        Class.forName("com.announcement.comprehension.MissApple")
                    ), 1, 1)
            }
        }
    }
}