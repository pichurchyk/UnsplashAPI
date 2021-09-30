package com.example.pichurchyk_p3.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Photo::class, Query::class], version = 1, exportSchema = false)
abstract class WallpapersDatabase : RoomDatabase() {
    abstract fun dao(): WallpapersDao

}