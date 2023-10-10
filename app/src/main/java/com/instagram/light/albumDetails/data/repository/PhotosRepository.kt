package com.instagram.light.albumDetails.data.repository

import com.instagram.light.albumDetails.domain.repository.IPhotosRepository
import com.instagram.light.network.ApiService
import com.paulrybitskyi.hiltbinder.BindType
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@BindType
@ViewModelScoped
class PhotosRepository @Inject constructor(private val apiService: ApiService) : IPhotosRepository {
    override suspend fun getPhotos(albumId: Int) = withContext(Dispatchers.IO) {
        apiService.getPhotos(albumId)
    }
}