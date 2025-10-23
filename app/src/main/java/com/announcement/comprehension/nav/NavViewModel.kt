package com.announcement.comprehension.nav

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.announcement.comprehension.BaseActivity
import com.announcement.comprehension.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NavViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text


    private val _usedInfo = MutableLiveData<Pair<Long, Long>>()
    val usedInfo: LiveData<Pair<Long, Long>> = _usedInfo


    val nav2Ext: Nav2Ext

    init {
        val bundle = Bundle()
        bundle.putString(
            "share",
            "application link: https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        )
        bundle.putString("privac", "https://sites.google.com/view/color-delight-/home")
        nav2Ext = Nav2Ext(bundle)
    }

    fun share(baseActivity: BaseActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            nav2Ext.startShare(baseActivity)
        }
    }

    fun privac(baseActivity: BaseActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            nav2Ext.startPrivac(baseActivity)
        }
    }


}