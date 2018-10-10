package com.raywenderlich.apps.backgroundprocessing.app

import android.content.Context
import com.raywenderlich.apps.backgroundprocessing.repository.PhotosRepository
import com.raywenderlich.apps.backgroundprocessing.repository.Repository
import com.raywenderlich.apps.backgroundprocessing.ui.photos.PhotosViewModelFactory

object Injection {

    fun provideRepository(context: Context?): Repository {
        return PhotosRepository()
    }

    fun provideViewModelFactory(context: Context?): PhotosViewModelFactory {
        val repository = provideRepository(context)
        return PhotosViewModelFactory(repository)
    }
}