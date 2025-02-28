// swift-tools-version:5.5
import PackageDescription

let package = Package(
    name: "FlutterNativeVideoTrimmer",
    platforms: [
        .iOS("13.0")
    ],
    products: [
        .library(
            name: "FlutterNativeVideoTrimmer",
            targets: ["FlutterNativeVideoTrimmer"]
        )
    ],
    dependencies: [],
    targets: [
        .target(
            name: "FlutterNativeVideoTrimmer",
            dependencies: [],
            path: "Classes"
        ),
        .testTarget(
            name: "FlutterNativeVideoTrimmerTests",
            dependencies: ["FlutterNativeVideoTrimmer"],
            path: "Tests"
        )
    ]
)
