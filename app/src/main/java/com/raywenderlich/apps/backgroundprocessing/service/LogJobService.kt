package com.raywenderlich.apps.backgroundprocessing.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class LogJobService : JobService() {
    companion object {
        private const val TAG = "LogJobService"
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i(TAG, "Stopping job : ${params?.jobId}")
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val runnable = Runnable {
            Thread.sleep(5000)
            jobFinished(params, false)
            Log.i(TAG, "Job finished : ${params?.jobId}")
        }

        Log.i(TAG, "Starting job: " + params?.jobId)
        Thread(runnable).start()
        return true
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        super.onDestroy()
    }
}
