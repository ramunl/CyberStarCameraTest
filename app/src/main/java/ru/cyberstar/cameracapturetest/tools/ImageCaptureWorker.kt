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
    private var framesCaptured:Int = 0

    private lateinit var cameraViewModel: CameraViewModel

    fun injectModel(vewModel: CameraViewModel) {
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
        startTime = System.currentTimeMillis()
        framesCaptured = 0
        scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            {
                cameraViewModel.setCurrentFPS(cameraViewModel.layoutFields.framesCaptured - framesCaptured)
                cameraViewModel.updateTimeStamp(System.currentTimeMillis() - startTime)
                framesCaptured = cameraViewModel.layoutFields.framesCaptured
            }, 0,
            MILLISECONDS_IN_SEC, TimeUnit.MILLISECONDS
        )
    }

}