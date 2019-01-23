package ru.cyberstar.cameracapturetest.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageReader
import ru.cyberstar.cameracapturetest.tools.ImageUtil.imageToByteArray
import java.io.FileOutputStream
import java.io.IOException


class ScreenCapture constructor(val width: Int, val height: Int) {

    val mImageReader: ImageReader = ImageReader.newInstance(width, height, ImageFormat.YUV_420_888, 1)


   /* @Synchronized
    fun startCaptureYUY2(reader: ImageReader?): Bitmap? {
        var bitmap: Bitmap? = null
        var image: Image? = null
        try {
            image = reader!!.acquireNextImage()

            val jpgBytes = imageToByteArray(image.planes)
            val boundOption = BitmapFactory.Options()
            boundOption.outHeight = height
            boundOption.outWidth = width

            bitmap = BitmapFactory.decodeByteArray(jpgBytes, 0, jpgBytes!!.size, boundOption)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            image?.close()
        }

        return bitmap
    }*/

    fun startCaptureJPEG(context: Context, reader: ImageReader?): Bitmap? {
        var bitmap: Bitmap? = null
        var image: Image? = null
        try {
            image = reader!!.acquireNextImage()


            val planes = image.planes
            val buffer = planes[0].buffer
            var jpgBytes = ByteArray(buffer.remaining())
            buffer.get(jpgBytes)


            val boundOption = BitmapFactory.Options()
            boundOption.outHeight = height
            boundOption.outWidth = width

            bitmap = BitmapFactory.decodeByteArray(jpgBytes, 0, jpgBytes.size, boundOption)

            //bitmap = BitmapFactory.decodeByteArray(jpgBytes, 0, jpgBytes.size, boundOption)*

           /* val intBuf = ByteBuffer.wrap(rgbBytes)
                .order(ByteOrder.BIG_ENDIAN)
                .asIntBuffer()
            intBuf.rewind()
            val array = IntArray(intBuf.remaining())
            intBuf.get(array)*/

//             bitmap = Bitmap.createBitmap(rgbBytes, width , height, Bitmap.Config.ARGB_8888)
           // var rgbBuffer = ByteBuffer.wrap(rgbBytes)
           // bitmap.copyPixelsFromBuffer(buffer)


           /* val uvBytes = ImageProcessor.yuvToN21(image)

            val yuvImage = YuvImage(uvBytes, ImageFormat.YUY2, width, height, null)

            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
            out.close()

            val jpgBytes = out.toByteArray()
            //  bufferSizeLatest = bufferSize

            val boundOption = BitmapFactory.Options()
            boundOption.outHeight = height
            boundOption.outWidth = width

            bitmap = BitmapFactory.decodeByteArray(jpgBytes, 0, jpgBytes.size, boundOption)*/
            /*if (bitmapLatest != null) {
                bitmapLatest?.recycle()
                bitmapLatest = bitmap
            } else {
                bitmapLatest = bitmap
            }*/


        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            image?.close()
        }

        return bitmap
    }

}