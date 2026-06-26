// swift-tools-version:5.9
import PackageDescription

let version = "3.0.0"
let releaseTag = "voxatrace-v3.0.0"
let checksum = "3aaa9e4e8767bee12e25cb54620223bde0a3978a04d789e15c08ef033102a938"

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
