package com.instagram.light.albumDetails.domain.repository

import com.instagram.light.albumDetails.data.model.Photo
import retrofit2.Response

interface IPhotosRepository {
    suspend fun getPhotos(albumId: Int): Response<ArrayList<Photo>>
}