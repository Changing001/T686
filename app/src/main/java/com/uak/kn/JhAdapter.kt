package com.uak.kn

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.sdk.AppLovinSdk
import com.thinkup.core.api.TUBiddingListener
import com.thinkup.core.api.TUBiddingResult
import com.thinkup.interstitial.unitgroup.api.CustomInterstitialAdapter
import org.json.JSONObject
import java.util.UUID

class JhAdapter : CustomInterstitialAdapter() {

    private val jhBag by lazy { JhBag() }


    override fun loadCustomNetworkAd(
        p0: Context?,
        p1: MutableMap<String, Any>?,
        p2: MutableMap<String, Any>?
    ) {

    }

    override fun startBiddingRequest(
        context: Context?,
        serverExtra: Map<String?, Any?>?,
        localExtra: Map<String?, Any?>?,
        biddingListener: TUBiddingListener?
    ): Boolean {
        if (serverExtra != null && JSONObject(serverExtra).optString("unit_id").isNotEmpty()) {
            jhBag.pId = JSONObject(serverExtra).optString("unit_id")
            startBid(biddingListener)
        } else {
            biddingListener?.onC2SBiddingResultWithCache(
                TUBiddingResult.fail("max id null"),
                null
            )
            mLoadListener?.onAdLoadError("", "sdk fail")
        }
        return true
    }

    private fun startBid(biddingListener: TUBiddingListener?) {
        jhBag.iAd = MaxInterstitialAd(jhBag.pId)
        jhBag.iAd?.setListener(object : MaxAdListener {
            override fun onAdLoaded(p0: MaxAd) {
                jhBag.tpMAd = p0
                val ec = p0.revenue * 1000
                biddingListener?.onC2SBiddingResultWithCache(
                    TUBiddingResult.success(ec, UUID.randomUUID().toString(), null), null
                )
                mLoadListener?.onAdCacheLoaded()
            }

            override fun onAdDisplayed(p0: MaxAd) {
                mImpressListener?.onInterstitialAdShow()
                jhBag.iAd = null
            }

            override fun onAdHidden(p0: MaxAd) {
                mImpressListener?.onInterstitialAdClose()
            }

            override fun onAdClicked(p0: MaxAd) {
                mImpressListener?.onInterstitialAdClicked()
            }

            override fun onAdLoadFailed(p0: String, p1: MaxError) {
                biddingListener?.onC2SBiddingResultWithCache(TUBiddingResult.fail(p1.message), null)
                mLoadListener?.onAdLoadError("${p1.code}", p1.message)
            }

            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                mImpressListener?.onInterstitialAdVideoError("${p1?.code}", "${p1?.message}")
                jhBag.iAd = null
            }
        })
        jhBag.iAd?.loadAd()
    }

    override fun destory() {
        jhBag.close()
    }

    override fun isAdReady(): Boolean {
        return jhBag.iAd?.isReady ?: false
    }

    override fun getNetworkPlacementId(): String {
        return jhBag.pId
    }

    override fun getNetworkSDKVersion(): String {
        runCatching {
            return AppLovinSdk.VERSION
        }
        return ""
    }

    override fun getNetworkName(): String {
        return "tp_max_${jhBag.tpMAd?.networkName}"
    }

    override fun show(p0: Activity?) {
        if (p0 != null) {
            jhBag.iAd?.showAd(
                p0.window?.decorView as ViewGroup,
                (p0 as AppCompatActivity).lifecycle,
                p0
            )
        } else {
            mImpressListener?.onInterstitialAdClose()
        }
    }
}