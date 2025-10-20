package com.announcement.comprehension.nav.nav0

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.announcement.comprehension.BaseFragment
import com.announcement.comprehension.R
import com.announcement.comprehension.databinding.FragmentTabBinding
import com.announcement.comprehension.databinding.ItemImageBinding
import com.announcement.comprehension.draw.DrawApple

class PlaceholderFragment : BaseFragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTabBinding.inflate(inflater, container, false)
        val root = binding.root


        load_gl()

        return root
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    val t0_arr = arrayOf(R.mipmap.t0_a0,R.mipmap.t0_a1,R.mipmap.t0_a2,R.mipmap.t0_a3,R.mipmap.t0_a4,R.mipmap.t0_a5,R.mipmap.t0_a6)
    val t1_arr = arrayOf(R.mipmap.t1_a0,R.mipmap.t1_a1,R.mipmap.t1_a2,R.mipmap.t1_a3)
    val t2_arr = arrayOf(R.mipmap.t2_a0,R.mipmap.t2_a1,R.mipmap.t2_a2,R.mipmap.t2_a3,R.mipmap.t2_a4,R.mipmap.t2_a5,R.mipmap.t2_a6)

    var arr = arrayOf(t0_arr,t1_arr,t2_arr)

    fun load_gl(){
        val tImgIndex = arguments?.getInt(ARG_SECTION_NUMBER) ?: 0
        val t_arr = arr[tImgIndex]
        val gl = binding.gl
        gl.removeAllViews()

        for ((index, resId) in t_arr.withIndex()) {

            val itm = ItemImageBinding.inflate(layoutInflater,gl,false)

            itm.image.setImageResource(resId)


            itm.root.setOnClickListener {
                DrawApple.start(mActivity,resId)
                // startActivity(Intent(mActivity, DrawActivity::class.java))
            }

            gl.addView(itm.root)
        }



    }
}