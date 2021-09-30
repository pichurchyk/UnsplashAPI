package com.example.pichurchyk_p3.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos_table")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val imgId: String,
    val imgUrl: String,
)