package com.instagram.light.profile.data.repository

import com.instagram.light.network.ApiService
import com.instagram.light.profile.domain.repository.IUsersRepository
import com.paulrybitskyi.hiltbinder.BindType
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


@BindType
@ViewModelScoped
class UsersRepository @Inject constructor(private val apiService: ApiService) : IUsersRepository {
    override suspend fun getUsers() = withContext(Dispatchers.IO) {
        apiService.getUsers()
    }
}