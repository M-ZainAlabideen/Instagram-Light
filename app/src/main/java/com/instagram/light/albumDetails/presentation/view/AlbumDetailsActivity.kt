package com.instagram.light.albumDetails.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.instagram.light.albumDetails.data.model.Photo
import com.instagram.light.albumDetails.presentation.adapter.PhotosAdapter
import com.instagram.light.albumDetails.presentation.viewModel.AlbumDetailsViewModel
import com.instagram.light.databinding.ActivityAlbumDetailsBinding
import com.instagram.light.utils.Constants
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumDetailsBinding
    private val albumDetailsViewModel: AlbumDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        receiveBundle()
        initRecyclerView()
        observePhotosList()
        observeLoadingProgress()
        observeFailure()
        onSearchCLick()
    }

    private fun receiveBundle() {
        val extras = intent.extras
        if (extras != null) {
            val albumId = extras.getInt(Constants.ALBUM_ID)
            albumDetailsViewModel.getPhotos(albumId)
        }
    }

    private fun initRecyclerView() {
        binding.photosRv.apply {
            layoutManager = GridLayoutManager(this@AlbumDetailsActivity, Constants.SPAN_COUNT)
            adapter = PhotosAdapter(ArrayList(), ::onItemClick)
        }
    }

    private fun observePhotosList() {
        lifecycleScope.launch {
            albumDetailsViewModel.photosList.collect { list ->
                if (list != null) {
                    getPhotosAdapter()?.updateData(list)
                }
            }

        }
    }

    private fun observeLoadingProgress() {
        lifecycleScope.launch {
            albumDetailsViewModel.loading.collect { loading ->
                if (loading) {
                    binding.photosRv.showShimmer()
                } else {
                    binding.photosRv.hideShimmer()
                }
            }
        }
    }

    private fun observeFailure() {
        lifecycleScope.launch {
            albumDetailsViewModel.errorMessage.collect { message ->
                if (!message.isNullOrEmpty()) Toast.makeText(
                    this@AlbumDetailsActivity,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun getPhotosAdapter() =
        binding.photosRv.adapter as? PhotosAdapter


    private fun onItemClick(photosList: List<Photo>, position: Int) {
        photoViewer(photosList, position)
    }

    private fun photoViewer(photosList: List<Photo>, position: Int) {
        StfalconImageViewer.Builder(this@AlbumDetailsActivity, photosList)
        { view, photo ->
            Glide.with(this@AlbumDetailsActivity)
                .asBitmap()
                .load(photo.thumbnailUrl)
                .into(view)
        }.withStartPosition(position).show()
    }


    private fun onSearchCLick() {
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
                albumDetailsViewModel.search(s.toString())
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                albumDetailsViewModel.search(s.toString())
            }
        })
    }

}