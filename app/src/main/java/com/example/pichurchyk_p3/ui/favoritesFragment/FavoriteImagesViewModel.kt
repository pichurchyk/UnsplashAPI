package com.example.pichurchyk_p3.ui.favoritesFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pichurchyk_p3.model.UnsplashPhoto
import com.example.pichurchyk_p3.model.UnsplashRepository
import com.example.pichurchyk_p3.room.Photo
import kotlinx.coroutines.flow.Flow

class FavoriteImagesViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    lateinit var photo: List<UnsplashPhoto>
    val allFavoritePhotos: Flow<List<Photo>> = repository.allFavoritePhotos

    suspend fun getPhotoById(id: String) {
        photo = listOf(repository.getPhotoById(id))
    }

    fun deleteFromFavorites(photo: Photo) {
        repository.deleteFromFavorites(photo)
    }
}