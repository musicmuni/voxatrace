import SwiftUI
import VoxaTrace

/// Vocal range detection view demonstrating VocalRangeSession API.
struct VocalRangeView: View {
    @StateObject private var viewModel = VocalRangeViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Vocal Range Detector")
                .font(.headline)

            Text(viewModel.error ?? viewModel.phaseMessage)
                .font(.subheadline)
                .foregroundColor(viewModel.hasError ? .red : .secondary)

            // Phase indicators
            phaseIndicators

            // Countdown
            if viewModel.phase == .countdown && viewModel.countdownSeconds > 0 {
                Text("\(viewModel.countdownSeconds)")
                    .font(.system(size: 72, weight: .bold))
                    .frame(maxWidth: .infinity)
            }

            // Real-time pitch display during detection
            if viewModel.isDetecting {
                pitchDisplayCard

                // Best note so far card
                if let best = viewModel.bestNoteForPhase {
                    bestNoteCard(note: best)
                }

                // Stability progress
                VStack(alignment: .leading, spacing: 4) {
                    Text("Stability: Hold for 1 second")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    ProgressView(value: Double(min(max(viewModel.stabilityProgress, 0), 1)))
                        .tint(viewModel.stabilityProgress >= 1.0 ? .green : .blue)
                }

                // Level meter
                HStack {
                    Text("Level:")
                        .font(.caption)
                    ProgressView(value: Double(min(max(viewModel.currentAmplitude, 0), 1)))
                }
            }

            // Results
            if let result = viewModel.result {
                resultsCard(result: result)
            }

            // Controls
            controlButtons
        }
        .onDisappear {
            viewModel.onDisappear()
        }
    }

    private var phaseIndicators: some View {
        HStack {
            Spacer()
            PhaseIndicator(
                label: "Ready",
                isActive: viewModel.phase == .countdown,
                isComplete: viewModel.isFlowActive && viewModel.phase.ordinal > VocalRangePhase.countdown.ordinal
            )
            Text("\u{2192}")
                .padding(.top, 8)
            PhaseIndicator(
                label: "Low",
                isActive: viewModel.phase == .detectingLow,
                isComplete: viewModel.isFlowActive && viewModel.phase.ordinal > VocalRangePhase.detectingLow.ordinal
            )
            Text("\u{2192}")
                .padding(.top, 8)
            PhaseIndicator(
                label: "High",
                isActive: viewModel.phase == .detectingHigh,
                isComplete: viewModel.phase == .complete
            )
            Text("\u{2192}")
                .padding(.top, 8)
            PhaseIndicator(
                label: "Done",
                isActive: viewModel.phase == .complete,
                isComplete: false
            )
            Spacer()
        }
    }

    private var pitchDisplayCard: some View {
        VStack(spacing: 4) {
            Text(viewModel.currentPitchLabel)
                .font(.system(size: 48, weight: .bold))
            Text(String(format: "%.1f Hz", viewModel.currentPitchHz))
                .font(.title3)
        }
        .frame(maxWidth: .infinity)
        .padding(16)
        .background(viewModel.stabilityProgress >= 1.0 ? Color.green.opacity(0.3) : Color.blue.opacity(0.2))
        .cornerRadius(12)
    }

    private func bestNoteCard(note: DetectedNote) -> some View {
        let label = viewModel.phase == .detectingLow ? "Lowest so far" : "Highest so far"
        return VStack(spacing: 8) {
            HStack {
                VStack(alignment: .leading, spacing: 2) {
                    Text(label)
                        .font(.caption)
                        .foregroundColor(.white.opacity(0.8))
                    HStack(alignment: .firstTextBaseline, spacing: 4) {
                        Text(note.pitch.noteLabel)
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                        Text(String(format: "%.1f Hz", note.pitch.frequencyHz))
                            .font(.caption)
                            .foregroundColor(.white.opacity(0.7))
                    }
                }
                Spacer()
                Button("Lock & Continue") {
                    viewModel.confirmNote()
                }
                .buttonStyle(.bordered)
                .tint(.white)
            }
        }
        .padding(12)
        .background(Color.orange)
        .cornerRadius(10)
    }

    private func resultsCard(result: VocalRangeResult) -> some View {
        VStack(spacing: 12) {
            HStack {
                VStack(alignment: .leading) {
                    Text("Low")
                        .font(.caption)
                        .foregroundColor(.white.opacity(0.7))
                    Text(result.low.noteLabel)
                        .font(.title)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                    Text(String(format: "%.1f Hz", result.low.frequencyHz))
                        .font(.caption)
                        .foregroundColor(.white.opacity(0.7))
                }

                Spacer()

                VStack {
                    Text("Range")
                        .font(.caption)
                        .foregroundColor(.white.opacity(0.7))
                    Text(String(format: "%.1f oct", result.octaves))
                        .font(.title)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                }

                Spacer()

                VStack(alignment: .trailing) {
                    Text("High")
                        .font(.caption)
                        .foregroundColor(.white.opacity(0.7))
                    Text(result.high.noteLabel)
                        .font(.title)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                    Text(String(format: "%.1f Hz", result.high.frequencyHz))
                        .font(.caption)
                        .foregroundColor(.white.opacity(0.7))
                }
            }

            Divider()
                .background(Color.white.opacity(0.3))

            HStack {
                Text("Natural Shruti:")
                    .foregroundColor(.white.opacity(0.7))
                Text(result.naturalShruti.noteLabel)
                    .font(.title2)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
            }
        }
        .padding(16)
        .background(Color.green)
        .cornerRadius(12)
    }

    private var controlButtons: some View {
        HStack {
            switch viewModel.phase {
            case .idle:
                Button("Start Detection") {
                    viewModel.start()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            case .complete, .cancelled:
                Button("Detect Again") {
                    viewModel.reset()
                    viewModel.start()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            default:
                Button("Cancel") {
                    viewModel.stop()
                }
                .buttonStyle(.borderedProminent)
                .tint(.red)
                .frame(maxWidth: .infinity)
            }
        }
    }
}

// MARK: - Phase Indicator

private struct PhaseIndicator: View {
    let label: String
    let isActive: Bool
    let isComplete: Bool

    var body: some View {
        VStack(spacing: 4) {
            ZStack {
                Circle()
                    .fill(backgroundColor)
                    .frame(width: 28, height: 28)
                if isComplete {
                    Text("\u{2713}")
                        .font(.caption)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                }
            }
            Text(label)
                .font(.caption)
                .foregroundColor(isActive ? .blue : .secondary)
        }
    }

    private var backgroundColor: Color {
        if isActive {
            return .blue
        } else if isComplete {
            return .green
        } else {
            return Color(.secondarySystemBackground)
        }
    }
}

/// Backward compatibility alias.
typealias VocalRangeSection = VocalRangeView

#Preview {
    VocalRangeView()
        .padding()
}
