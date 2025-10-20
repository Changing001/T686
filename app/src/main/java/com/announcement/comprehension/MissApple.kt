package com.announcement.comprehension

import android.os.Bundle
import androidx.activity.addCallback
import com.announcement.comprehension.databinding.AppleMainBinding
import com.announcement.comprehension.nav.Nav2Ext

class MissApple : BaseActivity() {

    private lateinit var binding: AppleMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StoreKv.init(this)
        binding = AppleMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback {  }
    }


    val ext = Nav2Ext(intent?.extras?: Bundle())
    override fun onResume() {
        super.onResume()
        ext. load(this)
    }

    override fun onPause() {
        ext.close()
        super.onPause()
    }


}