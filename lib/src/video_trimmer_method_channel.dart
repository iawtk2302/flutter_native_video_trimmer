import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'models/models.dart';

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
  }) async {
    final result = await methodChannel.invokeMethod<String>('trimVideo', {
      'startTimeMs': startTimeMs,
      'endTimeMs': endTimeMs,
    });
    return result;
  }

  @override
  Future<String?> getVideoThumbnail({
    required int positionMs,
    required int quality,
    int? width,
    int? height,
  }) async {
    final result =
        await methodChannel.invokeMethod<String>('getVideoThumbnail', {
      'position': positionMs,
      'quality': quality,
      'width': width,
      'height': height,
    });
    return result;
  }

  @override
  Future<MediaInfo> getVideoInfo() async {
    final result =
        await methodChannel.invokeMethod<Map<Object?, Object?>>('getVideoInfo');
    final map = Map<String, dynamic>.from(result ?? {});
    return MediaInfo.fromMap(map);
  }

  @override
  Future<void> clearCache() async {
    await methodChannel.invokeMethod<void>('clearTrimVideoCache');
  }
}
