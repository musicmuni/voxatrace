import SwiftUI

/// Pitch detection and processing demos using Calibra public API.
///
/// Demonstrates:
/// - Real-time pitch detection with tiered builder API (YIN + SwiftF0)
/// - Batch pitch extraction with ContourCleanup presets
/// - Cleanup comparison (RAW vs SCORING vs DISPLAY)
/// - PitchPoint computed properties explorer
struct PitchView: View {
    @State private var selectedDemo: Int = 0

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Pitch")
                .font(.headline)

            Picker("Demo", selection: $selectedDemo) {
                Text("Realtime").tag(0)
                Text("Extraction").tag(1)
                Text("Cleanup").tag(2)
                Text("PitchPoint").tag(3)
            }
            .pickerStyle(.segmented)

            Divider()

            switch selectedDemo {
            case 0:
                RealtimePitchView()
            case 1:
                PitchExtractionView()
            case 2:
                ContourCleanupView()
            case 3:
                PitchPointExplorerView()
            default:
                EmptyView()
            }
        }
    }
}

/// Backward compatibility alias.
typealias PitchSection = PitchView
