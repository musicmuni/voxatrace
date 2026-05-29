// swift-tools-version:5.9
import PackageDescription

let version = "2.1.0"
let releaseTag = "voxatrace-v2.1.0"
let checksum = "b9124e567d2d515825c4d00e2257e82f0c020212747a73385601fa755d515ee7"

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
