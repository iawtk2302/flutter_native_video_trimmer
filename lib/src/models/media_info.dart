/// A class that represents video media information.
class MediaInfo {
  /// The width of the video in pixels.
  final int width;

  /// The height of the video in pixels.
  final int height;

  /// The duration of the video in milliseconds.
  final int durationMs;

  /// The mime type of the video.
  final String? mimeType;

  /// Creates a new [MediaInfo] instance.
  const MediaInfo({
    required this.width,
    required this.height,
    required this.durationMs,
    this.mimeType,
  });

  /// Creates a [MediaInfo] instance from a map.
  factory MediaInfo.fromMap(Map<String, dynamic> map) {
    return MediaInfo(
      width: map['width'] as int? ?? 0,
      height: map['height'] as int? ?? 0,
      durationMs: map['durationMs'] as int? ?? 0,
      mimeType: map['mimeType'] as String?,
    );
  }

  /// Converts this [MediaInfo] instance to a map.
  Map<String, dynamic> toMap() {
    return {
      'width': width,
      'height': height,
      'durationMs': durationMs,
      'mimeType': mimeType,
    };
  }

  /// Creates a copy of this [MediaInfo] with the given fields replaced with the new values.
  MediaInfo copyWith({
    int? width,
    int? height,
    int? durationMs,
    int? rotation,
    int? size,
    String? mimeType,
  }) {
    return MediaInfo(
      width: width ?? this.width,
      height: height ?? this.height,
      durationMs: durationMs ?? this.durationMs,
      mimeType: mimeType ?? this.mimeType,
    );
  }

  @override
  String toString() {
    return 'MediaInfo(width: $width, height: $height, durationMs: $durationMs, mimeType: $mimeType)';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is MediaInfo &&
        other.width == width &&
        other.height == height &&
        other.durationMs == durationMs &&
        other.mimeType == mimeType;
  }

  @override
  int get hashCode {
    return width.hashCode ^
        height.hashCode ^
        durationMs.hashCode ^
        mimeType.hashCode;
  }
}
