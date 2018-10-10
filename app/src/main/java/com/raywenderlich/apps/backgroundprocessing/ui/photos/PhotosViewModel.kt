package com.raywenderlich.apps.backgroundprocessing.ui.photos

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.raywenderlich.apps.backgroundprocessing.repository.Repository

class PhotosViewModel(private val repository: Repository) : ViewModel() {

    fun getPhotos(): LiveData<List<String>> {
        return repository.getPhotos()
    }

    fun getBanner(): LiveData<String> {
        return repository.getBanner()
    }

    fun start(){
        repository.register()
    }

    fun stop(){
        repository.unregister()
    }
}