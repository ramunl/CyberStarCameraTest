/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.cyberstar.cameracapturetest.viewmodels

import android.media.ImageReader
import android.util.Size
import androidx.lifecycle.ViewModel
import org.jetbrains.anko.doAsync
import ru.cyberstar.cameracapturetest.model.ImageCaptureListener
import ru.cyberstar.cameracapturetest.model.ImageCaptureTimer
import ru.cyberstar.cameracapturetest.tools.deepCopy
import ru.cyberstar.cameracapturetest.tools.imageToByteArray
import java.nio.ByteBuffer


class CameraViewModel : ViewModel() {

    var framesCapturedPrev: Int = 0
    var layoutFields = CameraLayoutFields()
    private var frameByteBufferTemp: List<ByteBuffer>? = null

    init {
        startCapture()
    }

    fun stopCapture() {
        layoutFields.framesCaptured = 0
        layoutFields.updateTimeStamp(0)
        ImageCaptureTimer.stop()
    }

    fun startCapture() {
        ImageCaptureTimer.start(object : ImageCaptureListener {
            override fun onTimeChanged(timeStamp: Long) {
                val fps = layoutFields.framesCaptured - framesCapturedPrev
                layoutFields.currentFPS = fps
                framesCapturedPrev = layoutFields.framesCaptured
                layoutFields.updateTimeStamp(timeStamp)
            }
        })
    }

    fun onImageAvailable(reader: ImageReader, frameSize: Size) {
        layoutFields.framesCaptured++
        val image = reader.acquireNextImage()
        if (frameByteBufferTemp == null) {
            image?.let {
                val planes = image.planes
                val yBuffer = deepCopy(planes[0].buffer)
                val uBuffer = deepCopy(planes[1].buffer)
                val vBuffer = deepCopy(planes[2].buffer)
                frameByteBufferTemp = mutableListOf(yBuffer, uBuffer, vBuffer)
                frameByteBufferTemp?.let { copy ->
                    doAsync {
                        val bmp = imageToByteArray(
                            copy,
                            frameSize.width,
                            frameSize.height
                        )
                        frameByteBufferTemp = null
                        bmp?.let { layoutFields.previewBitmap = bmp }
                    }
                }
            }
        }
        image?.close()
    }

}