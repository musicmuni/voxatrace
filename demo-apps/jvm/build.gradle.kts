plugins {
    kotlin("jvm") version "2.3.0"
    application
}

repositories {
    mavenLocal()   // checked first for pre-release (publishToMavenLocal) testing
    mavenCentral()
    google()
}

// Bump to the release version (e.g. "3.0.0") once published to Maven Central.
val voxatraceVersion = "3.0.0-SNAPSHOT"

// Native classifier for your platform. Switch to natives-linux-x64 on Linux.
val nativesClassifier = "natives-macos-arm64"
val aiNativesClassifier = "natives-ai-macos-arm64"

dependencies {
    // SDK classes (platform-agnostic).
    implementation("com.musicmuni:voxatrace-jvm:$voxatraceVersion")
    // Native libraries for this platform (required).
    runtimeOnly("com.musicmuni:voxatrace-jvm:$voxatraceVersion:$nativesClassifier")
    // AI-backed pitch/VAD (optional; needed for PitchAlgorithm.SWIFT_F0).
    runtimeOnly("com.musicmuni:voxatrace-jvm:$voxatraceVersion:$aiNativesClassifier")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
}

application {
    mainClass.set("com.musicmuni.voxatrace.demo.MainKt")
}

kotlin {
    jvmToolchain(17)
}
