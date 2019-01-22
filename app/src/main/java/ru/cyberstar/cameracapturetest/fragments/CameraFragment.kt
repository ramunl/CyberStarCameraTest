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

import android.media.ImageReader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_camera.view.*
import ru.cyberstar.cameracapturetest.BR
import ru.cyberstar.cameracapturetest.tools.CameraProvider
import ru.cyberstar.cameracapturetest.tools.ImageCaptureWorker
import ru.cyberstar.cameracapturetest.tools.InjectorUtils
import ru.cyberstar.cameracapturetest.viewmodels.CameraViewModel

class CameraFragment : Camera2VideoFragment() {


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
            onPlayButtonClicked(isChecked)
        }
        subscribeUI()
        return rootView
    }

    @Synchronized
    private fun onPlayButtonClicked(startWorker: Boolean) {
        if (startWorker) ImageCaptureWorker.start()
        else ImageCaptureWorker.stop()
    }

    lateinit var viewModel: CameraViewModel

    private fun subscribeUI() {
        val factory = InjectorUtils.provideCameraViewModelFactory()
        viewModel = ViewModelProviders.of(this, factory).get(CameraViewModel::class.java)
        ImageCaptureWorker.injectModel(viewModel)
        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onFrameCaptured() {
        viewModel.incImageCounter()
    }
    override fun onPause() {
        super.onPause()
        ImageCaptureWorker.stop()
    }

    override fun onResume() {
        super.onResume()
        ImageCaptureWorker.start()
    }

}
