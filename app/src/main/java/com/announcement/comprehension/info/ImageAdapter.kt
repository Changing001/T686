package com.announcement.comprehension.info


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.announcement.comprehension.BaseActivity
import com.announcement.comprehension.R
import com.announcement.comprehension.Util
import com.bumptech.glide.Glide

class ImageAdapter(private val mActivity: BaseActivity, private val flatList :MutableList<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_IMAGE = 1

    // 创建一个扁平化的列表，包含日期头部和图片项
//    private val flatList = createFlatList()



    override fun getItemViewType(position: Int): Int {
        return if (flatList[position] is String) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            // 创建日期头部ViewHolder
            val view = LayoutInflater.from(mActivity).inflate(R.layout.item_info_header, parent, false)
            HeaderViewHolder(view)
        } else {
            // 创建图片项ViewHolder
            val view = LayoutInflater.from(mActivity).inflate(R.layout.item_info_image, parent, false)
            ImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            // 绑定日期头部数据
            val date = flatList[position] as String
            holder.dateTextView.text = formatDateHeader(date)
        } else if (holder is ImageViewHolder) {
            // 绑定图片数据
            val imageModel = flatList[position] as ImageModel

            holder.rt.setBackgroundResource(if (imageModel.number % 2 == 0) R.drawable.it_left_r16 else R.drawable.it_right_r16)

            // 使用Glide加载图片
            Glide.with(mActivity)
                .load(imageModel.filePath)
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView)

            // 设置文件路径和大小
//            holder.info.text = imageModel.getFormattedSize() +"\n" +imageModel.filePath
            holder.usTime.text = "Time used: ${Util.formatTimeToMMSS(imageModel.totalTime)}"

            holder.mView.setOnClickListener {
                InfoApple.start(mActivity,imageModel)
            }
        }
    }

    override fun getItemCount(): Int {
        return flatList.size
    }

    // 日期头部ViewHolder
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    // 图片项ViewHolder
    class ImageViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
//        val info: TextView = itemView.findViewById(R.id.info)
        val usTime: TextView = itemView.findViewById(R.id.usTime)
        val rt: FrameLayout = itemView.findViewById(R.id.rt)
    }

    // 格式化日期头部显示
    private fun formatDateHeader(date: String): String {
        // 这里可以根据需要自定义日期格式显示
        return date
    }
}