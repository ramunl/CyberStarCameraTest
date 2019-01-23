package ru.cyberstar.cameracapturetest.tools

import android.media.Image
import android.media.ImageReader
import kotlinx.android.synthetic.main.fragment_camera.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ru.cyberstar.cameracapturetest.fragments.helpers.MILLISECONDS_IN_SEC
import ru.cyberstar.cameracapturetest.viewmodels.CameraViewModel
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object ImageCaptureWorker {

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


                    /*if (framesQueue.size > 0) {
                        var planes = framesQueue.removeLast()
                        var bitmap = imageToByteArray(planes, width, height)
                        for (plane in planes) {
                            plane.reset()
                        }
                        bitmap?.let {
                            cameraViewModel!!.updatePreviewIMG(bitmap)
                        }
                    }*/
                }


            }, 0,
            TIMER_PERIOD, TimeUnit.MILLISECONDS
        )
    }

    private val framesQueue: Deque<List<ByteBuffer>> = LinkedList()

    var width: Int = 0
    var height: Int = 0

    fun enqueueFrame(planes: List<ByteBuffer>, width: Int, height: Int) {
       // framesQueue.add(planes)
        this.width = width
        this.height = height
    }


    var planesCopy: List<ByteBuffer>? = null

    fun onImageAvailable(reader: ImageReader?, width: Int, height: Int) {
        var image: Image? = reader!!.acquireNextImage()

        if (planesCopy == null) {
            image?.let {
                val planes = image.planes
                val yBuffer = deepCopy(planes[0].buffer)
                val uBuffer = deepCopy(planes[1].buffer)
                val vBuffer = deepCopy(planes[2].buffer)
                planesCopy = mutableListOf(yBuffer, uBuffer, vBuffer)
                planesCopy?.let { copy ->
                    cameraViewModel?.incImageCounter()
                    doAsync {
                        val bmp = imageToByteArray(copy, videoSize.width, videoSize.height)
                        planesCopy = null
                        bmp?.let { cameraViewModel?.updatePreviewIMG(bmp) }
                    }
                }
            }
        }
        image?.close()
    }


}