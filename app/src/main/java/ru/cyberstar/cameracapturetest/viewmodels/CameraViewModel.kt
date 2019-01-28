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

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import ru.cyberstar.cameracapturetest.BR
import java.text.SimpleDateFormat
import java.util.*


class CameraViewModel : ViewModel() {

    var layoutFields = CameraLayoutFields()

    class CameraLayoutFields : BaseObservable() {

        private val timerFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

        @get:Bindable
        var timer: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.timer)
            }

        @get:Bindable
        var currentFPS: Int = 0
            set(value) {
                field = value
                notifyPropertyChanged(BR.currentFPS)
            }

        @get:Bindable
        var framesCaptured: Int = 0
            set(value) {
                field = value
                notifyPropertyChanged(BR.framesCaptured)
            }

        @get:Bindable
        var previewBitmap: Bitmap? = null
            set(value) {
                field = value
                notifyPropertyChanged(BR.previewBitmap)
            }


        object BindingAdapters {
            @BindingAdapter("bind:imageBitmap")
            @JvmStatic
            fun loadImage(iv: ImageView?, bitmap: Bitmap?) {
                bitmap?.let { iv?.setImageBitmap(bitmap) }
            }
        }


        fun updateTimeStamp(timeStamp: Long) {
            timer = timerFormat.format(timeStamp)
            this.timeStamp = timeStamp
        }

        private var timeStamp: Long = 0

    }

    fun updatePreviewIMG(bitmap: Bitmap) {
        layoutFields.previewBitmap = bitmap
    }

    fun updateFPS(fps: Int) {
        layoutFields.currentFPS = fps
    }

    fun updateImageCounter(imgCount:Int) {
        layoutFields.framesCaptured = imgCount
    }

    fun resetImageCounter() {
        layoutFields.framesCaptured = 0
    }

    fun updateTimeStamp(timeStamp: Long) {
        layoutFields.updateTimeStamp(timeStamp)
    }


}