package com.raywenderlich.apps.backgroundprocessing.repository

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.*
import android.os.AsyncTask
import android.support.v4.content.LocalBroadcastManager
import androidx.work.*
import com.raywenderlich.apps.backgroundprocessing.app.PhotosUtils
import com.raywenderlich.apps.backgroundprocessing.app.RWDC2018Application
import com.raywenderlich.apps.backgroundprocessing.service.DownloadWorker
import com.raywenderlich.apps.backgroundprocessing.service.FetchIntentService
import com.raywenderlich.apps.backgroundprocessing.service.LogJobService
import com.raywenderlich.apps.backgroundprocessing.service.PhotosJobService
import java.util.concurrent.TimeUnit

class PhotosRepository : Repository {

    companion object {
        const val DOWNLOAD_WORK_TAG = "DOWNLOAD_WORK_TAG"
    }

    private val photosLiveData = MutableLiveData<List<String>>()
    private val bannerLiveData = MutableLiveData<String>()

    init {
        schedulePeriodicWorkRequest()
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            FetchPhotosAsyncTask({ photos ->
                photosLiveData.value = photos

            }).execute()

            FetchBannerAsyncTask({ banner ->
                bannerLiveData.value = banner
            }).execute()
        }

    }

    override fun register() {
        LocalBroadcastManager.getInstance(RWDC2018Application.getAppContext())
                .registerReceiver(receiver, IntentFilter(FetchIntentService.FETCH_COMPLETE))
    }

    override fun unregister() {
        LocalBroadcastManager.getInstance(RWDC2018Application.getAppContext()).unregisterReceiver(receiver)
    }

    override fun getPhotos(): LiveData<List<String>> {

        FetchPhotosAsyncTask({ photos ->
            photosLiveData.value = photos
        }).execute()

        return photosLiveData
    }

    override fun getBanner(): LiveData<String> {
        FetchBannerAsyncTask({ banner ->
            bannerLiveData.value = banner
        }).execute()

        return bannerLiveData
    }

    private fun schedulePeriodicWorkRequest() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .build()

        val workManager = WorkManager.getInstance()

        val request: WorkRequest = PeriodicWorkRequestBuilder<DownloadWorker>(15,
                TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(DOWNLOAD_WORK_TAG)
                .build()

        workManager.cancelAllWorkByTag(DOWNLOAD_WORK_TAG)
        workManager.enqueue(request)
    }

    private fun scheduleFetchJob() {
        val jobScheduler = RWDC2018Application.getAppContext().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(1000,
                ComponentName(RWDC2018Application.getAppContext(), PhotosJobService::class.java))
                .setPeriodic(900000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
        jobScheduler.schedule(jobInfo)
    }

    private fun scheduleLogJob() {
        val jobScheduler = RWDC2018Application.getAppContext().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(1001,
                ComponentName(RWDC2018Application.getAppContext(), LogJobService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
        jobScheduler.schedule(jobInfo)

    }


//    private fun fetchBanner() {
//        val runnable = Runnable {
//            val photosString = PhotosUtils.photoJsonString()
//            val banner = PhotosUtils.bannerFromJsonString(photosString ?: "")
//            banner?.let {
//                bannerLiveData.postValue(it)
//            }
//        }
//
//        val thread = Thread(runnable)
//        thread.start()
//    }

    /*private fun fetchPhotos() {

        val runnable = Runnable {
            val photosString = PhotosUtils.photoJsonString()
            val photos = PhotosUtils.photoUrlsFromJsonString(photosString ?: "")
            if (photos != null) {
                photosLiveData.postValue(photos)
            }
        }

        val thread = Thread(runnable)
        thread.start()
    }

    */

    private class FetchBannerAsyncTask(val callback: (String) -> Unit) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            val photosString = PhotosUtils.photoJsonString()
            return PhotosUtils.bannerFromJsonString(photosString ?: "")
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                callback(result)
            }
        }

    }

    private class FetchPhotosAsyncTask(val callback: (List<String>) -> Unit) : AsyncTask<Void, Void, List<String>>() {

        override fun doInBackground(vararg params: Void?): List<String>? {

            val photosString = PhotosUtils.photoJsonString()
            return PhotosUtils.photoUrlsFromJsonString(photosString ?: "")
        }

        override fun onPostExecute(result: List<String>?) {
            if (result != null) {
                callback(result)
            }
        }

    }
}