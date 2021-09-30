package com.example.pichurchyk_p3.api

import com.example.pichurchyk_p3.model.UnsplashPhoto

data class UnsplashResponse(
    val total: Int,
    val results: List<UnsplashPhoto>
)