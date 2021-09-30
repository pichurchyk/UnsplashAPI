package com.example.pichurchyk_p3.model

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.pichurchyk_p3.api.UnsplashApi
import com.example.pichurchyk_p3.room.Photo
import com.example.pichurchyk_p3.room.Query
import com.example.pichurchyk_p3.room.WallpapersDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val dao: WallpapersDao
) {
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
        ).liveData

    suspend fun getTotalSearchResults(query: String) = unsplashApi.searchPhotos(query, 1,1).total

    val allFavoritePhotos: Flow<List<Photo>> = dao.getAllFavoritePhotos()
    val allQueries: Flow<List<Query>> = dao.getAllQueries()
    val allFavoriteQueries: Flow<List<Query>> = dao.getLikedQueries()

    suspend fun getPhotoById(id: String) =
        unsplashApi.searchPhotoById(id)
    fun likePhoto(photo: Photo) {
        dao.addPhoto(photo)
    }
    fun deleteFromFavorites(photo: Photo) {
        dao.deleteFromFavorite(photo)
    }
    fun isPhotoExists(id: String): Boolean {
        return dao.isPhotoExists(id)
    }

    fun addQuery(query: Query) {
        dao.addQuery(query)
    }
    fun changeQueryLikeState(query: Query) {
        dao.changeQueryLikeState(query)
    }
    fun deleteQueries(){
        dao.deleteQueries()
    }
}