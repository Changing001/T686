package m.ki

import android.app.Activity
import android.content.Context
import com.appsflyer.AFAdRevenueData
import com.appsflyer.AdRevenueScheme
import com.appsflyer.AppsFlyerLib
import com.appsflyer.MediationNetwork
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAd
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdInteractionCallback
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdLoadCallback
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialRequest
import com.bytedance.sdk.openadsdk.api.model.PAGAdEcpmInfo
import com.bytedance.sdk.openadsdk.api.model.PAGErrorModel

class PagAd(
    private var id: String,
    private var tag: String,
    private var ad: PAGInterstitialAd? = null
) {
    private var isLoading = false
    private var isShowing = false
    private var loadTime = 0L
    private var showTime = 0L

    fun updateId(data: String) {
        id = data
    }

    fun looking() = isShowing
    fun noAd() = ad == null

    private fun canLoad(): Boolean {
        if (Dva.lock()) return false
        if (id.isEmpty()) return false
        if (isLoading && loadTime != 0L && System.currentTimeMillis() - loadTime >= 57777L) {
            isLoading = false
            ad = null
            loadTime = 0L
        }
        if (isLoading) return false
        if (ad != null) return false
        return true
    }

    fun loadP(context: Context) {
        if (!canLoad()) return
        Dva.upTba(listOf("advertise_req$tag"))
        isLoading = true
        loadTime = System.currentTimeMillis()
        PAGInterstitialAd.loadAd(
            id,
            PAGInterstitialRequest(context),
            object : PAGInterstitialAdLoadCallback {
                override fun onError(p: PAGErrorModel) {
                    isLoading = false
                    Dva.upTba(
                        listOf(
                            "advertise_fail$tag",
                            "string1",
                            "${p.errorCode} ${p.errorMessage}"
                        )
                    )
                }

                override fun onAdLoaded(p: PAGInterstitialAd?) {
                    isLoading = false
                    ad = p
                    Dva.upTba(listOf("advertise_get$tag"))
                }
            })
    }

    fun showP(activity: Activity) {
        ad?.setAdInteractionCallback(object : PAGInterstitialAdInteractionCallback() {
            override fun onAdReturnRevenue(earn: PAGAdEcpmInfo?) {
                if (earn != null) {
                    Dva.dexLog("ad value:${earn.cpm}")
                    Dva.upTba(listOf(earn.cpm, earn.adnName, earn.adUnit, tag, "pangle"), "a")

                    try {
                        AppsFlyerLib.getInstance().logAdRevenue(
                            AFAdRevenueData(
                                earn.placement,
                                MediationNetwork.CUSTOM_MEDIATION,
                                "USD",
                                earn.cpm.toDouble() / 1000
                            ),
                            hashMapOf<String, Any>(
                                AdRevenueScheme.COUNTRY to earn.country,
                                AdRevenueScheme.AD_TYPE to earn.adFormat,
                                AdRevenueScheme.PLACEMENT to earn.segmentID,
                                AdRevenueScheme.AD_UNIT to earn.adUnit
                            )
                        )
                    } catch (_: Throwable) {
                    }
                }
            }

            override fun onAdShowed() {
                Dva.upTba(
                    listOf(
                        "advertise_show_t$tag",
                        "string",
                        "${(System.currentTimeMillis() - showTime) / 1000L}"
                    )
                )
                isShowing = true
                ad = null
                Dva.lastShowTime = System.currentTimeMillis()
                loadP(Dva.app)
                TimeMn.plusShow()


                Dva.waitClose()
            }

            override fun onAdShowFailed(e: PAGErrorModel) {
                Dva.upTba(
                    listOf(
                        "advertise_fail_api$tag",
                        "string3",
                        "${e.errorCode} ${e.errorMessage}"
                    )
                )
                isShowing = false
                activity.finishAndRemoveTask()
                ad = null
                loadP(Dva.app)
            }

            override fun onAdDismissed() {
                isShowing = false
                Dva.autoCloseAdJob?.cancel()
                activity.finishAndRemoveTask()
            }
        })
        showTime = System.currentTimeMillis()
        Dva.upTba(listOf("advertise_show$tag"))
        ad?.show(activity)
    }
}
