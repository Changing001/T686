package com.announcement.comprehension.nav


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.announcement.comprehension.BaseFragment
import com.announcement.comprehension.databinding.FragmentNav2Binding

class Nav2Fragment : BaseFragment() {


    companion object {
        fun newInstance() = Nav2Fragment()
    }


    private var _binding: FragmentNav2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val model = ViewModelProvider(this).get(NavViewModel::class.java)
        _binding = FragmentNav2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.share.setOnClickListener {
            model.share(mActivity)
        }
        binding.privac.setOnClickListener {
            model.privac(mActivity)
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}