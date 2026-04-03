// swift-tools-version:5.9
import PackageDescription

// LOCAL DEV: This Package.swift uses the xcframework built by build.sh ios.
// Activated by: ./scripts/ios-demo-source.sh local
// Restored by:  ./scripts/ios-demo-source.sh spm

let package = Package(
    name: "VoxaTrace",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "VoxaTrace", targets: ["VoxaTrace"]),
    ],
    targets: [
        .binaryTarget(
            name: "VoxaTrace",
            path: "demo-apps/ios/VoxaTraceDemo/Frameworks/voxatrace.xcframework"
        ),
    ]
)
