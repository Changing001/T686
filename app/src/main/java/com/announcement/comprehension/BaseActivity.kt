package com.announcement.comprehension

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


open class BaseActivity : AppCompatActivity() {

    companion object {
//        var barInsets: Rect = Rect(0,0,0,0)
    }

    lateinit var mActivity: BaseActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        // 隐藏状态栏（全屏模式）
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val barStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.WHITE, detectDarkMode = { return@auto false})
        enableEdgeToEdge(statusBarStyle = barStyle, navigationBarStyle = barStyle)
//        enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT, detectDarkMode = { return@auto false}))
    }


    var isOnResume = false
    override fun onResume() {
        super.onResume()
        isOnResume = true
    }

    override fun onPause() {
        isOnResume = false
        super.onPause()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        windowInsets()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        windowInsets()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        windowInsets()
    }

    fun windowInsets(){
        val main = findViewById<View>(R.id.container)
        if (main!=null){
            ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
                val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//                barInsets.left = systemBars.left
//                barInsets.top = systemBars.top
//                barInsets.right = systemBars.right
//                barInsets.bottom = systemBars.bottom
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        //        val back: View?= findViewById<View>(R.id.back)
//        back?.setOnClickListener { finish() }
    }




    fun dp2px( dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,resources.displayMetrics)
    }



}

//import android.util.Log
//object Util {
//    inline fun log(call:() -> String){
//        Log.i("HHHH",call.invoke())
//    }
//}