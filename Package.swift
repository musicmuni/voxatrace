// swift-tools-version:5.9
import PackageDescription

let version = "0.9.0"
let releaseTag = "voxatrace-v0.9.0"
let checksum = "f974ce45786b1484b4305f3e48fcb2dd4993cafaefc7c5e6e0f588e4dbb744e5"

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
