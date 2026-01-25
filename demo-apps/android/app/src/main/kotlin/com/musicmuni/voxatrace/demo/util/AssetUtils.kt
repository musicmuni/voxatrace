package com.musicmuni.voxatrace.demo.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Copies an asset file to the app's cache directory.
 * Returns the File object pointing to the cached copy.
 */
fun copyAssetToFile(context: Context, assetName: String): File {
    val file = File(context.cacheDir, assetName)
    if (!file.exists()) {
        context.assets.open(assetName).use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }
    return file
}

/**
 * Suspend version of copyAssetToFile for coroutine contexts.
 * Runs on IO dispatcher.
 */
suspend fun copyAssetToCache(context: Context, assetName: String): String {
    return withContext(Dispatchers.IO) {
        copyAssetToFile(context, assetName).absolutePath
    }
}
