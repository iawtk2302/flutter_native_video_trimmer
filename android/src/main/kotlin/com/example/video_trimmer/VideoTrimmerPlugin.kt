package com.example.video_trimmer

import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.example.video_trimmer.handlers.*
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

@UnstableApi
class VideoTrimmerPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var handlers: Map<MethodName, BaseMethodHandler>

    companion object {
        const val CHANNEL_NAME = "flutter_native_video_trimmer"
    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        context = binding.applicationContext
        channel = MethodChannel(binding.binaryMessenger, CHANNEL_NAME)
        channel.setMethodCallHandler(this)

        handlers = mapOf(
            MethodName.LOAD_VIDEO to LoadVideoHandler(context),
            MethodName.TRIM_VIDEO to TrimVideoHandler(context),
            MethodName.GET_VIDEO_THUMBNAIL to GetVideoThumbnailHandler(context),
            MethodName.GET_VIDEO_INFO to GetVideoInfoHandler(context),
            MethodName.CLEAR_TRIM_VIDEO_CACHE to ClearTrimVideoCacheHandler(context)
        )
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        VideoManager.getInstance(context).release()
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val methodName = MethodName.fromString(call.method)
        if (methodName == null) {
            result.notImplemented()
            return
        }
        handlers[methodName]?.handle(call, result) ?: result.notImplemented()
    }
}