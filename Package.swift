// swift-tools-version:5.9
import PackageDescription

let version = "0.9.1"
let releaseTag = "voxatrace-v0.9.1"
let checksum = "7edd4c4c7112f7992141dc4c314ca80c125debdb254c4550a9c4c77625e1e327"

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
