import SwiftUI
import VoxaTrace

@main
struct VoxaTraceDemoApp: App {
    @State private var isInitialized = false
    @State private var licenseError: String? = nil

    init() {
        // ⚠️ DEMO ONLY - DO NOT USE IN PRODUCTION
        // This uses direct API key initialization which embeds credentials in the app.
        // For production apps, use one of these secure methods:
        //   - VT.initialize(proxyEndpoint:) - Recommended for apps with backend servers
        //   - VT.initializeWithAttestation(apiKey:) - For apps without backends (requires App Attest)
        // See docs/client-proxy-setup.md and docs/client-attestation-guide.md
        //
        // === AIModels Preload Examples ===
        //
        // Default: Just pitch model (most apps)
        // preload: AIModels.default
        //
        // Pitch + Speech VAD
        // preload: [AIModels.Pitch.realtime, AIModels.VAD.speech]
        //
        // All models (~17MB)
        // preload: AIModels.all
        //
        // No preload (fully lazy, download on first use)
        // preload: AIModels.none

        do {
            try VT.initializeForServer(
                apiKey: Config.apiKey,
                debugLogging: true,
                preload: AIModels.all
            )
            Log.i(.general, "VoxaTrace SDK initialized")
            _isInitialized = State(initialValue: true)
        } catch let error as VoxaTraceKilledException {
            let errorMessage = error.message ?? "License validation failed"
            Log.e(.general, "License validation failed: \(errorMessage)")
            _licenseError = State(initialValue: errorMessage)
        } catch {
            Log.e(.general, "Unexpected error: \(error)")
            _licenseError = State(initialValue: "Unexpected error: \(error.localizedDescription)")
        }
    }

    var body: some Scene {
        WindowGroup {
            if let error = licenseError {
                LicenseErrorView(message: error)
            } else if isInitialized {
                ContentView()
            } else {
                InitializingView()
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

/// View displayed while SDK is initializing
struct InitializingView: View {
    var body: some View {
        VStack(spacing: 16) {
            ProgressView()
                .scaleEffect(1.5)

            Text("Initializing VoxaTrace...")
                .font(.body)
                .foregroundColor(.secondary)
        }
    }
}
