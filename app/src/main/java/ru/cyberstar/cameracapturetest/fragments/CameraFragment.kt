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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import ru.cyberstar.cameracapturetest.fragments.helpers.FPS_DEFAULT
import ru.cyberstar.cameracapturetest.fragments.helpers.FPS_KEY
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper.get
import ru.cyberstar.cameracapturetest.tools.InjectorUtils
import ru.cyberstar.cameracapturetest.viewmodels.CameraViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class CameraFragment : CameraBaseFragment() {

    val timerFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    lateinit var timer: TextView

    companion object {
        @JvmStatic
        fun newInstance(): CameraFragment = CameraFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var scheduledFuture: ScheduledFuture<*>? = null

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
        val millisecond = 1000
        val startTime = System.currentTimeMillis()
        val fps: Int? = PreferenceHelper.prefs()[FPS_KEY, FPS_DEFAULT]
        val period = (millisecond / fps!!).toLong()
        if (startWorker) {
            scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                {
                    lockFocus()
                    updateTimerView(System.currentTimeMillis() - startTime)
                }, 0,
                period, TimeUnit.MILLISECONDS
            )
        } else {
            scheduledFuture?.let {
                it.cancel(true)
                scheduledFuture = null
            }

        }
    }


    fun subscribeUI() {
        val factory = InjectorUtils.provideCameraViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory)
            .get(CameraViewModel::class.java)
        binding.setVariable(0, viewModel)
    }

    private fun updateTimerView(duration: Long) {
        activity?.runOnUiThread {
            captureTimer.text = timerFormat.format(duration)
        }
    }
}
