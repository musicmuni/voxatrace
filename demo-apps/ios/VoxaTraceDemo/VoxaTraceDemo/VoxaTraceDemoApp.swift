import SwiftUI
import VoxaTrace

@main
struct VoxaTraceDemoApp: App {
    @State private var isInitialized = false
    @State private var licenseError: String? = nil

    init() {
        // Initialize VoxaTrace SDK using App Attestation (App Attest on iOS 14+).
        // This verifies the app is running on a genuine device before granting access.
        //
        // ALTERNATIVE: If you have a backend server, use proxy-based initialization:
        // ```
        // do {
        //     try VT.initialize(
        //         proxyEndpoint: "https://your-server.com/api/voxatrace/register",
        //         debugLogging: true
        //     )
        //     // SDK ready immediately
        // } catch let error as VoxaTraceKilledException {
        //     // Handle license error
        // }
        // ```
        // See docs/client-proxy-setup.md for proxy server implementation.

        VT.initializeWithAttestation(
            apiKey: Config.apiKey,
            debugLogging: true
        ) { success, error in
            DispatchQueue.main.async {
                if success {
                    Log.i(.general, "VoxaTrace SDK initialized")
                    self.isInitialized = true
                } else {
                    let errorMessage = error ?? "License validation failed"
                    Log.e(.general, "License validation failed: \(errorMessage)")
                    self.licenseError = errorMessage
                }
            }
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
