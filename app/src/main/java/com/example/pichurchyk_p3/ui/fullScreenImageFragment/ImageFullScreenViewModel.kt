package com.example.pichurchyk_p3.ui.fullScreenImageFragment

import android.annotation.SuppressLint
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pichurchyk_p3.model.UnsplashPhoto
import com.example.pichurchyk_p3.model.UnsplashRepository
import com.example.pichurchyk_p3.room.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ImageFullScreenViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    private var photo: UnsplashPhoto? = null

    fun setPhoto(givenPhoto: UnsplashPhoto) {
        photo = givenPhoto
    }

    fun getPhoto(): UnsplashPhoto {
        return photo!!
    }

    private var queryText: String? = null
    fun setQuery(givenQuery: String) {
        queryText = givenQuery
    }

    fun likePhoto(photo: Photo) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.likePhoto(photo)
        }
    }

    fun isPhotoExists(id: String): Boolean {
        return repository.isPhotoExists(id)
    }

    fun validateQueryLength(): String {
        return if (queryText!!.length > 35) {
            "${queryText!!.substring(0, 35)}..."
        } else {
            queryText!!
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getPhotoValidatedDate(): String {
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val photoDate: Date = inputFormat.parse(photo!!.created_at.substring(0, 10))!!
        return outputFormat.format(photoDate)
    }
}