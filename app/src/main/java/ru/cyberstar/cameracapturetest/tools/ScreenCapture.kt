package ru.cyberstar.cameracapturetest.tools

import android.content.Context
import android.graphics.*
import android.media.Image
import android.media.ImageReader
import android.renderscript.RenderScript
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ByteOrder.BIG_ENDIAN
import kotlin.experimental.and


class ScreenCapture constructor(val width: Int, val height: Int) {

    val mImageReader: ImageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)

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

    var bitmapLatest: Bitmap? = null
    var bufferSizeLatest: Int = 0

    fun startCapture(context: Context, reader: ImageReader?): Bitmap? {
        var bitmap: Bitmap? = null
        var image: Image? = null
        try {
            image = reader!!.acquireNextImage()


            val planes = image.planes
            val buffer = planes[0].buffer
            var jpgBytes = ByteArray(buffer.remaining())
            buffer.get(jpgBytes)
            //val rs = RenderScript.create(context)

            //val rgbBytes = getRGBIntFromPlanes(planes,height)//ImageProcessor.yuvToRgb(image, rs)


            val boundOption = BitmapFactory.Options()
            boundOption.outHeight = height
            boundOption.outWidth = width

           // bitmap = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888)
           // bitmap.copyPixelsFromBuffer(buffer)

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

    private fun getRGBIntFromPlanes(planes: Array<Image.Plane>, height:Int) : IntArray{
        val mRgbBuffer = IntArray(10000000)
        val yPlane = planes[0].buffer
        val uPlane = planes[1].buffer
        val vPlane = planes[2].buffer

        var bufferIndex = 0
        val total = yPlane.capacity()
        val uvCapacity = uPlane.capacity()
        val width = planes[0].rowStride

        var yPos = 0
        for (i in 0 until height) {
            var uvPos = (i shr 1) * width

            for (j in 0 until width) {
                if (uvPos >= uvCapacity - 1)
                    break
                if (yPos >= total)
                    break

                val y1 = (yPlane.get(yPos++) and 0xff.toByte()) - (Integer.valueOf(16).toByte())

                /*
              The ordering of the u (Cb) and v (Cr) bytes inside the planes is a
              bit strange. The _first_ byte of the u-plane and the _second_ byte
              of the v-plane build the u/v pair and belong to the first two pixels
              (y-bytes), thus usual YUV 420 behavior. What the Android devs did
              here (IMHO): just copy the interleaved NV21 U/V data to two planes
              but keep the offset of the interleaving.
             */
                val u = (uPlane.get(uvPos) and 0xff.toByte()) - 128
                val v = (vPlane.get(uvPos + 1) and 0xff.toByte()) - 128
                if (j and 1 == 1) {
                    uvPos += 2
                }

                // This is the integer variant to convert YCbCr to RGB, NTSC values.
                // formulae found at
                // https://software.intel.com/en-us/android/articles/trusted-tools-in-the-new-android-world-optimization-techniques-from-intel-sse-intrinsics-to
                // and on StackOverflow etc.
                val y1192 = 1192 * y1
                var r = y1192 + 1634 * v
                var g = y1192 - 833 * v - 400 * u
                var b = y1192 + 2066 * u

                r = if (r < 0) 0 else if (r > 262143) 262143 else r
                g = if (g < 0) 0 else if (g > 262143) 262143 else g
                b = if (b < 0) 0 else if (b > 262143) 262143 else b


                mRgbBuffer[bufferIndex++] = r shl 6 and 0xff0000 or
                        (g shr 2 and 0xff00) or
                        (b shr 10 and 0xff)
            }
        }
        return mRgbBuffer
    }
}