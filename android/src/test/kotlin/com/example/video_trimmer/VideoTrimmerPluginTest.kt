package com.example.video_trimmer

import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.mockito.Mockito

/*
 * Unit tests of the Kotlin portion of this plugin's implementation.
 *
 * Once you have built the plugin's example app, you can run these tests from the command
 * line by running `./gradlew testDebugUnitTest` in the `example/android/` directory, or
 * you can run them directly from IDEs that support JUnit such as Android Studio.
 */

internal class VideoTrimmerPluginTest {
  private val plugin = VideoTrimmerPlugin()

  @BeforeTest
  fun initPlugin() {
    val binding = Mockito.mock(FlutterPlugin.FlutterPluginBinding::class.java)
    Mockito.`when`(binding.applicationContext).thenReturn(Mockito.mock(Context::class.java))
    Mockito.`when`(binding.binaryMessenger).thenReturn(Mockito.mock(BinaryMessenger::class.java))
    plugin.onAttachedToEngine(binding)
  }

  @Test
  fun onMethodCall_unknownMethod_returnsNotImplemented() {
    val mockResult: MethodChannel.Result = Mockito.mock(MethodChannel.Result::class.java)
    plugin.onMethodCall(MethodCall("getPlatformVersion", null), mockResult)

    Mockito.verify(mockResult).notImplemented()
  }
}
