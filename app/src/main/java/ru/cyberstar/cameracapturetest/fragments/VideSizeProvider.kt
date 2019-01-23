package ru.cyberstar.cameracapturetest.fragments

import android.util.Size

/**
 * Provides camera chosen frame size
 */
interface FrameVideoSizeProvider {
    fun cameraFrameSize(): Size?
}