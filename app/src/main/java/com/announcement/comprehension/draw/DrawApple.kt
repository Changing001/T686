package com.announcement.comprehension.draw


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.widget.Button
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.drawToBitmap
import com.announcement.comprehension.BaseActivity
import com.announcement.comprehension.R
import com.announcement.comprehension.ResultApple
import com.announcement.comprehension.databinding.AppleDrawBinding
import com.announcement.comprehension.databinding.ItemColorBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/*
如果你有暂停 / 恢复
你需要在暂停时记录一个 pauseOffset，恢复时加上它：
long pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
// 恢复时
chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);

然后获取用时时，还是：
long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
 */
class DrawApple : BaseActivity() {

    companion object {
        private var tmpData: Int = 0
        fun start(mActivity: BaseActivity,data: Int){
            tmpData = data
            mActivity.startActivity(Intent(mActivity,DrawApple::class.java))
        }
    }

    private lateinit var drawingView: DrawingView

    private lateinit var resetButton: Button
    private lateinit var exampleImageView: ImageView


    lateinit var binding:AppleDrawBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AppleDrawBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback {  }

        binding.back.setOnClickListener { finish() }

        // 初始化视图组件
        drawingView = findViewById(R.id.drawingView)

        resetButton = findViewById(R.id.resetButton)
        exampleImageView = findViewById(R.id.exampleImageView)

        resetButton.setOnClickListener {
//            drawingView.resetDrawing()
            saveImageToExternalPrivateStorage(mActivity,drawingView.drawToBitmap(),321)
        }

        // 设置绘图完成监听器
        drawingView.setOnDrawingCompleteListener(object : DrawingView.OnDrawingCompleteListener {
            override fun onDrawingComplete() {
                // 当所有方格都被填涂时显示提示
                binding. chronometer.stop()
                val elapsedMillis: Long = SystemClock.elapsedRealtime() -  binding. chronometer.getBase()
                val seconds = elapsedMillis / 1000

//                Toast.makeText(mActivity.applicationContext, "恭喜！您已完成填色！" + "已计时 $seconds 秒", Toast.LENGTH_LONG).show()

                saveImageToExternalPrivateStorage(mActivity,drawingView.drawToBitmap(),seconds)
            }
        })

        val bitmap = BitmapFactory.decodeResource(resources, tmpData)
        processAndDisplayImage(bitmap)

        binding. chronometer.setBase(SystemClock.elapsedRealtime()); // 设置起始时间
        binding. chronometer.start(); // 开始计时
    }


    private fun processAndDisplayImage(bitmap: Bitmap) {
        runCatching {

            // 处理图片：转换为马赛克并提取颜色
            val pixelSize = 15 // 马赛克块大小
            val result = ImageProcessor.processImage(bitmap, pixelSize)

            // 显示示例图片预览
//            exampleImageView.setImageBitmap(result.mosaicBitmap)
//            exampleImageView.visibility = ImageView.VISIBLE

            // 设置颜色选择器
            load_gl(result.colors)
            // 初始化绘图视图
            drawingView.setImageAndColors(
                result.mosaicBitmap,
                result.grayScaleBitmap,
                result.colors,
                result.colorMap,
                result.pixelSize
            )

            // 默认选择第一个颜色
            if (result.colors.isNotEmpty()) {
                drawingView.setCurrentColorIndex(0)
            }

        }.onFailure { it.printStackTrace()}
    }


    val linViewList:MutableList<ItemColorBinding> = mutableListOf()
    fun load_gl(colors:List<Int>){
        val lin = binding.lin
        lin.removeAllViews()

        for ((index, colorId) in colors.withIndex()) {

            val item = ItemColorBinding.inflate(layoutInflater,lin,false)
            linViewList.add(item)
            item.image.setImageDrawable(colorId.toDrawable())
            item.tv.text = "${index+1}"

            if (lastColorIndex == index){
                item.image.strokeWidth = dp2px(2.0f)
            }else{
                item.image.strokeWidth = dp2px(0.0f)
            }

            item.root.setOnClickListener {
                val tmp = linViewList.get(lastColorIndex)
                tmp.image.strokeWidth = dp2px(0.0f)

                item.image.strokeWidth = dp2px(2.0f)

                lastColorIndex = index

                drawingView.setCurrentColorIndex(index)
            }

            lin.addView(item.root)
        }
    }

    var lastColorIndex = 0




    fun saveImageToExternalPrivateStorage(context: Context, bitmap: Bitmap,timeUsed:Long) {
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (directory != null) {
            val filename = "IMG_${System.currentTimeMillis()}.jpeg"
            val file = File(directory, filename)
            try {
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }


            ResultApple.start(mActivity,file.absolutePath,timeUsed)
            finish()
        }
    }

}