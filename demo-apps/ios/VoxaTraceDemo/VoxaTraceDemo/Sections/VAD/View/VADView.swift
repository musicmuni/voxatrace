import SwiftUI
import VoxaTrace

/// Voice Activity Detection section using Calibra public API.
///
/// Demonstrates:
/// - All VAD backends (SPEECH, GENERAL, SINGING_REALTIME)
/// - Real-time waveform visualization with VAD overlay
/// - Backend comparison with concurrent processing
/// - Sensitivity/threshold configuration
struct VADView: View {
    @State private var selectedDemo: Int = 0

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Voice Activity Detection")
                .font(.headline)

            Text("Detects speech/singing using multiple detection backends")
                .font(.caption)
                .foregroundColor(.secondary)

            Picker("Demo", selection: $selectedDemo) {
                Text("Live").tag(0)
                Text("Compare").tag(1)
            }
            .pickerStyle(.segmented)

            Divider()

            switch selectedDemo {
            case 0:
                LiveDetectionView()
            case 1:
                BackendComparisonView()
            default:
                EmptyView()
            }
        }
    }
}

/// Backward compatibility alias.
typealias VADSection = VADView
