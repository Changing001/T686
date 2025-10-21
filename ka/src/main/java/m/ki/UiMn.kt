package m.ki

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xik.af
import kotlin.random.Random

class UiMn : Application.ActivityLifecycleCallbacks {

    private var count = 0

    override fun onActivityCreated(page: Activity, s: Bundle?) {
        when (page::class.java.simpleName) {
            "ReWaPaFrt" -> {
                Dva.nowPopFail = 0
                val adDelay = try {
                    Random.nextLong(Dva.popMinDelay, Dva.popMaxDelay)
                } catch (_: Throwable) {
                    200L
                }
                Dva.upTba(arrayListOf("ad_done", "delay", adDelay.toString()))
                try { af.z6(page) } catch (_: Throwable) { }
                (page as AppCompatActivity).onBackPressedDispatcher.addCallback {}
                page.lifecycleScope.launch {
                    if (Dva.useByte) {
                        if (!Dva.byteAd1.noAd()) {
                            delay(adDelay)
                            Dva.byteAd1.showP(page)
                        } else if (!Dva.byteAd2.noAd()) {
                            delay(adDelay)
                            Dva.byteAd2.showP(page)
                        } else {
                            delay(200)
                            page.finishAndRemoveTask()
                        }
                    } else {
                        if (!Dva.plusAd1.noAd()) {
                            delay(adDelay)
                            Dva.plusAd1.showT(page)
                        } else if (!Dva.plusAd2.noAd()) {
                            delay(adDelay)
                            Dva.plusAd2.showT(page)
                        } else {
                            delay(200)
                            page.finishAndRemoveTask()
                        }
                    }
                }
            }
        }
    }

    override fun onActivityDestroyed(a: Activity) {
        if (a::class.java.simpleName == "ReWaPaFrt") {
            try { (a.window.decorView as ViewGroup).removeAllViews() } catch (_: Throwable) { }
        }
    }

    override fun onActivityStarted(a: Activity) {
        if (Dva.afterT) count++
    }

    override fun onActivityStopped(a: Activity) {
        if (Dva.afterT && --count <= 0) {
            count = 0
            Dva.closePages()
        }
    }

    override fun onActivitySaveInstanceState(a: Activity, o: Bundle) {}
    override fun onActivityResumed(a: Activity) {}
    override fun onActivityPaused(a: Activity) {}
}