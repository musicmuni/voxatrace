// swift-tools-version:5.9
import PackageDescription

let version = "0.9.2"
let releaseTag = "voxatrace-v0.9.2"
let checksum = "918d50fcd39c38d47dc0db81009125a5669b076830d56f4ba1e952cebdbc21f2"

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
