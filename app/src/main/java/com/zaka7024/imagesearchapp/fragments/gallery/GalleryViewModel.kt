package com.zaka7024.imagesearchapp.fragments.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zaka7024.imagesearchapp.data.UnsplashRepository

class GalleryViewModel @ViewModelInject constructor(private val unsplashRepository: UnsplashRepository) :
    ViewModel() {

    companion object {
        private const val DEFAULT_SEARCH_QUERY = "cats"
    }

    private val currentQuery = MutableLiveData(DEFAULT_SEARCH_QUERY)

    val photos = currentQuery.switchMap {
        query ->
        unsplashRepository.getSearchResults(query).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }
}