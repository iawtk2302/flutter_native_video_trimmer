package com.example.video_trimmer.handlers

import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.example.video_trimmer.BaseMethodHandler
import com.example.video_trimmer.VideoManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope    
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@UnstableApi
class TrimVideoHandler(private val context: Context) : BaseMethodHandler {
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        val startTimeMs = call.argument<Number>("startTimeMs")?.toLong() // Long for milliseconds
        val endTimeMs = call.argument<Number>("endTimeMs")?.toLong() // Long for milliseconds

        if (startTimeMs == null || endTimeMs == null) {
            result.error("INVALID_ARGUMENTS", "Missing startTimeMs/endTimeMs parameters", null)
            return
        }

        scope.launch {
            try {
                val path = VideoManager.getInstance(context).trimVideo(
                    startTimeMs = startTimeMs,
                    endTimeMs = endTimeMs
                )
                withContext(Dispatchers.Main) {
                    result.success(path)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.error("TRIM_ERROR", e.message, null)
                }
            }
        }
    }
}
