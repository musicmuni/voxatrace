// swift-tools-version:5.9
import PackageDescription

let version = "3.0.1"
let releaseTag = "voxatrace-v3.0.1"
let checksum = "0bb6735471806321a1e33320a59d42ed158536516f8710f71bf3b764a340202c"

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
