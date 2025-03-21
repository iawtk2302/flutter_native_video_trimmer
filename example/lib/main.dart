import 'package:flutter/material.dart';
import 'package:flutter_native_video_trimmer/flutter_native_video_trimmer.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _videoTrimmer = VideoTrimmer();

  // Example of how to use the video trimmer functions
  Future<void> _exampleUsage() async {
    try {
      // 1. Load a video file
      await _videoTrimmer.loadVideo('/path/to/your/video.mp4');

      // 3. Trim the video (first 5 seconds)
      final trimmedPath = await _videoTrimmer.trimVideo(
        startTimeMs: 0,
        endTimeMs: 5000,
      );
      print('Video trimmed to: $trimmedPath');

      // 4. Clear the cache
      await _videoTrimmer.clearCache();
    } catch (e) {
      print('Error: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: Scaffold(
        body: Center(
          child: Text('Check console for function usage example'),
        ),
      ),
    );
  }
}
