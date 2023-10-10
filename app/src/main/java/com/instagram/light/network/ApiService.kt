package com.instagram.light.network

import com.instagram.light.albumDetails.data.model.Photo
import com.instagram.light.profile.data.model.Album
import com.instagram.light.profile.data.model.User
import com.instagram.light.utils.NetworkUtils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(NetworkUtils.USERS)
    suspend fun getUsers(): Response<ArrayList<User>>

    @GET(NetworkUtils.ALBUMS)
    suspend fun getAlbums(@Query("userId") userId: Int): Response<ArrayList<Album>>

    @GET(NetworkUtils.PHOTOS)
    suspend fun getPhotos(@Query("albumId") albumId: Int): Response<ArrayList<Photo>>
}