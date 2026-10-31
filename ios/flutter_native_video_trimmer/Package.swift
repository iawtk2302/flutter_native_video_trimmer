// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "flutter_native_video_trimmer",
    platforms: [
        .iOS("11.0")
    ],
    products: [
        .library(
            name: "flutter-native-video-trimmer",
            targets: ["flutter_native_video_trimmer"]
        )
    ],
    dependencies: [
        .package(name: "FlutterFramework", path: "../FlutterFramework")
    ],
    targets: [
        .target(
            name: "flutter_native_video_trimmer",
            dependencies: [
                .product(name: "FlutterFramework", package: "FlutterFramework")
            ],
            resources: [
                .process("PrivacyInfo.xcprivacy")
            ]
        )
    ]
)
