package com.example.pichurchyk_p3.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface WallpapersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPhoto(photo: Photo)

    @androidx.room.Query("SELECT EXISTS (SELECT 1 FROM photos_table WHERE imgId = :id)")
    fun isPhotoExists(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQuery(query: Query)

    @androidx.room.Query("Select * FROM photos_table ORDER BY id ASC")
    fun getAllFavoritePhotos():Flow<List<Photo>>

    @androidx.room.Query("Select * FROM queries_table ORDER BY date ASC")
    fun getAllQueries():Flow<List<Query>>

    @androidx.room.Query("Select * FROM queries_table WHERE isLiked = 1")
    fun getLikedQueries():Flow<List<Query>>

    @androidx.room.Query("DELETE FROM queries_table")
    fun deleteQueries()

    @Delete
    fun deleteFromFavorite(photo: Photo)

    @Update
    fun changeQueryLikeState(query: Query)
}