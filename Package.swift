// swift-tools-version:5.9
import PackageDescription

let version = "2.0.0"
let releaseTag = "voxatrace-v2.0.0"
let checksum = "1801702607beb2c3456dd1a2396e1e4c6513b20c72df862339fd1b26a7f37ec0"

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
