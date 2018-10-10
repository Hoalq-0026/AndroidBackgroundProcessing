package com.raywenderlich.apps.backgroundprocessing.app

import android.app.Application
import android.content.Context

class RWDC2018Application : Application() {

    companion object {

        private lateinit var instance: RWDC2018Application

        var isPlayingSong = false

        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        instance = this
        super.onCreate()

    }
}