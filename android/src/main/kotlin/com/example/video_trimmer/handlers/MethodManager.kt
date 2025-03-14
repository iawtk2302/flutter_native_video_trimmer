 package com.example.video_trimmer.handlers

import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.example.video_trimmer.BaseMethodHandler
import com.example.video_trimmer.MethodName
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

 @UnstableApi
class MethodManager(private val context: Context):BaseMethodHandler{
    private val handlers: Map<MethodName, BaseMethodHandler> = mapOf(
    MethodName.LOAD_VIDEO to LoadVideoHandler(),
    MethodName.TRIM_VIDEO to TrimVideoHandler(context),
    MethodName.CLEAR_TRIM_VIDEO_CACHE to ClearTrimVideoCacheHandler(context)
    )

     override fun handle(call: MethodCall, result: MethodChannel.Result) {
         val methodName = MethodName.fromString(call.method)
         if (methodName == null) {
             result.notImplemented()
             return
         }
         handlers[methodName]?.handle(call, result) ?: result.notImplemented()
     }
 }