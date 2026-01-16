# Calibra Sample Applications

This directory contains sample applications for Android and iOS demonstrating the features of the Calibra SDK.

## Android

The Android sample application is located in the `android/` directory.

### Building and Running

1.  Open the `calibra` project in Android Studio.
2.  Connect an Android device or start an emulator.
3.  Select the `app` run configuration.
4.  Click the "Run" button.

The application will build and install on your device.

## iOS

The iOS sample application is located in the `ios/` directory.

### Building and Running

1.  Open `ios/CalibraSample/CalibraSample.xcodeproj` in Xcode.
2.  Connect an iOS device or select a simulator.
3.  Select the `CalibraSample` scheme.
4.  Click the "Run" button.

The application will build and install on your device/simulator.

### Note on Dependencies

The iOS project depends on the `calibra.xcframework`, which is built from the KMP `library` module. The Xcode project should be configured to automatically build this framework as a dependency. If you encounter issues, you may need to manually build the framework first by running the appropriate Gradle task in the root `calibra` project.
