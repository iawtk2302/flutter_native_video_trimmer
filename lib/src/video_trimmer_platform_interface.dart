import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'models/models.dart';

import 'video_trimmer_method_channel.dart';

abstract class VideoTrimmerPlatform extends PlatformInterface {
  /// Constructs a VideoTrimmerPlatform.
  VideoTrimmerPlatform() : super(token: _token);

  static final Object _token = Object();

  static VideoTrimmerPlatform _instance = MethodChannelVideoTrimmer();

  /// The default instance of [VideoTrimmerPlatform] to use.
  ///
  /// Defaults to [MethodChannelVideoTrimmer].
  static VideoTrimmerPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [VideoTrimmerPlatform] when
  /// they register themselves.
  static set instance(VideoTrimmerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<void> loadVideo(String path) {
    throw UnimplementedError('loadVideo() has not been implemented.');
  }

  Future<String?> trimVideo({
    required int startTimeMs,
    required int endTimeMs,
  }) {
    throw UnimplementedError('trimVideo() has not been implemented.');
  }

  Future<String?> getVideoThumbnail({
    required int positionMs,
    required int quality,
    int? width,
    int? height,
  }) {
    throw UnimplementedError('getVideoThumbnail() has not been implemented.');
  }

  Future<MediaInfo> getVideoInfo() {
    throw UnimplementedError('getVideoInfo() has not been implemented.');
  }

  Future<void> clearCache() {
    throw UnimplementedError('clearCache() has not been implemented.');
  }
}
