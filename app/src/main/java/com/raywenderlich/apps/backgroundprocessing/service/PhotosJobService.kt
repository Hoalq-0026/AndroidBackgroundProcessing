package com.raywenderlich.apps.backgroundprocessing.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.raywenderlich.apps.backgroundprocessing.app.PhotosUtils

class PhotosJobService : JobService() {

    companion object {
        private const val TAG = "PhotosJobService"
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val runnable = Runnable {
            val needsReschedule: Boolean
            needsReschedule = try {
                val jsonString = PhotosUtils.fetchJsonString()
                (jsonString == null)

            } catch (e: InterruptedException) {
                Log.i(TAG, "Error running job: ")
                true
            }
            Log.i(TAG, "Job finished : ${params?.jobId}, needsReschedule: $needsReschedule")
            jobFinished(params, needsReschedule)
        }
        Thread(runnable).start()

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i(TAG, "Job stopped: +$params?.jobId ")
        return false
    }


}
