package com.announcement.comprehension.utcs

import android.content.Context
import android.util.Log
import com.announcement.comprehension.BuildConfig
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import com.appsflyer.AppsFlyerLib
import com.bytedance.sdk.openadsdk.api.init.PAGMConfig
import com.bytedance.sdk.openadsdk.api.init.PAGMSdk
import com.thinkup.core.api.TUSDK

class IdPro(private val context: Context) {

    fun listStart(userId: String) {
        PAGMSdk.init(
            context,
            PAGMConfig.Builder().appId("8580262").supportMultiProcess(false).build(),
            null
        )


        AppLovinSdk.getInstance(context).settings.setVerboseLogging(true)


        val initConfig =
            AppLovinSdkInitializationConfiguration.builder("HJFhpJAwSFJc4vKhpSiTESSEs1rhEL_ONC9UU5cc7qLd22D_FuuhMAeMiI0CVFV72QZ3JBGOL7XSQHMWp6krE2")
                .setMediationProvider(AppLovinMediationProvider.MAX)
                .setTestDeviceAdvertisingIds(arrayListOf("668b1aaf-0900-4714-9272-1ac69a299577"))
                .build()
        AppLovinSdk.getInstance(context).initialize(initConfig) { sdkConfig ->
            AppLovinSdk.getInstance(context).showMediationDebugger()
        }

        Log.e(
            "T686 App",
            "Af id:${"5MiZBZBjzzChyhaowfLpyR"} user Id:${userId} App Version:${BuildConfig.VERSION_NAME}"
        )

        AppsFlyerLib.getInstance().apply {
            init("5MiZBZBjzzChyhaowfLpyR", null, context)
            setCustomerUserId(userId)
            start(context)
            logSession(context)
        }

        TUSDK.setNetworkLogDebug(true)
        TUSDK.init(context, "h670e13c4e3ab6", "ac360a993a659579a11f6df50b9e78639")
    }
}