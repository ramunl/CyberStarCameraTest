package ru.cyberstar.cameracapturetest.tools

import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageReader
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer


class ScreenCapture constructor(val width: Int, val height: Int) {

    val mImageReader: ImageReader = ImageReader.newInstance(width, height, ImageFormat.YUV_420_888, 3)

    fun toJpeg(reader: ImageReader?, path: String = ""): Bitmap? {
        var bitmap: Bitmap? = null
        var fos: FileOutputStream? = null
        var image: Image? = null

        try {
            image = reader!!.acquireNextImage()
            val height = image.height
            val width = image.width

            val planes = image.planes
            val buffer = planes[0].buffer
            val pixelStride = planes[0].pixelStride
            val rowStride = planes[0].rowStride
            val rowPadding = rowStride - pixelStride * width

            // create bitmap
            bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
            //val byteBuffer = ByteBuffer.wrap(buffer.array())
            buffer.rewind()
            //bitmap!!.copyPixelsFromBuffer(buffer)
            bitmap.copyPixelsFromBuffer(buffer)
            // write bitmap to a file
            //fos = FileOutputStream(path)
            //return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                image?.close()
                fos?.close()
                // bitmap?.recycle()
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
        return bitmap
    }

    fun startCapture(reader: ImageReader?): Bitmap? {
        var bitmap: Bitmap? = null
        var image: Image? = null
        try {
            image = reader!!.acquireLatestImage()

            val width = image.width
            val height = image.height

            val planes = image.planes
            val buffer = planes[0].buffer
            val pixelStride = planes[0].pixelStride
            val rowStride = planes[0].rowStride
            val rowPadding = rowStride - pixelStride * width
            var bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(buffer)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            image?.close()
        }

        return bitmap
    }

}