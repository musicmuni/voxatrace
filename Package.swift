// swift-tools-version:5.9
import PackageDescription

let version = "1.0.0"
let releaseTag = "voxatrace-v1.0.0"
let checksum = "e5b17ea5af233c5526968b9e712e7059b1dea2c6b790c7f54f8d5140962aaec0"

let package = Package(
    name: "VoxaTrace",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "VoxaTrace", targets: ["VoxaTrace"]),
    ],
    targets: [
        .binaryTarget(
            name: "VoxaTrace",
            url: "https://github.com/musicmuni/voxatrace/releases/download/\(releaseTag)/voxatrace.xcframework.zip",
            checksum: checksum
        ),
    ]
)
