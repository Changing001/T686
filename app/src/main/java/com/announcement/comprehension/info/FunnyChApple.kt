package com.announcement.comprehension.info

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.announcement.comprehension.HoursApple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FunnyChApple : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            delay(2333L)
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                try {
                    startActivity(Intent(this@FunnyChApple, HoursApple::class.java))
                    finish()
                } catch (t: Throwable) {

                }
            }
        }
    }
}