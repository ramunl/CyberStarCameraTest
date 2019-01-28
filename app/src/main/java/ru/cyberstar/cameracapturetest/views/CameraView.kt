package ru.cyberstar.cameracapturetest.views

import android.graphics.Bitmap
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType


@StateStrategyType(AddToEndSingleStrategy::class)
interface CameraView : MvpView {
    fun showTimer()

    fun hideTimer()

    fun updatePreviewIMG(bmp: Bitmap)

    fun updateTimeStamp(timeStamp: Long)

    fun updateImageCounter(imageCount:Int)

    fun updateFPS(fps:Int)

}