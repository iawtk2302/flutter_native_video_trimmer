package com.example.video_trimmer.handlers

import android.content.Context
import com.example.video_trimmer.BaseMethodHandler
import com.example.video_trimmer.VideoManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class GetVideoInfoHandler(private val context: Context) : BaseMethodHandler {
    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        try {
            val info = VideoManager.getInstance(context).getVideoInfo()
            result.success(info)
        } catch (e: Exception) {
            result.error("INFO_ERROR", e.message, null)
        }
    }
}
