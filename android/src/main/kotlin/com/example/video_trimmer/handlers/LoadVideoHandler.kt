package com.example.video_trimmer.handlers

import android.content.Context
import com.example.video_trimmer.BaseMethodHandler
import com.example.video_trimmer.VideoManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class LoadVideoHandler(private val context: Context) : BaseMethodHandler {
    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        val path = call.argument<String>("path")
        if (path == null) {
            result.error("INVALID_ARGUMENTS", "Missing path parameter", null)
            return
        }

        try {
            VideoManager.getInstance(context).loadVideo(path)
            result.success(null)
        } catch (e: Exception) {
            result.error("LOAD_ERROR", e.message, null)
        }
    }
}
