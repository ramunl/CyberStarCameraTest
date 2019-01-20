package ru.cyberstar.cameracapturetest;

import android.app.Application
import android.content.Context

class CyberApp : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: CyberApp? = null

        fun appContext(): Context {
            return instance!!.applicationContext
        }

    }
}
