package com.announcement.comprehension.info

import com.announcement.comprehension.StoreKv

class ImageScanner {
    private val scannedImages = mutableListOf<ImageModel>()
    private var isScanning = false
    private val scanListeners = mutableListOf<OnScanListener>()

    interface OnScanListener {
        fun onScanStarted()
        fun onImageFound(image: ImageModel)
        fun onScanCompleted(images: List<ImageModel>)
    }

    fun addScanListener(listener: OnScanListener) {
        scanListeners.add(listener)
    }

    fun removeScanListener(listener: OnScanListener) {
        scanListeners.remove(listener)
    }

  suspend  fun scanImages() {
        if (isScanning) return

        isScanning = true
        scannedImages.clear()

        // 通知所有监听器扫描开始
        for (listener in scanListeners) {
            listener.onScanStarted()
        }


      runCatching {
          val set = StoreKv.getFileList()

          set.map {
              val arr = it.split("#*#*")
              scannedImages.add(ImageModel(arr.get(0),arr.get(1).toLong()))
          }

          // 扫描完成后，按照修改日期倒序排序
          scannedImages.sortByDescending { it.lastModified }

          // 通知所有监听器扫描完成
          for (listener in scanListeners) {
              listener.onScanCompleted(scannedImages)
          }
      }.onFailure { it.printStackTrace() }

      isScanning = false
    }

    fun stopScan() {
        isScanning = false
    }
}