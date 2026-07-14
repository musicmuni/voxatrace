// swift-tools-version:5.9
import PackageDescription

let version = "3.0.2"
let releaseTag = "voxatrace-v3.0.2"
let checksum = "64364406f3985ba64c6009b4d278f517b3f4836de5755d2586586d25a7d80832"

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
