//package com.test
//
//import android.app.Activity
//import android.content.Context
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import com.announcement.comprehension.t.Fnc
//import com.applovin.mediation.MaxAd
//import com.applovin.mediation.MaxAdListener
//import com.applovin.mediation.MaxError
//import com.applovin.mediation.ads.MaxInterstitialAd
//import com.applovin.sdk.AppLovinSdk
//import com.thinkup.core.api.TUBiddingListener
//import com.thinkup.core.api.TUBiddingResult
//import com.thinkup.interstitial.unitgroup.api.CustomInterstitialAdapter
//import org.json.JSONObject
//import java.util.UUID
//
//
////com.uak.kn.JhAdapter
//
//class TestAdapter : CustomInterstitialAdapter() {
//    private var placementId: String = ""
//    private var interstitialAd: MaxInterstitialAd? = null
//    private var maxAd: MaxAd? = null
//    override fun loadCustomNetworkAd(
//        p0: Context?,
//        p1: MutableMap<String, Any>?,
//        p2: MutableMap<String, Any>?
//    ) {
//
//    }
//
//    override fun startBiddingRequest(
//        context: Context?,
//        serverExtra: Map<String?, Any?>?,
//        localExtra: Map<String?, Any?>?,
//        biddingListener: TUBiddingListener?
//    ): Boolean {
//        Fnc.dexLog(
//            "topOn App startBiddingRequest: ${
//                JSONObject(serverExtra).optString("unit_id").toString()
//            }"
//        )
//        if (serverExtra != null && JSONObject(serverExtra).optString("unit_id").isNotEmpty()) {
//            placementId = JSONObject(serverExtra).optString("unit_id")
//            startBid(biddingListener)
//        } else {
//            biddingListener?.onC2SBiddingResultWithCache(
//                TUBiddingResult.fail("pangle unit_id null"),
//                null
//            )
//            mLoadListener?.onAdLoadError("", "pangle initSdk fail")
//        }
//        return true
//    }
//
//    private fun startBid(biddingListener: TUBiddingListener?) {
//        interstitialAd = MaxInterstitialAd(placementId)
//        interstitialAd?.setListener(object : MaxAdListener {
//            override fun onAdLoaded(p0: MaxAd) {
//                Fnc.dexLog("topOn App onAdLoaded: ${p0.revenue * 1000}")
//                maxAd = p0
//                val ec = p0.revenue * 1000
//                biddingListener?.onC2SBiddingResultWithCache(
//                    TUBiddingResult.success(ec, UUID.randomUUID().toString(), null), null
//                )
//                mLoadListener?.onAdCacheLoaded()
//            }
//
//            override fun onAdDisplayed(p0: MaxAd) {
//                Fnc.dexLog("topOn App onAdDisplayed")
//                mImpressListener?.onInterstitialAdShow()
//                interstitialAd = null
//            }
//
//            override fun onAdHidden(p0: MaxAd) {
//                Fnc.dexLog("topOn App onAdHidden")
//                mImpressListener?.onInterstitialAdClose()
//            }
//
//            override fun onAdClicked(p0: MaxAd) {
//                Fnc.dexLog("topOn App onAdClicked")
//                mImpressListener?.onInterstitialAdClicked()
//            }
//
//            override fun onAdLoadFailed(p0: String, p1: MaxError) {
//                Fnc.dexLog("topOn App onAdLoadFailed: ${p1?.code}  ${p1?.message}")
//                biddingListener?.onC2SBiddingResultWithCache(TUBiddingResult.fail(p1.message), null)
//                mLoadListener?.onAdLoadError("${p1.code}", p1.message)
//            }
//
//            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
//                Fnc.dexLog("topOn App onAdDisplayFailed: ")
//                mImpressListener?.onInterstitialAdVideoError("${p1?.code}", "${p1?.message}")
//                interstitialAd = null
//            }
//        })
//        interstitialAd?.loadAd()
//    }
//
//    override fun destory() {
//        interstitialAd?.setListener(null)
//        interstitialAd?.destroy()
//        interstitialAd = null
//        maxAd = null
//    }
//
//    override fun isAdReady(): Boolean {
//        return interstitialAd?.isReady ?: false
//    }
//
//    override fun getNetworkPlacementId(): String {
//        return placementId
//    }
//
//    override fun getNetworkSDKVersion(): String {
//        runCatching {
//            return AppLovinSdk.VERSION
//        }
//        return ""
//    }
//
//    override fun getNetworkName(): String {
//        return "max_${maxAd?.networkName}"
//    }
//
//    override fun show(p0: Activity?) {
//        Fnc.dexLog("topOn App show:${p0} ${interstitialAd == null}")
//        if (p0 != null) {
//            Fnc.dexLog("topOn App show:Real")
////            interstitialAd?.showAd(p0)
//            interstitialAd?.showAd(p0.window?.decorView as ViewGroup,(p0 as AppCompatActivity).lifecycle,p0)
//        } else {
//            Fnc.dexLog("topOn App show:AdClose")
//            mImpressListener?.onInterstitialAdClose()
//        }
//    }
//}