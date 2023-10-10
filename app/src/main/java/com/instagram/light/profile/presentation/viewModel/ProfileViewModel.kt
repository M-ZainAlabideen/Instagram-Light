package com.instagram.light.profile.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagram.light.profile.data.model.Album
import com.instagram.light.profile.data.model.User
import com.instagram.light.profile.domain.repository.IAlbumsRepository
import com.instagram.light.profile.domain.repository.IUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private var usersRepository: IUsersRepository,
    private var albumsRepository: IAlbumsRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _usersList = MutableStateFlow<ArrayList<User>?>(ArrayList())
    val usersList: StateFlow<ArrayList<User>?> = _usersList

    private val _albumsList = MutableStateFlow<ArrayList<Album>?>(ArrayList())
    val albumsList: StateFlow<ArrayList<Album>?> = _albumsList

    private val _usersLoading = MutableStateFlow(false)
    val usersLoading: StateFlow<Boolean> = _usersLoading

    private val _albumsLoading = MutableStateFlow(false)
    val albumsLoading: StateFlow<Boolean> = _albumsLoading

    fun getUsers() {
        viewModelScope.launch {
            _usersLoading.value = true
            val response = usersRepository.getUsers()
            _usersLoading.value = false
            if (response.isSuccessful) {
                response.body()?.let { _usersList.emit(response.body()) }
            } else {
                _errorMessage.value = response.message()
            }

        }
    }

    fun getAlbums(userId: Int) {
        viewModelScope.launch {
            _albumsLoading.value = true
            val response = albumsRepository.getAlbums(userId)
            _albumsLoading.value = false
            if (response.isSuccessful) {
                _albumsList.emit(response.body())
            } else {
                _errorMessage.value = response.message()
            }

        }
    }
}