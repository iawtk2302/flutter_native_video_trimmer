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
  /// Set [includeAudio] to false to remove audio from the trimmed video.
  Future<String?> trimVideo({
    required int startTimeMs,
    required int endTimeMs,
    bool includeAudio = true,
  }) {
    return VideoTrimmerPlatform.instance.trimVideo(
      startTimeMs: startTimeMs,
      endTimeMs: endTimeMs,
      includeAudio: includeAudio,
    );
  }

  /// Clears any cached files created during video trimming
  Future<void> clearCache() {
    return VideoTrimmerPlatform.instance.clearCache();
  }
}
