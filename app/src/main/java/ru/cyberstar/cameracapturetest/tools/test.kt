package ru.cyberstar.cameracapturetest.tools

import android.media.ImageReader
import java.nio.ByteBuffer
import androidx.work.Data.toByteArray
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.media.Image.Plane





class test {
    internal fun asd(mImageReader: ImageReader) {

    }

    /*object ImageUtil {

        fun imageToByteArray(image: Image): ByteArray? {
            var data: ByteArray? = null
            if (image.getFormat() === ImageFormat.JPEG) {
                val planes = image.getPlanes()
                val buffer = planes[0].getBuffer()
                data = ByteArray(buffer.capacity())
                buffer.get(data)
                return data
            } else if (image.getFormat() === ImageFormat.YUV_420_888) {
                data = NV21toJPEG(
                    YUV_420_888toNV21(image),
                    image.getWidth(), image.getHeight()
                )
            }
            return data
        }

        private fun YUV_420_888toNV21(image: Image): ByteArray {
            val nv21: ByteArray
            val yBuffer = image.getPlanes()[0].getBuffer()
            val uBuffer = image.getPlanes()[1].getBuffer()
            val vBuffer = image.getPlanes()[2].getBuffer()

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
    }-*/
}
