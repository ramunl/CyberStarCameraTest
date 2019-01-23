package ru.cyberstar.cameracapturetest.tools

import android.media.ImageReader
import android.util.Size
import org.jetbrains.anko.doAsync
import ru.cyberstar.cameracapturetest.fragments.helpers.MILLISECONDS_IN_SEC
import ru.cyberstar.cameracapturetest.viewmodels.CameraViewModel
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object ImageCaptureWorker {

    private var frameByteBufferTemp: List<ByteBuffer>? = null
    private var scheduledFuture: ScheduledFuture<*>? = null
    private var startTime: Long = 0
    private var prevTimeStamp: Long = 0
    private var framesCaptured: Int = 0
    private const val TIMER_PERIOD = 100L
    private var cameraViewModel: CameraViewModel? = null

    fun injectModel(vewModel: CameraViewModel) {
        cameraViewModel = vewModel
        start()
    }

    fun stop() {
        scheduledFuture?.let {
            it.cancel(true)
            scheduledFuture = null
        }
    }

    fun start() {
        cameraViewModel?.resetImageCounter()
        cameraViewModel?.updateTimeStamp(0)
        startTime = System.currentTimeMillis()
        framesCaptured = 0
        scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            {
                if (cameraViewModel != null) {
                    val timeStamp = System.currentTimeMillis()
                    val timePassed = timeStamp - prevTimeStamp
                    if (timePassed >= MILLISECONDS_IN_SEC) {
                        val fps = cameraViewModel!!.layoutFields.framesCaptured - framesCaptured
                        cameraViewModel!!.setCurrentFPS(fps)
                        framesCaptured = cameraViewModel!!.layoutFields.framesCaptured
                        prevTimeStamp = timeStamp
                    }

                    cameraViewModel!!.updateTimeStamp(timeStamp - startTime)

                }


            }, 0,
            TIMER_PERIOD, TimeUnit.MILLISECONDS
        )
    }

    fun onImageAvailable(reader: ImageReader, frameSize: Size) {
        cameraViewModel?.incImageCounter()
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
                        val bmp = imageToByteArray(copy, frameSize.width, frameSize.height)
                        frameByteBufferTemp = null
                        bmp?.let { cameraViewModel?.updatePreviewIMG(bmp) }
                    }
                }
            }
        }
        image?.close()
    }


}