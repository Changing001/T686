package com.announcement.comprehension

import android.os.Bundle
import com.announcement.comprehension.databinding.AppleHomeBinding
import com.announcement.comprehension.nav.Nav0Fragment
import com.announcement.comprehension.nav.Nav1Fragment
import com.announcement.comprehension.nav.Nav2Fragment

class HoursApple : BaseActivity() {

    private lateinit var binding: AppleHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AppleHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nav0 = Nav0Fragment.newInstance()
        val nav1 = Nav1Fragment.newInstance()
        val nav2 = Nav2Fragment.newInstance()

        val fm = supportFragmentManager
        val tr = fm.beginTransaction()
        tr.replace(R.id.fragment_container,nav0)
        tr.commitNow()

        binding.nav0.setOnClickListener {
            navIndex = 0
            binding.nav0.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.home_nav_t0_select,0,R.mipmap.home_nav_ind)
            binding.nav1.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.home_nav_t1,0,0)
            binding.nav2.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.home_nav_t2,0,0)
            val tr = fm.beginTransaction()
            tr.replace(R.id.fragment_container,nav0)
            tr.commitNow()
        }
        binding.nav1.setOnClickListener {
            navIndex = 1
            binding.nav0.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.home_nav_t0,0,0)
            binding.nav1.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.home_nav_t1_select,0,R.mipmap.home_nav_ind)
            binding.nav2.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.home_nav_t2,0,0)
            val tr = fm.beginTransaction()
            tr.replace(R.id.fragment_container,nav1)
            tr.commitNow()
        }
        binding.nav2.setOnClickListener {
            navIndex = 2
            binding.nav0.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.home_nav_t0,0,0)
            binding.nav1.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.home_nav_t1,0,0)
            binding.nav2.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.home_nav_t2_select,0,R.mipmap.home_nav_ind)
            val tr = fm.beginTransaction()
            tr.replace(R.id.fragment_container,nav2)
            tr.commitNow()
        }

    }

    var navIndex = 0
}