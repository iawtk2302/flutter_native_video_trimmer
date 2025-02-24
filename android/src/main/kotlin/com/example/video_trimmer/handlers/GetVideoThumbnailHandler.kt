package com.example.video_trimmer.handlers

import android.content.Context
import com.example.video_trimmer.BaseMethodHandler
import com.example.video_trimmer.VideoManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GetVideoThumbnailHandler(private val context: Context) : BaseMethodHandler {
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        val positionMs = call.argument<Number>("positionMs")?.toLong() // Long for milliseconds
        val quality = call.argument<Number>("quality")?.toInt() // Int for quality percentage (0-100)
        val width = call.argument<Number>("width")?.toInt() // Int for pixel dimensions
        val height = call.argument<Number>("height")?.toInt() // Int for pixel dimensions

        if (positionMs == null || quality == null) {
            result.error("INVALID_ARGUMENTS", "Missing positionMs/quality parameters", null)
            return
        }

        scope.launch {
            try {
                val path = VideoManager.getInstance(context).generateThumbnail(
                    positionMs = positionMs,
                    width = width,
                    height = height,
                    quality = quality
                )
                withContext(Dispatchers.Main) {
                    result.success(path)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.error("THUMBNAIL_ERROR", e.message, null)
                }
            }
        }
    }
}
