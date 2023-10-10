package com.instagram.light.profile.data.repository

import com.instagram.light.network.ApiService
import com.instagram.light.profile.domain.repository.IAlbumsRepository
import com.paulrybitskyi.hiltbinder.BindType
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@BindType
@ViewModelScoped
class AlbumsRepository @Inject constructor(private val apiService: ApiService) : IAlbumsRepository {
    override suspend fun getAlbums(userId: Int) = withContext(Dispatchers.IO) {
            apiService.getAlbums(userId)
        }
    }