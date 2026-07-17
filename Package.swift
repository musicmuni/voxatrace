// swift-tools-version:5.9
import PackageDescription

let version = "3.0.3"
let releaseTag = "voxatrace-v3.0.3"
let checksum = "687d82d61e2381643b79441101430823eb50fe6dcf703a08433b5b3cb94d6a01"

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
