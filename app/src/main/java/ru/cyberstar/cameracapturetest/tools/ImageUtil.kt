package ru.cyberstar.cameracapturetest.tools

import android.graphics.*
import android.media.Image

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

object ImageUtil {

    fun planesToByteArray(planes: Array<out Image.Plane>, width: Int, height: Int): Bitmap? {
        var data: ByteArray? = null

        data = NV21toJPEG(
            YUV_420_888toNV21(planes),
            width, height
        )

        val boundOption = BitmapFactory.Options()
        boundOption.outHeight = height
        boundOption.outWidth = width

        return BitmapFactory.decodeByteArray(data, 0, data.size, boundOption)
    }

    private fun YUV_420_888toNV21(planes: Array<out Image.Plane>): ByteArray {
        val nv21: ByteArray
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        nv21 = ByteArray(ySize + uSize + vSize)

        //U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        return nv21
    }

    fun imageToByteArray(image: Image, width: Int, height: Int): Bitmap? {
        var data: ByteArray? = null

        data = NV21toJPEG(
            YUV_420_888toNV21(image),
            image.getWidth(), image.getHeight()
        )

        val boundOption = BitmapFactory.Options()
        boundOption.outHeight = height
        boundOption.outWidth = width

        return BitmapFactory.decodeByteArray(data, 0, data.size, boundOption)
    }

    private fun YUV_420_888toNV21(image: Image): ByteArray {
        val nv21: ByteArray
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        nv21 = ByteArray(ySize + uSize + vSize)

        //U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        return nv21
    }

    private fun NV21toJPEG(nv21: ByteArray, width: Int, height: Int): ByteArray {
        val out = ByteArrayOutputStream()
        val yuv = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        yuv.compressToJpeg(Rect(0, 0, width, height), 100, out)
        return out.toByteArray()
    }

    fun imageToByteArray(buffers: List<ByteBuffer>, width: Int, height: Int): Bitmap? {
        var data: ByteArray? = null
        data = NV21toJPEG(
            YUV_420_888toNV21(buffers),
            width, height
        )
        val boundOption = BitmapFactory.Options()
        boundOption.outHeight = height
        boundOption.outWidth = width

        return BitmapFactory.decodeByteArray(data, 0, data.size, boundOption)
    }

    /* public fun YUV_420_888toNV21(planes: MutableList<ByteBuffer>): ByteArray {
         val nv21: ByteArray
         val yBuffer = planes[0]
         val uBuffer = planes[1]
         val vBuffer = planes[2]

         val ySize = yBuffer.remaining()
         val uSize = uBuffer.remaining()
         val vSize = vBuffer.remaining()

         nv21 = ByteArray(ySize + uSize + vSize)

         //U and V are swapped
         yBuffer.get(nv21, 0, ySize)
         vBuffer.get(nv21, ySize, vSize)
         uBuffer.get(nv21, ySize + vSize, uSize)

         return nv21
     }*/


    /*fun imageToByteArray(planes: List<ByteBuffer>, width: Int, height: Int): ByteArray? {
        var data = NV21toJPEG(
                YUV_420_888toNV21(planes),
            width, height
            )
        return data
    }*/

    private fun YUV_420_888toNV21(planes: List<ByteBuffer>): ByteArray {
        val nv21: ByteArray

        val yBuffer = planes[0]
        val uBuffer = planes[1]
        val vBuffer = planes[2]

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        nv21 = ByteArray(ySize + uSize + vSize)

        //U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        return nv21
    }

}