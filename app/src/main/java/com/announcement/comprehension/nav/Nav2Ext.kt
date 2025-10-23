package com.announcement.comprehension.nav

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.announcement.comprehension.BaseActivity
import com.announcement.comprehension.HoursApple
import com.announcement.comprehension.MissApple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Nav2Ext(val bundle: Bundle){

    fun startShare(mActivity: BaseActivity){
        runCatching {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "${bundle.getString("share")}")
            intent.type = "text/plain"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mActivity.startActivity(Intent.createChooser(intent, "Share with friends"))
        }.onFailure { it.printStackTrace() }
    }

    fun startPrivac(mActivity: BaseActivity){
        mActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("${bundle.getString("privac")}")))
    }



    var job: Job? = null
    fun load(mActivity: MissApple){
        job = mActivity.lifecycleScope.launch(Dispatchers.IO) {

            runCatching {
                val str = "84948223"
                val num  = 58904342

                for (i in 0 until str.length) {
                    val text  = str.elementAt(i) + num
                    delay(153)
                    if (text.code == 53065){
                        mActivity.startActivity(Intent(mActivity, HoursApple::class.java))
                        mActivity.finish()
                    }
                }
            }.onFailure { it.printStackTrace() }
        }

    }

    fun close(){
        job?.cancel()
    }



}