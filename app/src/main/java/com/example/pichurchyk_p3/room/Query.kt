package com.example.pichurchyk_p3.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queries_table")
data class Query(
    @PrimaryKey
    val queryText: String,
    val isLiked: Boolean,
    val total: Int,
    val date: String,
)