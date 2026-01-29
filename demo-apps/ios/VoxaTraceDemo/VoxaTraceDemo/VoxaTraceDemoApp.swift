import SwiftUI
import VoxaTrace

@main
struct VoxaTraceDemoApp: App {
    @State private var licenseError: String? = nil

    init() {
        // Initialize VoxaTrace SDK with API key
        // debugLogging: true enables console output in Xcode
        do {
            try VT.initialize(apiKey: Config.apiKey, debugLogging: true)
            Log.i(.general, "VoxaTrace SDK initialized")
        } catch let error as VoxaTraceKilledException {
            Log.e(.general, "License validation failed: \(error.message)")
            _licenseError = State(initialValue: error.message)
        } catch {
            Log.e(.general, "License validation failed", error: error)
            _licenseError = State(initialValue: "License validation failed: \(error.localizedDescription)")
        }

        // Configure ModelLoader for AI features (VAD, Pitch extraction)
        // Must be called before using ModelLoader.loadSileroVAD() or ModelLoader.loadSwiftF0()
        ModelLoader.configure()

        // Register AI models for auto-loading by CalibraPitch factories
        // After this, SwiftF0-based detectors/extractors work without explicit modelProvider
        AIModelRegistry.shared.registerSwiftF0 { ModelLoader.loadSwiftF0() }
    }

    var body: some Scene {
        WindowGroup {
            if let error = licenseError {
                LicenseErrorView(message: error)
            } else {
                ContentView()
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
