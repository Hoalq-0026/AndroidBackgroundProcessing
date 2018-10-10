package com.raywenderlich.apps.backgroundprocessing.ui.photos

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.apps.backgroundprocessing.R
import com.raywenderlich.apps.backgroundprocessing.app.Injection
import com.raywenderlich.apps.backgroundprocessing.app.RWDC2018Application
import com.raywenderlich.apps.backgroundprocessing.service.FetchIntentService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlinx.android.synthetic.main.list_item_photo.*

class PhotosFragment : Fragment() {

    private lateinit var viewModel: PhotosViewModel

    companion object {
        fun newInstance(): PhotosFragment {
            return PhotosFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val viewModelFactory = Injection.provideViewModelFactory(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PhotosViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBanner().observe(this, Observer { banner ->
            Picasso.get().load(banner).fit().into(bannerImageView)
        })

        viewModel.getPhotos().observe(this, Observer { photos ->
            val adapter = PhotosAdapter(photos ?: emptyList())
            photosRecyclerView.layoutManager = GridLayoutManager(context, 2)
            photosRecyclerView.adapter = adapter
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.start()
        FetchIntentService.startActionFetch(RWDC2018Application.getAppContext())
    }

    override fun onStop() {
        super.onStop()
        viewModel.stop()
    }
}