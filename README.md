# Flutter Native Video Trimmer

[![pub package](https://img.shields.io/pub/v/flutter_native_video_trimmer.svg)](https://pub.dev/packages/flutter_native_video_trimmer)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A lightweight Flutter plugin for video manipulation using native code. Trim videos without FFmpeg dependency.

## ⭐️ IMPORTANT ⭐️

This library focuses specifically on video trimming functionality.
If you need more advanced video editing features (trim, merge, crop, rotate, etc.), consider using [easy_video_editor](https://pub.dev/packages/easy_video_editor).

### Why choose this plugin?

- 🚀 **No FFmpeg Dependency**: Uses platform-native video processing capabilities instead of heavy FFmpeg libraries
- 🪶 **Lightweight**: Smaller app size and faster processing compared to FFmpeg-based solutions
- ⚡️ **Native Performance**: Direct use of Media3 (Android) and AVFoundation (iOS) for optimal performance
- 📱 **Memory Efficient**: Processes videos without loading entire files into memory
- 🔒 **Privacy Focused**: All processing happens locally on the device

## ✨ Features

- 📼 **Video Loading**: Load and process video files from any source
- ✂️ **Precise Trimming**: Trim videos with millisecond precision
- 🛠️ **Native Implementation**: Clean and efficient platform-specific code

## 📦 Installation

Add this to your package's `pubspec.yaml` file:

```yaml
dependencies:
  flutter_native_video_trimmer: ^1.1.9
```

Or install via command line:

```bash
flutter pub add flutter_native_video_trimmer
```

## 🚀 Usage

### Import

```dart
import 'package:flutter_native_video_trimmer/flutter_native_video_trimmer.dart';
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
  includeAudio: true, // Optional, default is true
);
```

### Clear Cache

```dart
// Clear the cache
await videoTrimmer.clearCache();
```

## Example

Check the [example](example) folder for a complete sample app demonstrating all features.

## 📱 Platform Support

| Platform | Implementation | Minimum Version | Status |
| -------- | -------------- | --------------- | ------ |
| Android  | Media3         | API 21 (5.0)    | ✅     |
| iOS      | AVFoundation   | iOS 11.0        | ✅     |

## 🛠️ Requirements

### Android

- Minimum SDK: API 21 (Android 5.0)
- Target SDK: API 34
- Kotlin: 1.9.0
- AndroidX

### iOS

- Minimum iOS: 11.0
- Swift: 5.0
- Xcode: Latest version

## 🤝 Contributing

Contributions are always welcome! Here's how you can help:

1. 🐛 Report bugs by opening an issue
2. 💡 Suggest new features or improvements
3. 📝 Improve documentation
4. 🔧 Submit pull requests

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📧 Author

[iawtk2302](https://github.com/iawtk2302)

## ⭐ Show Your Support

If you find this plugin helpful, please give it a star on [GitHub](https://github.com/iawtk2302/flutter_native_video_trimmer)! It helps others discover the plugin and motivates me to keep improving it.
