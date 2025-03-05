package com.example.video_trimmer.handlers

import com.example.video_trimmer.BaseMethodHandler
import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.example.video_trimmer.VideoManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

@UnstableApi
class LoadVideoHandler: BaseMethodHandler {
    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        val path = call.argument<String>("path")
        if (path == null) {
            result.error("INVALID_ARGUMENTS", "Missing path parameter", null)
            return
        }

        try {
            VideoManager.getInstance().loadVideo(path)
            result.success(null)
        } catch (e: Exception) {
            result.error("LOAD_ERROR", e.message, null)
        }
    }
}
