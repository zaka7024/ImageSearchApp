package com.zaka7024.imagesearchapp

import android.app.Application
import com.downloader.PRDownloader
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ImageSearchApp: Application() {
    override fun onCreate() {
        super.onCreate()
        PRDownloader.initialize(getApplicationContext());
    }
}