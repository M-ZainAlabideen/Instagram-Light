package com.instagram.light.profile.domain.repository

import com.instagram.light.profile.data.model.User
import retrofit2.Response

interface IUsersRepository {
    suspend fun getUsers(): Response<ArrayList<User>>
}