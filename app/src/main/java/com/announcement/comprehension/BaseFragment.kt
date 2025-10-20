package com.announcement.comprehension

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment


open class BaseFragment : Fragment() {



    lateinit var mActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity

    }

//    fun <T : View> findViewById(@IdRes id: Int): T {
//        val root:View? = view
//        return  root!!.findViewById(id)
//    }
//
//    fun finish(){
//        mActivity.finish()
//    }
//
//    fun toast(msg:String){
//        Toast.makeText(context?.applicationContext,msg, Toast.LENGTH_SHORT).show();
//    }

}