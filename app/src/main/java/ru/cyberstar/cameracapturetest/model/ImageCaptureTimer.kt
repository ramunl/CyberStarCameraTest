package ru.cyberstar.cameracapturetest.model

import ru.cyberstar.cameracapturetest.fragments.helpers.MILLISECONDS_IN_SEC
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object ImageCaptureTimer {

    private var scheduledFuture: ScheduledFuture<*>? = null
    private var startTime: Long = 0L
    private var framesCapturedAll: Int = 0

    fun stop() {
        scheduledFuture?.let {
            it.cancel(true)
            scheduledFuture = null
        }
    }

    fun start(listener: ImageCaptureListener) {
        if(startTime == 0L) {
            startTime = System.currentTimeMillis()
            framesCapturedAll = 0
            scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                {
                    val timeStamp = System.currentTimeMillis()
                    listener.onTimeChanged(timeStamp - startTime)
                }, 0,
                MILLISECONDS_IN_SEC, TimeUnit.MILLISECONDS
            )
        }
    }




}