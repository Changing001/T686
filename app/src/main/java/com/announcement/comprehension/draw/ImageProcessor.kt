package com.announcement.comprehension.draw


import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
/*
还有问题，例如颜色1和颜色10颜色非常接近但是没有合并（原因我猜测是，只比较了相邻颜色的值如果接近就合并，当颜色在列表中间隔比较远就没有比较）
 */
class ImageProcessor {
    companion object {
        // 将图片转换为马赛克，并提取颜色列表
        fun processImage(bitmap: Bitmap, pixelSize: Int): ProcessedImageResult {
            // 调整图片大小以适应处理
            val scaledBitmap = scaleBitmap(bitmap, pixelSize)

            val width = scaledBitmap.width
            val height = scaledBitmap.height

            // 创建马赛克图片和颜色映射
            val mosaicBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val grayScaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val colorMap = Array(height / pixelSize) { IntArray(width / pixelSize) }

            // 提取颜色
            val colorCountMap = HashMap<Int, Int>()
            val pixelColors = mutableListOf<Int>()

            for (i in 0 until height step pixelSize) {
                for (j in 0 until width step pixelSize) {
                    // 计算马赛克块的平均颜色
                    val averageColor = getAverageColor(scaledBitmap, j, i, pixelSize)

                    // 填充马赛克块
                    for (y in 0 until pixelSize) {
                        for (x in 0 until pixelSize) {
                            if (i + y < height && j + x < width) {
                                mosaicBitmap.setPixel(j + x, i + y, averageColor)

                                // 创建灰阶图像
                                val gray = Color.red(averageColor) * 0.299f +
                                        Color.green(averageColor) * 0.587f +
                                        Color.blue(averageColor) * 0.114f
                                grayScaleBitmap.setPixel(j + x, i + y, Color.rgb(gray.toInt(), gray.toInt(), gray.toInt()))
                            }
                        }
                    }

                    pixelColors.add(averageColor)
                    colorCountMap[averageColor] = colorCountMap.getOrDefault(averageColor, 0) + 1
                }
            }

            // 提取主要颜色
            val initialColors = colorCountMap.entries
                .sortedByDescending { it.value }
                .map { it.key }
                .distinct()
                .take(100) // 获取最多25种初始颜色
                .toList()

            // 合并相似颜色
            val sortedColors = mergeSimilarColors(initialColors, 100) // 30是颜色相似度阈值（较小值表示更严格）

            // 创建颜色映射（从颜色到编号）
            val colorToIndex = HashMap<Int, Int>()
            for (i in sortedColors.indices) {
                colorToIndex[sortedColors[i]] = i + 1 // 编号从1开始
            }

            // 填充颜色映射数组
            var rowIndex = 0
            var colIndex = 0
            val rows = colorMap.size
            val cols = if (rows > 0) colorMap[0].size else 0

            for (i in 0 until pixelColors.size) {
                // 添加边界检查以防止索引越界
                if (rowIndex < rows && colIndex < cols) {
                    val color = pixelColors[i]
                    val closestColor = findClosestColor(color, sortedColors)
                    colorMap[rowIndex][colIndex] = colorToIndex[closestColor] ?: 1
                }

                colIndex++
                if (colIndex >= width / pixelSize) {
                    colIndex = 0
                    rowIndex++
                }
            }

            // 调整灰阶图像的亮度以确保编号清晰可见
            adjustGrayscaleContrast(grayScaleBitmap)

            return ProcessedImageResult(mosaicBitmap, grayScaleBitmap, sortedColors, colorMap, pixelSize)
        }

        // 调整位图大小以适应马赛克处理
        private fun scaleBitmap(bitmap: Bitmap, pixelSize: Int): Bitmap {
            val targetWidth = 300 // 目标宽度
            val targetHeight = (bitmap.height * targetWidth.toFloat() / bitmap.width).toInt()

            val matrix = Matrix()
            matrix.postScale(targetWidth.toFloat() / bitmap.width, targetHeight.toFloat() / bitmap.height)

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        // 获取指定区域的平均颜色
        private fun getAverageColor(bitmap: Bitmap, x: Int, y: Int, size: Int): Int {
            var red = 0
            var green = 0
            var blue = 0
            var count = 0

            for (i in 0 until size) {
                for (j in 0 until size) {
                    val pixelX = x + j
                    val pixelY = y + i

                    if (pixelX < bitmap.width && pixelY < bitmap.height) {
                        val color = bitmap.getPixel(pixelX, pixelY)
                        red += Color.red(color)
                        green += Color.green(color)
                        blue += Color.blue(color)
                        count++
                    }
                }
            }

            if (count == 0) return Color.WHITE

            return Color.rgb(red / count, green / count, blue / count)
        }

        // 查找最接近的颜色
        private fun findClosestColor(targetColor: Int, colorList: List<Int>): Int {
            var closestColor = colorList[0]
            var minDistance = Int.MAX_VALUE

            for (color in colorList) {
                val distance = calculateColorDistance(targetColor, color)
                if (distance < minDistance) {
                    minDistance = distance
                    closestColor = color
                }
            }

            return closestColor
        }

        // 计算颜色之间的距离 - 使用加权欧几里得距离进一步提高颜色匹配准确性
        private fun calculateColorDistance(color1: Int, color2: Int): Int {
            val r1 = Color.red(color1)
            val g1 = Color.green(color1)
            val b1 = Color.blue(color1)

            val r2 = Color.red(color2)
            val g2 = Color.green(color2)
            val b2 = Color.blue(color2)

            // 计算颜色通道差异
            val rDiff = r1 - r2
            val gDiff = g1 - g2
            val bDiff = b1 - b2

            // 使用加权欧几里得距离，考虑人眼对不同颜色通道的敏感度
            // 人眼对绿色最敏感，红色次之，蓝色最弱
            val redWeight = 0.3f
            val greenWeight = 0.59f
            val blueWeight = 0.11f

            // 计算加权距离（使用平方和避免开方运算）
            val weightedDistance = (rDiff * rDiff * redWeight +
                    gDiff * gDiff * greenWeight +
                    bDiff * bDiff * blueWeight).toInt()

            return weightedDistance
        }

        // 合并相似颜色 - 修改后的算法可以比较所有颜色对
        private fun mergeSimilarColors(colors: List<Int>, similarityThreshold: Int): List<Int> {
            if (colors.isEmpty()) return emptyList()

            val mergedColors = mutableListOf<Int>()
            val used = BooleanArray(colors.size) { false }

            // 使用K-means聚类的简化版本，确保所有相似颜色都被比较
            for (i in colors.indices) {
                if (used[i]) continue

                // 选择当前颜色作为聚类中心
                val currentColor = colors[i]
                val cluster = mutableListOf(currentColor)
                used[i] = true

                // 第一轮：寻找与当前中心直接相似的颜色
                for (j in colors.indices) {
                    if (i != j && !used[j] && calculateColorDistance(currentColor, colors[j]) < similarityThreshold) {
                        cluster.add(colors[j])
                        used[j] = true
                    }
                }

                // 第二轮：寻找与聚类中任何颜色相似的颜色，确保所有相似颜色都被合并
                var newColorsAdded: Boolean
                do {
                    newColorsAdded = false
                    for (j in colors.indices) {
                        if (!used[j]) {
                            // 检查当前未使用的颜色是否与聚类中的任何颜色相似
                            val isSimilar = cluster.any { calculateColorDistance(it, colors[j]) < similarityThreshold }
                            if (isSimilar) {
                                cluster.add(colors[j])
                                used[j] = true
                                newColorsAdded = true
                            }
                        }
                    }
                } while (newColorsAdded) // 继续直到没有新的相似颜色被添加

                // 计算聚类中心颜色（平均颜色）
                mergedColors.add(calculateAverageColorFromList(cluster))
            }

            return mergedColors
        }

        // 计算颜色列表的平均颜色
        private fun calculateAverageColorFromList(colors: List<Int>): Int {
            var red = 0
            var green = 0
            var blue = 0

            for (color in colors) {
                red += Color.red(color)
                green += Color.green(color)
                blue += Color.blue(color)
            }

            val count = colors.size
            return Color.rgb(red / count, green / count, blue / count)
        }

        // 调整灰阶图像的对比度
        private fun adjustGrayscaleContrast(bitmap: Bitmap) {
            val contrastFactor = 1.5f // 对比度因子

            for (x in 0 until bitmap.width) {
                for (y in 0 until bitmap.height) {
                    val color = bitmap.getPixel(x, y)
                    var gray = Color.red(color)

                    // 调整对比度
                    gray = ((gray - 128) * contrastFactor + 128).toInt()
                    gray = gray.coerceIn(0, 255)

                    bitmap.setPixel(x, y, Color.rgb(gray, gray, gray))
                }
            }
        }
    }

    // 处理结果数据类
    data class ProcessedImageResult(
        val mosaicBitmap: Bitmap,
        val grayScaleBitmap: Bitmap,
        val colors: List<Int>,
        val colorMap: Array<IntArray>,
        val pixelSize: Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ProcessedImageResult

            if (mosaicBitmap != other.mosaicBitmap) return false
            if (grayScaleBitmap != other.grayScaleBitmap) return false
            if (colors != other.colors) return false
            if (!colorMap.contentDeepEquals(other.colorMap)) return false
            if (pixelSize != other.pixelSize) return false

            return true
        }

        override fun hashCode(): Int {
            var result = mosaicBitmap.hashCode()
            result = 31 * result + grayScaleBitmap.hashCode()
            result = 31 * result + colors.hashCode()
            result = 31 * result + colorMap.contentDeepHashCode()
            result = 31 * result + pixelSize
            return result
        }
    }
}