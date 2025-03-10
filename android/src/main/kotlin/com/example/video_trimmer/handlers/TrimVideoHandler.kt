package com.example.video_trimmer.handlers

import com.example.video_trimmer.BaseMethodHandler
import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.example.video_trimmer.VideoManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope    
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@UnstableApi
class TrimVideoHandler (private val context: Context): BaseMethodHandler {
    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        val startTimeMs = call.argument<Number>("startTimeMs")?.toLong()
        val endTimeMs = call.argument<Number>("endTimeMs")?.toLong()
        val includeAudio = call.argument<Boolean>("includeAudio")?:true
    
        if (startTimeMs == null || endTimeMs == null) {
            result.error("INVALID_ARGUMENTS", "Missing startTimeMs/endTimeMs parameters", null)
            return
        }
    
        // Create a new scope that's tied only to this method call
        val methodScope = CoroutineScope(Dispatchers.Main + Job())
        
        methodScope.launch {
            try {
                val path = VideoManager.getInstance().trimVideo(
                    context,
                    startTimeMs = startTimeMs,
                    endTimeMs = endTimeMs,
                    includeAudio=includeAudio
                )
                result.success(path)
            } catch (e: Exception) {
                result.error("TRIM_ERROR", e.message, null)
            } finally {
                methodScope.cancel() // Always clean up the scope
            }
        }
    }
}
