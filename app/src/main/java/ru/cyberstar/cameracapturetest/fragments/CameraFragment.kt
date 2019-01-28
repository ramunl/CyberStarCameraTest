/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.cyberstar.cameracapturetest.fragments

import android.graphics.Bitmap
import android.media.ImageReader
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_camera.view.*
import ru.cyberstar.cameracapturetest.BR
import ru.cyberstar.cameracapturetest.tools.InjectorUtils
import ru.cyberstar.cameracapturetest.viewmodels.CameraViewModel
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import org.jetbrains.anko.doAsync
import ru.cyberstar.cameracapturetest.presenters.CameraPresenter
import ru.cyberstar.cameracapturetest.tools.deepCopy
import ru.cyberstar.cameracapturetest.tools.imageToByteArray
import ru.cyberstar.cameracapturetest.views.CameraView
import java.nio.ByteBuffer


class CameraFragment : CameraView, Camera2VideoFragment() {

    @InjectPresenter(type = PresenterType.GLOBAL)
    internal lateinit var cameraPresenter: CameraPresenter

    private lateinit var cameraViewModel: CameraViewModel

    override fun updateTimeStamp(timeStamp: Long) {
        cameraViewModel.updateTimeStamp(timeStamp)
    }

    override fun updatePreviewIMG(bmp: Bitmap) {
        cameraViewModel.updatePreviewIMG(bmp)
    }

    override fun updateImageCounter(imageCount: Int) {
        cameraViewModel.updateImageCounter(imageCount)
    }

    override fun updateFPS(fps: Int) {
        cameraViewModel.updateFPS(fps)
    }

    override fun showTimer() {
        binding.root.captureTimer.visibility = VISIBLE
    }

    override fun hideTimer() {
        binding.root.captureTimer.visibility = GONE
    }


    companion object {
        @JvmStatic
        fun newInstance(): CameraFragment = CameraFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        rootView!!.playButton!!.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
                cameraPresenter.startTimer()
            else
                cameraPresenter.stopTimer()
        }
        subscribeUI()
        return rootView
    }

    private fun subscribeUI() {
        val factory = InjectorUtils.provideCameraViewModelFactory()
        cameraViewModel = ViewModelProviders.of(this, factory).get(CameraViewModel::class.java)
        binding.setVariable(BR.viewModel, cameraViewModel)

    }


    override fun onImageAvailable(reader: ImageReader?) {
        var frameSize = cameraFrameSize()
        if (frameSize != null && reader != null) {
            onImageAvailable(reader, frameSize)
        }
        cameraPresenter.incImageCounter()
    }


    private var frameByteBufferTemp: List<ByteBuffer>? = null

    private fun onImageAvailable(reader: ImageReader, frameSize: Size) {
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
                        bmp?.let { cameraViewModel.updatePreviewIMG(bmp) }
                    }
                }
            }
        }
        image?.close()
    }

}
