package com.example.pichurchyk_p3.di

import android.content.Context
import androidx.room.Room
import com.example.pichurchyk_p3.api.UnsplashApi
import com.example.pichurchyk_p3.room.WallpapersDao
import com.example.pichurchyk_p3.room.WallpapersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi =
        retrofit.create(UnsplashApi::class.java)

    @Singleton
    @Provides
    fun provideSearchDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            WallpapersDatabase::class.java,
            "wallpapers_database"
        ).build()

    @Provides
    fun provideSearchDAO(appDatabase: WallpapersDatabase): WallpapersDao {
        return appDatabase.dao()
    }
}