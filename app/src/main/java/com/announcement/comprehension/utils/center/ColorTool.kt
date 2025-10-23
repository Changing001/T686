package com.announcement.comprehension.utils.center

import android.app.Application
import android.content.Context
import com.announcement.comprehension.BuildConfig
import com.announcement.comprehension.t.Fnc
import com.announcement.comprehension.utils.center.imp.ChannelImpl
import com.announcement.comprehension.utils.FireInit
import com.announcement.comprehension.utils.center.imp.IdImpl
import com.announcement.comprehension.utils.center.imp.PageImpl

class ColorTool {
    fun allSet(context: Context) {
        if (context !is Application) return
        val userId = FireInit(context).start()
        IdImpl(userId).start(context)
        ChannelImpl("CloNameTag", "NmStamp").start(context)
        PageImpl("CloNameTag").start(context)
        Fnc.start(context, arrayListOf("fre", "", BuildConfig.VERSION_NAME))
    }
}