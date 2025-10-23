package com.announcement.comprehension

import android.content.Context
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    lateinit var mActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity

    }
}