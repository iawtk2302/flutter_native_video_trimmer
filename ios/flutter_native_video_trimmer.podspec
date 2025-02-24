#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint video_trimmer.podspec` to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'flutter_native_video_trimmer'
  s.version          = '1.0.0'
  s.summary          = 'A Flutter plugin for video manipulation using native code'
  s.description      = <<-DESC
A lightweight Flutter plugin for video manipulation that uses pure native implementations (Media3 for Android and AVFoundation for iOS). Efficiently trim videos, generate thumbnails, and retrieve video information without FFmpeg dependency.
                       DESC
  s.homepage         = 'https://github.com/iawtk2302/flutter_native_video_trimmer'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'iawtk2302' => 'https://github.com/iawtk2302' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.platform = :ios, '12.0'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'

  # If your plugin requires a privacy manifest, for example if it uses any
  # required reason APIs, update the PrivacyInfo.xcprivacy file to describe your
  # plugin's privacy impact, and then uncomment this line. For more information,
  # see https://developer.apple.com/documentation/bundleresources/privacy_manifest_files
  s.resource_bundles = {'flutter_native_video_trimmer_privacy' => ['Resources/PrivacyInfo.xcprivacy']}
end
