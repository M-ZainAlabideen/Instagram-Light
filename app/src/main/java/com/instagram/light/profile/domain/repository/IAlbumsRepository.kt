package com.instagram.light.profile.domain.repository

import com.instagram.light.profile.data.model.Album
import retrofit2.Response

interface IAlbumsRepository {
    suspend fun getAlbums(userId: Int): Response<ArrayList<Album>>
}