package com.example.video_trimmer.handlers

import android.content.Context
import com.example.video_trimmer.BaseMethodHandler
import com.example.video_trimmer.VideoManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class ClearTrimVideoCacheHandler(private val context: Context) : BaseMethodHandler {
    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        VideoManager.getInstance(context).clearCache()
        result.success(null)
    }
}
