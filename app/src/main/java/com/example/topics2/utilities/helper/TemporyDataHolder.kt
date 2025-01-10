package com.example.topics2.utilities.helper

object TemporaryDataHolder {
    private var _imagePaths: List<String> = emptyList()

    fun setImagePaths(imagePathsP: List<String>) {
        _imagePaths = imagePathsP
    }

    fun getImagePaths(): List<String> { // Clear after retrieval
        return _imagePaths.also { _imagePaths = emptyList() }
    }
}