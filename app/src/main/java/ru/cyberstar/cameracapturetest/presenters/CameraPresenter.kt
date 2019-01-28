package ru.cyberstar.cameracapturetest.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.cyberstar.cameracapturetest.fragments.helpers.MILLISECONDS_IN_SEC
import ru.cyberstar.cameracapturetest.views.CameraView
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

@InjectViewState
class CameraPresenter: MvpPresenter<CameraView>() {

    private var scheduledFuture: ScheduledFuture<*>? = null
    private val TIMER_PERIOD = 100L
    private var prevTimeStamp: Long = 0
    private var framesCapturedAll: Int = 0
    private var framesCapturedPerSecond: Int = 0
    private var startTime: Long = 0

    fun startTimer() {
        prevTimeStamp = 0
        framesCapturedAll = 0
        startTime = System.currentTimeMillis()
        viewState.showTimer()
        scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            {
                val currentTime = System.currentTimeMillis()
                val timePassed = currentTime - prevTimeStamp
                framesCapturedPerSecond++
                if (timePassed >= MILLISECONDS_IN_SEC) {
                    viewState.updateFPS(framesCapturedPerSecond)
                    framesCapturedPerSecond = 0
                    prevTimeStamp = currentTime
                }
                viewState.updateTimeStamp(currentTime - startTime)

            }, 0,
            TIMER_PERIOD, TimeUnit.MILLISECONDS
        )
    }

    fun incImageCounter() {
        viewState.updateImageCounter(++framesCapturedAll)
    }

    fun stopTimer() {
        startTime = System.currentTimeMillis()
        viewState.hideTimer()
        scheduledFuture?.let {
            it.cancel(true)
            scheduledFuture = null
        }
    }
}