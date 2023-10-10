package com.instagram.light.profile.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.instagram.light.albumDetails.presentation.view.AlbumDetailsActivity
import com.instagram.light.databinding.ActivityProfileBinding
import com.instagram.light.profile.data.model.Album
import com.instagram.light.profile.data.model.User
import com.instagram.light.profile.presentation.adapter.AlbumsAdapter
import com.instagram.light.profile.presentation.adapter.UsersAdapter
import com.instagram.light.profile.presentation.viewModel.ProfileViewModel
import com.instagram.light.utils.Constants
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        initUsersRv()
        observeUsersList()
        observeUsersLoadingProgress()
        profileViewModel.getUsers()
        observeFailure()
    }

    private fun initUsersRv() {
        binding.usersRv.apply {
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = UsersAdapter(ArrayList(), ::onItemClick)
        }
    }

    private fun observeUsersList() {
        lifecycleScope.launch {
            profileViewModel.usersList.collect { list ->
                if (!list.isNullOrEmpty()) {
                    getUsersAdapter()?.updateData(list)
                }
            }

        }
    }

    private fun observeUsersLoadingProgress() {
        lifecycleScope.launch {
            profileViewModel.usersLoading.collect { loading ->
                if (loading) {
                    binding.usersRv.showShimmer()
                } else {
                    binding.usersRv.hideShimmer()
                }
            }
        }
    }

    private fun getUsersAdapter() =
        binding.usersRv.adapter as? UsersAdapter

    private fun onItemClick(user: User, albumsRv: ShimmerRecyclerView) {
        if (user.expandable) {
            initAlbumsRv(albumsRv)
            observeAlbumsList(albumsRv)
            observeAlbumsLoadingProgress(albumsRv)
            profileViewModel.getAlbums(user.id)
        }
    }

    private fun initAlbumsRv(albumsRv: ShimmerRecyclerView) {
        albumsRv.apply {
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = AlbumsAdapter(ArrayList(), ::onSubItemClick)
        }
    }

    private fun observeAlbumsList(albumsRv: ShimmerRecyclerView) {
        lifecycleScope.launch {
            profileViewModel.albumsList.collect { list ->
                if (list != null) {
                    getAlbumsAdapter(albumsRv)?.updateData(list)

                }
            }
        }
    }

    private fun observeAlbumsLoadingProgress(albumsRv: ShimmerRecyclerView) {
        lifecycleScope.launch {
            profileViewModel.albumsLoading.collect { loading ->
                if (loading) {
                    albumsRv.showShimmer()
                } else {
                    albumsRv.hideShimmer()
                }
            }
        }
    }

    private fun getAlbumsAdapter(albumsRv: ShimmerRecyclerView) =
        albumsRv.adapter as? AlbumsAdapter

    private fun onSubItemClick(album: Album) {
        val intent =
            Intent(this@ProfileActivity, AlbumDetailsActivity::class.java)
        intent.putExtra(Constants.ALBUM_ID, album.id)
        startActivity(intent)
    }

    private fun observeFailure() {
        lifecycleScope.launch {
            profileViewModel.errorMessage.collect { message ->
                if (!message.isNullOrEmpty()) Toast.makeText(
                    this@ProfileActivity, message, Toast.LENGTH_SHORT
                ).show()

            }
        }
    }
}