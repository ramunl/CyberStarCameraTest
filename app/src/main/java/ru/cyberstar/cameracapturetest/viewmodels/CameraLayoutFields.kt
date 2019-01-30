package ru.cyberstar.cameracapturetest.viewmodels

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import ru.cyberstar.cameracapturetest.BR
import java.text.SimpleDateFormat
import java.util.*

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