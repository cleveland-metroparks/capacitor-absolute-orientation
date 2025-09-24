// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorAbsoluteOrientation",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorAbsoluteOrientation",
            targets: ["AbsoluteOrientationPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "AbsoluteOrientationPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/AbsoluteOrientationPlugin"),
        .testTarget(
            name: "AbsoluteOrientationPluginTests",
            dependencies: ["AbsoluteOrientationPlugin"],
            path: "ios/Tests/AbsoluteOrientationPluginTests")
    ]
)