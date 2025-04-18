package com.GrandSphere.Topiks.utilities.helper

import android.util.Log

object TemporaryDataHolder {
    private var _imagePaths: List<String> = emptyList()

    fun setImagePaths(imagePathsP: List<String>) {
        _imagePaths = imagePathsP
    }

    fun getImagePaths(): List<String> { // Clear after retrieval
        Log.d("zzee", "This sould clear")
        return _imagePaths.also { _imagePaths = emptyList() }
        Log.d("zzee Current size:", _imagePaths.size.toString())
    }

}