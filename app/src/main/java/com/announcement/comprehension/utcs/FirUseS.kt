package com.announcement.comprehension.utcs

import android.content.ComponentName
import android.content.Context
import com.announcement.comprehension.t.KvItem
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import java.util.UUID

class FirUseS(private val context: Context) {
    fun fieIn(): String {
        runCatching { FirebaseApp.initializeApp(context) }
        val fireTop = KvItem("fireTop", "V" to "I")
        if (fireTop.read().isEmpty()) {
            ga(true)
        }
        return getUs()
    }

    private fun getUs(): String {
        val userId = KvItem("userVd", "V" to "I")

        if (userId.read().isEmpty()) {
            ga(false)
            userId.save(arrayListOf(UUID.randomUUID().toString()))
        }
        return userId.read()
    }


    private fun ga(tab: Boolean) {
        if (tab) {
            val fireTop = KvItem("fireTop", "V" to "I")
            runCatching {
                FirebaseMessaging.getInstance().subscribeToTopic("ffTooVike")
                    .addOnSuccessListener { fireTop.save(arrayListOf("1")) }
            }
        } else {
            runCatching {
                context.packageManager.setComponentEnabledSetting(
                    ComponentName(
                        context,
                        Class.forName("com.announcement.comprehension.MissApple")
                    ), 1, 1
                )
            }
        }
    }
}