package com.announcement.comprehension.nav


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.announcement.comprehension.BaseFragment
import com.announcement.comprehension.Util
import com.announcement.comprehension.databinding.FragmentNav1Binding
import com.announcement.comprehension.info.ImageAdapter
import com.announcement.comprehension.info.ImageModel
import com.announcement.comprehension.info.ImageScanner
import com.announcement.comprehension.info.MySpanSizeLookup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Nav1Fragment : BaseFragment(), ImageScanner.OnScanListener{


    companion object {
        fun newInstance() = Nav1Fragment()
    }


    private var _binding: FragmentNav1Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /// Use the ViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val model = ViewModelProvider(this).get(NavViewModel::class.java)
        _binding = FragmentNav1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    private lateinit var imageScanner: ImageScanner
    private val allImages = mutableListOf<ImageModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初始化RecyclerView


        binding.recyclerView.layoutManager = GridLayoutManager(context, 2).apply { spanSizeLookup = MySpanSizeLookup(flatList) }


        // 初始化图片扫描器
        imageScanner = ImageScanner()
        imageScanner.addScanListener(this)

        startScanImages()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun startScanImages() {
        // 清空之前的图片列表
        allImages.clear()
        // 开始扫描图片
        lifecycleScope.launch(Dispatchers.IO){
            imageScanner.scanImages()
        }
    }


    // 图片扫描开始时的回调
    override fun onScanStarted() {}

    // 发现新图片时的回调
    override fun onImageFound(image: ImageModel) {
        allImages.add(image)
    }

    // 图片扫描完成时的回调
    override fun onScanCompleted(images: List<ImageModel>) {

        lifecycleScope.launch(Dispatchers.Main){

            // 将图片按照日期分组
//                val groupedImages = fileList.groupBy { it.dateKey }
            val groupedImages = images.groupBy { it.dateKey }

            imageList.clear()
            val flist = createFlatList(groupedImages)

            flatList.clear()
            flatList.addAll(flist)

            // 创建并设置适配器
            val adapter = ImageAdapter(mActivity, flatList)
            binding.recyclerView.adapter = adapter


            binding.compl.text = "Completed: ${imageList.size}"


            if(imageList.size==0){
                binding.avg.text = "Average Time: 00:00"
            }else{
                val uTime = imageList.sumOf { it.totalTime }
                val avgTime:Long = uTime/imageList.size
//                Util.log { "uTime=$uTime avgTime=$avgTime" }
                binding.avg.text = "Average Time: ${Util.formatTimeToMMSS(avgTime)}"
            }



        }




    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止扫描并移除监听器
        imageScanner.stopScan()
        imageScanner.removeScanListener(this)
    }

    val flatList:MutableList<Any> = mutableListOf()
    val imageList:MutableList<ImageModel> = mutableListOf()

    // 扁平化数据列表
    private fun createFlatList(groupedImages: Map<String, List<ImageModel>>): MutableList<Any> {
        val list = mutableListOf<Any>()

        // 按照日期降序排序
        val sortedDates = groupedImages.keys.sortedDescending()

        for (date in sortedDates) {
            // 添加日期头部
            list.add(date)
            // 添加该日期下的所有图片

            val tmpList = groupedImages[date] ?: emptyList()
            tmpList.forEachIndexed { index, imageModel ->
                imageModel.number = index
            }

            imageList.addAll(tmpList)

            list.addAll(tmpList)
        }

        return list
    }

}