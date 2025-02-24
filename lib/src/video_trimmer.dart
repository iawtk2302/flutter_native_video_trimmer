import 'dart:async';

import 'video_trimmer_platform_interface.dart';

class VideoTrimmer {
  /// Loads a video file from the given [path].
  Future<void> loadVideo(String path) {
    return VideoTrimmerPlatform.instance.loadVideo(path);
  }

  /// Trims the loaded video from [startTime] to [endTime].
  /// Returns the path to the trimmed video file.
  /// Times are in milliseconds.
  Future<String?> trimVideo({
    required int startTimeMs,
    required int endTimeMs,
  }) {
    return VideoTrimmerPlatform.instance.trimVideo(
      startTimeMs: startTimeMs,
      endTimeMs: endTimeMs,
    );
  }

  /// Gets a thumbnail from the video at the specified [position] in milliseconds.
  /// Returns the path to the generated thumbnail file.
  Future<String?> getVideoThumbnail({
    required int positionMs,
    required int quality,
    int? width,
    int? height,
  }) {
    return VideoTrimmerPlatform.instance.getVideoThumbnail(
      positionMs: positionMs,
      quality: quality,
      width: width,
      height: height,
    );
  }

  /// Gets information about the loaded video including duration, width, height, etc.
  Future<Map<String, dynamic>> getVideoInfo() {
    return VideoTrimmerPlatform.instance.getVideoInfo();
  }

  /// Clears any cached files created during video trimming
  Future<void> clearCache() {
    return VideoTrimmerPlatform.instance.clearCache();
  }
}
