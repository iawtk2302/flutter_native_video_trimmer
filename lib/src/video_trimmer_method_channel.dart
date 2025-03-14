import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'video_trimmer_platform_interface.dart';

/// An implementation of [VideoTrimmerPlatform] that uses method channels.
class MethodChannelVideoTrimmer extends VideoTrimmerPlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_native_video_trimmer');

  @override
  Future<void> loadVideo(String path) async {
    await methodChannel.invokeMethod<void>('loadVideo', {'path': path});
  }

  @override
  Future<String?> trimVideo({
    required int startTimeMs,
    required int endTimeMs,
    bool includeAudio = true,
  }) async {
    final result = await methodChannel.invokeMethod<String>('trimVideo', {
      'startTimeMs': startTimeMs,
      'endTimeMs': endTimeMs,
      'includeAudio': includeAudio,
    });
    return result;
  }

  @override
  Future<void> clearCache() async {
    await methodChannel.invokeMethod<void>('clearTrimVideoCache');
  }
}
