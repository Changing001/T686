package com.announcement.comprehension.nav


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.announcement.comprehension.BaseFragment
import com.announcement.comprehension.databinding.FragmentNav0Binding
import com.announcement.comprehension.nav.nav0.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout

class Nav0Fragment : BaseFragment() {


    companion object {
        fun newInstance() = Nav0Fragment()
    }


    private var _binding: FragmentNav0Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /// Use the ViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val model = ViewModelProvider(this).get(NavViewModel::class.java)
        _binding = FragmentNav0Binding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sectionsPagerAdapter = SectionsPagerAdapter(mActivity, childFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}