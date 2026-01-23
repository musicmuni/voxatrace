import SwiftUI
import VozOS

// Timing: capture when this file is first loaded
let appLoadTime = Date()

// Helper to print timestamps with high precision
func logTiming(_ message: String) {
    let elapsed = Date().timeIntervalSince(appLoadTime)
    print("[\(String(format: "%.3f", elapsed))s] \(message)")
}

@main
struct VozOSDemoApp: App {
    @State private var licenseError: String? = nil

    init() {
        logTiming("VozOSDemoApp.init() START")

        // Initialize VozOS logging first to see debug output in Xcode console
        VozOS.initializeLogging()

        // Initialize VozOS SDK with API key
        // This validates the API key synchronously and throws if invalid
        do {
            try VozOS.initialize(apiKey: Config.apiKey)
        } catch let error as VozOSKilledException {
            _licenseError = State(initialValue: error.message ?? "Invalid or revoked API key")
        } catch {
            _licenseError = State(initialValue: "License validation failed: \(error.localizedDescription)")
        }

        logTiming("VozOSDemoApp.init() END")
    }

    var body: some Scene {
        logTiming("VozOSDemoApp.body START")
        return WindowGroup {
            if let error = licenseError {
                LicenseErrorView(message: error)
            } else {
                ContentView()
                    .onAppear {
                        logTiming("ContentView.onAppear (UI is now visible)")
                    }
            }
        }
    }
}

/// View displayed when license validation fails
struct LicenseErrorView: View {
    let message: String

    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.triangle.fill")
                .font(.system(size: 60))
                .foregroundColor(.red)

            Text("License Error")
                .font(.title)
                .fontWeight(.semibold)

            Text(message)
                .font(.body)
                .multilineTextAlignment(.center)
                .foregroundColor(.secondary)

            Text("Contact support@musicmuni.com")
                .font(.caption)
                .foregroundColor(.secondary)
        }
        .padding(32)
    }
}
