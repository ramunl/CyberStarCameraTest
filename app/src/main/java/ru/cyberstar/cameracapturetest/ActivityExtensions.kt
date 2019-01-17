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

package ru.cyberstar.cameracapturetest

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Shows a [Toast] on the UI thread.
 * @param text The message to show
 */
fun FragmentActivity.showToast(text: String) {
    runOnUiThread { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }
}


enum class FragmentTag {
    CAMERA_FRAGMENT_TAG,
    SETTINGS_FRAGMENT_TAG,
}


private fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.replaceFragment(containerId: Int, fragment: Fragment, frameTag: FragmentTag) {
    val isAdded = supportFragmentManager.findFragmentByTag(frameTag.name)?.isAdded ?: false
    if (!isAdded) {
        supportFragmentManager.inTransaction { replace(containerId, fragment, frameTag.name) }
    }
}