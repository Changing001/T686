package com.announcement.comprehension.info

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.announcement.comprehension.BaseActivity
import com.announcement.comprehension.Util

import com.announcement.comprehension.databinding.AppleInfoBinding

class InfoApple : BaseActivity() {

    companion object {
        private var tmpData: ImageModel? = null

        fun start(mActivity: BaseActivity,data: ImageModel){
            tmpData = data

            mActivity.startActivity(Intent(mActivity, InfoApple::class.java))
        }
    }

    private lateinit var binding: AppleInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AppleInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener { finish() }

        val model: ImageModel? = tmpData
        if (model!=null){
            binding.title.text = model.dateKey
            binding.timeUsed.text = "Time used: ${Util.formatTimeToMMSS(model.totalTime)}"


            binding.download.setOnClickListener {
                Util.saveImageToGallery(this, model.file)
            }
            binding.share.setOnClickListener {
                Util.shareFile(this, model.filePath)
            }


            val bitmap = BitmapFactory.decodeFile(model.filePath)

            binding.image.setImageBitmap(bitmap)
        }


    }





}