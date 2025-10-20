package com.announcement.comprehension

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.announcement.comprehension.databinding.AppleResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ResultApple : BaseActivity() {

    companion object {
        private var tmpData: String = ""
        private var mTime: Long = 0L
        fun start(mActivity: BaseActivity,data: String,time:Long){
            tmpData = data
            mTime = time
            mActivity.startActivity(Intent(mActivity, ResultApple::class.java))
        }
    }

    private lateinit var binding: AppleResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AppleResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveInfo()

        binding.back.setOnClickListener { finish() }

        binding.timeUsed.text = "Time used: ${Util.formatTimeToMMSS(mTime)}"


        binding.download.setOnClickListener {
            Util.saveImageToGallery(this, File(tmpData))
        }
        binding.share.setOnClickListener {
            Util.shareFile(this, tmpData)
        }


        val bitmap = BitmapFactory.decodeFile(tmpData)

        binding.image.setImageBitmap(bitmap)

    }



    fun saveInfo(){
        lifecycleScope.launch(Dispatchers.IO) {
           val set = StoreKv.getFileList()
            set.add("$tmpData#*#*$mTime")
            StoreKv.putFileList(set)
        }
    }

}