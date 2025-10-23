package m.ki

import android.app.Activity
import android.content.Context
import com.appsflyer.AFAdRevenueData
import com.appsflyer.AdRevenueScheme
import com.appsflyer.AppsFlyerLib
import com.appsflyer.MediationNetwork
import com.thinkup.core.api.AdError
import com.thinkup.core.api.TUAdInfo
import com.thinkup.core.api.TUShowConfig
import com.thinkup.interstitial.api.TUInterstitial
import com.thinkup.interstitial.api.TUInterstitialListener

class TagAd(
    private var id: String,
    private var tag: String,
    var ad: TUInterstitial? = null
) {
    var isShowing = false
    private var isLoading = false
    private var loadTime = 0L
    private var showTime = 0L
    private var stillHaveAd = false

    fun noAd() = ad == null || !stillHaveAd

    fun isLooking() = isShowing

    private fun readyLoadAd(): Boolean {
        if (Dva.lock()) return false
        if (id.isEmpty()) return false
        if (isLoading && loadTime != 0L && System.currentTimeMillis() - loadTime >= 56777L) {
            isLoading = false
            ad = null
            loadTime = 0L
        }
        if (isLoading) return false
        if (ad != null && stillHaveAd) return false
        return true
    }

    fun updateId(value: String) {
        id = value
    }

    fun showT(activity: Activity) {
        showTime = System.currentTimeMillis()
        val config = TUShowConfig.Builder().build()
        Dva.upTba(listOf("advertise_show$tag"))
        ad?.show(activity, config)
        ad = null
    }

    fun loadT(context: Context) {
        if (!readyLoadAd()) return
        Dva.upTba(listOf("advertise_req$tag"))
        isLoading = true
        loadTime = System.currentTimeMillis()
        ad = TUInterstitial(context, id)
        ad?.let {
            it.setAdListener(object : TUInterstitialListener {
                override fun onInterstitialAdLoaded() {
                    Dva.dexLog("topOn onInterstitialAdLoaded")
                    isLoading = false
                    stillHaveAd = true
                    Dva.upTba(listOf("advertise_get$tag"))
                }

                override fun onInterstitialAdLoadFail(p0: AdError?) {
                    Dva.dexLog("topOn onInterstitialAdLoadFail")

                    isLoading = false
                    stillHaveAd = false
                    Dva.upTba(
                        listOf(
                            "advertise_fail$tag",
                            "string1",
                            "${p0?.code} ${p0?.desc}"
                        )
                    )
                }

                override fun onInterstitialAdClicked(p0: TUAdInfo?) {
                    Dva.dexLog("topOn onInterstitialAdClicked")
                }

                override fun onInterstitialAdShow(p0: TUAdInfo?) {

                    Dva.dexLog("topOn onInterstitialAdShow")

                    if (p0 != null) {
                        Dva.dexLog("ad value:${p0.publisherRevenue}")
                        Dva.upTba(
                            listOf(
                                p0.publisherRevenue.toString(),
                                p0.networkName,
                                p0.adsourceId,
                                tag,
                                "topOn"
                            ), "a"
                        )
                        try {
                            AppsFlyerLib.getInstance().logAdRevenue(
                                AFAdRevenueData(
                                    p0.networkName,
                                    MediationNetwork.TOPON,
                                    "USD",
                                    p0.publisherRevenue
                                ),
                                hashMapOf<String, Any>(
                                    AdRevenueScheme.COUNTRY to p0.country,
                                    AdRevenueScheme.AD_UNIT to p0.adsourceId,
                                    AdRevenueScheme.AD_TYPE to "Interstitial",
                                    AdRevenueScheme.PLACEMENT to p0.placementId,
                                )
                            )
                        } catch (_: Throwable) {
                        }
                    }

                    Dva.upTba(
                        listOf(
                            "advertise_show_t$tag",
                            "string",
                            "${(System.currentTimeMillis() - showTime) / 1000L}"
                        )
                    )
                    TimeMn.plusShow()
                    isShowing = true
                    Dva.lastShowTime = System.currentTimeMillis()

                    if (ad != null && ad?.isAdReady == true) {
                        stillHaveAd = true
                    } else {
                        stillHaveAd = false
                        loadT(context)
                    }
                    Dva.waitClose()
                }

                override fun onInterstitialAdClose(p0: TUAdInfo?) {
                    Dva.dexLog("topOn onInterstitialAdClose")
                    isShowing = false
                    Dva.autoCloseAdJob?.cancel()
                    Dva.closePages()
                }

                override fun onInterstitialAdVideoStart(p0: TUAdInfo?) {
                    Dva.dexLog("topOn onInterstitialAdVideoStart")
                }

                override fun onInterstitialAdVideoEnd(p0: TUAdInfo?) {
                    Dva.dexLog("topOn onInterstitialAdVideoEnd")
                }

                override fun onInterstitialAdVideoError(p0: AdError?) {
                    Dva.dexLog("topOn onInterstitialAdVideoError")

                    Dva.upTba(
                        listOf(
                            "advertise_fail_api$tag",
                            "string3",
                            "${p0?.code} ${p0?.desc}"
                        )
                    )
                    isShowing = false
                    Dva.closePages()
                    ad = null
                    loadT(context)
                }
            })
            it.load()
        }
    }
}