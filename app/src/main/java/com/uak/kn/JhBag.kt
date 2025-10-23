package com.uak.kn

import com.applovin.mediation.MaxAd
import com.applovin.mediation.ads.MaxInterstitialAd

class JhBag {
    var pId: String = ""
    var iAd: MaxInterstitialAd? = null
    var tpMAd: MaxAd? = null

    fun close() {
        iAd?.setListener(null)
        iAd?.destroy()
        iAd = null
        tpMAd = null
    }
}