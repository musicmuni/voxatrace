// swift-tools-version:5.9
import PackageDescription

let version = "1.0.1"
let releaseTag = "voxatrace-v1.0.1"
let checksum = "4774b460e1ee9826366621cbcc543310985e72b8a9c1ab47e5d105ffcaee6c6d"

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
