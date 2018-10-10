package com.raywenderlich.apps.backgroundprocessing.ui.photos

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.raywenderlich.apps.backgroundprocessing.repository.Repository
import java.lang.IllegalArgumentException

class PhotosViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotosViewModel::class.java)) {
            return PhotosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}