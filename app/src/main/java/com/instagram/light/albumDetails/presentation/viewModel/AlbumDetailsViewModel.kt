package com.instagram.light.albumDetails.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagram.light.albumDetails.data.model.Photo
import com.instagram.light.albumDetails.domain.repository.IPhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private var photosRepository: IPhotosRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _photosList = MutableStateFlow<ArrayList<Photo>?>(ArrayList())
    val photosList: StateFlow<ArrayList<Photo>?> = _photosList

    private val cashedList: ArrayList<Photo> = ArrayList()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    fun getPhotos(albumId: Int) {
        viewModelScope.launch {
            _loading.value = true
            val response = photosRepository.getPhotos(albumId)
            _loading.value = false
            if (response.isSuccessful) {
                _photosList.emit(response.body())
                cashedList.clear()
                _photosList.value?.let { cashedList.addAll(it) }
            } else {
                _errorMessage.value = response.message()
            }

        }
    }

    fun search(searchQuery: String) {
        val results = ArrayList<Photo>()

        viewModelScope.launch {
            if (searchQuery.isEmpty()) {
                _photosList.value = ArrayList()
                _photosList.emit(cashedList)
            } else {
                for (item in cashedList) {
                    if (item.title.contains(searchQuery, ignoreCase = true)) {
                        results.add(item)
                    }
                }

                _photosList.emit(results)
            }
        }
    }
}