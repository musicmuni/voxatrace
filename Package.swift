// swift-tools-version:5.9
import PackageDescription

let version = "2025.129.1000"
let checksum = "CHECKSUM_HERE"
let bundleChecksum = "BUNDLE_CHECKSUM_HERE"

let package = Package(
    name: "VoxaTrace",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "VoxaTrace", targets: ["VoxaTrace", "VoxaTraceResources"]),
    ],
    targets: [
        .binaryTarget(
            name: "VoxaTrace",
            url: "https://github.com/musicmuni/voxatrace/releases/download/\(version)/VoxaTrace-\(version).xcframework.zip",
            checksum: checksum
        ),
        .binaryTarget(
            name: "VoxaTraceResources",
            url: "https://github.com/musicmuni/voxatrace/releases/download/\(version)/voxatrace-\(version).bundle.zip",
            checksum: bundleChecksum
        ),
    ]
)
