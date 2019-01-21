package ru.cyberstar.cameracapturetest.tools

import ru.cyberstar.cameracapturetest.fragments.helpers.FPS_DEFAULT
import ru.cyberstar.cameracapturetest.fragments.helpers.FPS_KEY
import ru.cyberstar.cameracapturetest.fragments.helpers.MILLISECONDS_IN_SEC
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper.get
import ru.cyberstar.cameracapturetest.viewmodels.CameraViewModel
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object ImageCaptureWorker {

    private var scheduledFuture: ScheduledFuture<*>? = null
    private var startTime: Long = 0

    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var cameraProvider: CameraProvider

    fun injectModel(provider: CameraProvider, vewModel: CameraViewModel) {
        cameraProvider = provider
        cameraViewModel = vewModel
    }

    fun stop() {
        scheduledFuture?.let {
            it.cancel(true)
            scheduledFuture = null
        }
    }

    fun start() {
        cameraViewModel.resetImageCounter()
        cameraViewModel.updateTimeStamp(0)
        val fps: Int? = PreferenceHelper.prefs()[FPS_KEY, FPS_DEFAULT]

        val period = (MILLISECONDS_IN_SEC / fps!!).toLong()
        startTime = System.currentTimeMillis()
        scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            {
                cameraProvider.imageCapture()
                cameraViewModel.updateTimeStamp(System.currentTimeMillis() - startTime)
            }, 0,
            period, TimeUnit.MILLISECONDS
        )
    }

}