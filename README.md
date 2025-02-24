# Flutter Native Video Trimmer

A lightweight Flutter plugin for video manipulation that uses pure native implementations (Media3 for Android and AVFoundation for iOS). Efficiently trim videos, generate thumbnails, and retrieve video information without any FFmpeg dependency.

### Why choose this plugin?

- **No FFmpeg Dependency**: Uses platform-native video processing capabilities instead of heavy FFmpeg libraries
- **Lightweight**: Smaller app size and faster processing compared to FFmpeg-based solutions
- **Native Performance**: Direct use of Media3 (Android) and AVFoundation (iOS) for optimal performance
- **Memory Efficient**: Processes videos without loading entire files into memory

## Features

- Load video files
- Trim videos with millisecond precision
- Generate video thumbnails with customizable size and quality
- Get video metadata (duration, dimensions, etc.)
- Clean and efficient native implementations

## Installation

Add this to your package's `pubspec.yaml` file:

```yaml
dependencies:
  flutter_native_video_trimmer: ^1.0.0
```

## Usage

### Import

```dart
import 'package:flutter_native_video_trimmer/video_trimmer.dart';
```

### Initialize

```dart
final videoTrimmer = VideoTrimmer();
```

### Load a Video

```dart
await videoTrimmer.loadVideo('/path/to/video.mp4');
```

### Trim Video

```dart
// Trim the first 5 seconds of the video
final trimmedPath = await videoTrimmer.trimVideo(
  startTimeMs: 0,     // Start time in milliseconds
  endTimeMs: 5000,    // End time in milliseconds (5 seconds)
);
```

### Generate Thumbnail

```dart
// Generate a thumbnail at 1 second mark
final thumbnailPath = await videoTrimmer.getVideoThumbnail(
  positionMs: 1000,   // Position in milliseconds
  quality: 100,       // Quality (0-100)
  width: 640,         // Optional width
  height: 480,        // Optional height
);
```

### Get Video Information

```dart
final videoInfo = await videoTrimmer.getVideoInfo();
print('Duration: ${videoInfo['duration']}');
print('Width: ${videoInfo['width']}');
print('Height: ${videoInfo['height']}');
```

## Example

Check the [example](example) folder for a complete sample app demonstrating all features.

## Platform Support

| Platform | Support |
|----------|----------|
| Android  | ✅        |
| iOS      | ✅        |

## Requirements

### Android
- Minimum SDK version: 21
- Target SDK version: 34
- Kotlin version: 1.9.0

### iOS
- Minimum deployment target: 11.0
- Swift version: 5.0

## Contributing

Feel free to contribute to this project by:
1. Reporting bugs
2. Suggesting enhancements
3. Creating pull requests

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
