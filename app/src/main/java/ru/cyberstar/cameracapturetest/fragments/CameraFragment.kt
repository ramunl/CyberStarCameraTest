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
import kotlinx.android.synthetic.main.fragment_camera.*
import ru.cyberstar.cameracapturetest.fragments.helpers.FPS_DEFAULT
import ru.cyberstar.cameracapturetest.fragments.helpers.FPS_KEY
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper
import ru.cyberstar.cameracapturetest.fragments.helpers.PreferenceHelper.get
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class CameraFragment : CameraBaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): CameraFragment = CameraFragment()
    }

    private var scheduledFuture: ScheduledFuture<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = super.onCreateView(inflater, container, savedInstanceState)
        playButton?.setOnCheckedChangeListener { _, isChecked ->
            onPlayButtonClicked(isChecked)
        }

        return rootView
    }

    @Synchronized
    private  fun onPlayButtonClicked(isPlaying: Boolean) {
        val millisecond = 1000
        val fps: Int? = PreferenceHelper.prefs()[FPS_KEY, FPS_DEFAULT]

        if(isPlaying) {
            scheduledFuture?.let {
                it.cancel(true)
                scheduledFuture = null
            }
        } else {
            scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                { lockFocus() }, 0,
                (millisecond / fps!!).toLong(), TimeUnit.MILLISECONDS
            )
        }

    }
}
