/// A class that represents video media information.
class MediaInfo {
  /// The width of the video in pixels.
  final int width;

  /// The height of the video in pixels.
  final int height;

  /// The duration of the video in milliseconds.
  final int durationMs;

  /// The rotation angle of the video in degrees.
  final int rotation;

  /// The size of the video file in bytes.
  final int size;

  /// The mime type of the video.
  final String? mimeType;

  /// Creates a new [MediaInfo] instance.
  const MediaInfo({
    required this.width,
    required this.height,
    required this.durationMs,
    required this.rotation,
    required this.size,
    this.mimeType,
  });

  /// Creates a [MediaInfo] instance from a map.
  factory MediaInfo.fromMap(Map<String, dynamic> map) {
    return MediaInfo(
      width: map['width'] as int? ?? 0,
      height: map['height'] as int? ?? 0,
      durationMs: map['durationMs'] as int? ?? 0,
      rotation: map['rotation'] as int? ?? 0,
      size: map['size'] as int? ?? 0,
      mimeType: map['mimeType'] as String?,
    );
  }

  /// Converts this [MediaInfo] instance to a map.
  Map<String, dynamic> toMap() {
    return {
      'width': width,
      'height': height,
      'durationMs': durationMs,
      'rotation': rotation,
      'size': size,
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
      rotation: rotation ?? this.rotation,
      size: size ?? this.size,
      mimeType: mimeType ?? this.mimeType,
    );
  }

  @override
  String toString() {
    return 'MediaInfo(width: $width, height: $height, durationMs: $durationMs, rotation: $rotation, size: $size, mimeType: $mimeType)';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
  
    return other is MediaInfo &&
      other.width == width &&
      other.height == height &&
      other.durationMs == durationMs &&
      other.rotation == rotation &&
      other.size == size &&
      other.mimeType == mimeType;
  }

  @override
  int get hashCode {
    return width.hashCode ^
      height.hashCode ^
      durationMs.hashCode ^
      rotation.hashCode ^
      size.hashCode ^
      mimeType.hashCode;
  }
}
