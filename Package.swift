// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorCommunitySafeArea",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorCommunitySafeArea",
            targets: ["SafeAreaPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "SafeAreaPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/SafeAreaPlugin"),
        .testTarget(
            name: "SafeAreaPluginTests",
            dependencies: ["SafeAreaPlugin"],
            path: "ios/Tests/SafeAreaPluginTests")
    ]
)