package com.announcement.comprehension.info

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.announcement.comprehension.HoursApple
import com.announcement.comprehension.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FunnyChApple : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            delay(2500L)
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                startActivity(Intent(this@FunnyChApple, HoursApple::class.java))
                finish()
            }
        }
    }
}