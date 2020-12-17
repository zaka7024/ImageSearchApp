package com.zaka7024.imagesearchapp.api

import com.zaka7024.imagesearchapp.data.UnsplashPhoto

data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)