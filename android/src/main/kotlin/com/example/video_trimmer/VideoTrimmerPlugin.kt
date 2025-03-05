package com.example.video_trimmer

import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.example.video_trimmer.handlers.*
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler


class VideoTrimmerPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var methodManager: MethodManager

    companion object {
        const val CHANNEL_NAME = "flutter_native_video_trimmer"
    }

    @UnstableApi override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        context = binding.applicationContext
        channel = MethodChannel(binding.binaryMessenger, CHANNEL_NAME)
        channel.setMethodCallHandler(this)
        methodManager = MethodManager(context)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        VideoManager.getInstance().release()
        channel.setMethodCallHandler(null)
    }

    @UnstableApi override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        methodManager.handle(call,result)
    }
}