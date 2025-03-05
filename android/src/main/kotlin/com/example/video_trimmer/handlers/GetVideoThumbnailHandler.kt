package com.example.video_trimmer.handlers


import com.example.video_trimmer.BaseMethodHandler
import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.example.video_trimmer.VideoManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel


@UnstableApi
class GetVideoThumbnailHandler(private val context: Context) : BaseMethodHandler {

    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        val positionMs = call.argument<Number>("positionMs")?.toLong() // Long for milliseconds
        val quality = call.argument<Number>("quality")?.toInt() // Int for quality percentage (0-100)
        val width = call.argument<Number>("width")?.toInt() // Int for pixel dimensions
        val height = call.argument<Number>("height")?.toInt() // Int for pixel dimensions

        if (positionMs == null || quality == null) {
            result.error("INVALID_ARGUMENTS", "Missing positionMs/quality parameters", null)
            return
        }

        // Create a new scope that's tied only to this method call
        val methodScope = CoroutineScope(Dispatchers.Main + Job())

        methodScope.launch {
            try {
                val path = VideoManager.getInstance().generateThumbnail(
                    context,
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
            finally {
                methodScope.cancel()
            }
        }
    }
}
