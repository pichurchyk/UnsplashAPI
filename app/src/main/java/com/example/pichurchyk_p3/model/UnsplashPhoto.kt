package com.example.pichurchyk_p3.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val created_at: String,
    val width: Int,
    val height: Int,
    val color: String?,
    val urls: UnsplashPhotoUrl,
    val user: UnsplashUser
):Parcelable {

    @Parcelize
    data class UnsplashUser(
        val name: String,
        val username: String,
        val instagram_username: String?,
        val twitter_username: String?,
        val portfolio_url: String?,
        val profile_image: UnsplashUserPhotoUrl?,
    ):Parcelable {

        @Parcelize
        data class UnsplashUserPhotoUrl(
            val small: String,
            val medium: String,
            val large: String
        ):Parcelable
    }

    @Parcelize
    data class UnsplashPhotoUrl(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    ):Parcelable
}