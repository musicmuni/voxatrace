import SwiftUI
import vozos

/// Vocal Range Detector using VocalRangeSession API.
///
/// Demonstrates:
/// - Direct `VocalRangeSession` API with `@State` primitives (iOS 15+ pattern)
/// - `SonixRecorder` for audio capture
/// - `MainActor.run` for thread-safe UI updates
struct VocalRangeSection: View {
    // Direct Calibra API + @State primitives (recommended iOS 15+ pattern)
    @State private var session: VocalRangeSession?
    @State private var recorder: SonixRecorder?
    @State private var audioTask: Task<Void, Never>?
    @State private var observerTask: Task<Void, Never>?

    // State primitives from VocalRangeState
    @State private var phase: VocalRangePhase = .idle
    @State private var countdownSeconds: Int32 = 0
    @State private var phaseMessage: String = "Ready to detect your vocal range"
    @State private var currentPitch: VocalPitch? = nil
    @State private var currentAmplitude: Float = 0
    @State private var stabilityProgress: Float = 0
    @State private var bestLowNote: DetectedNote? = nil
    @State private var bestHighNote: DetectedNote? = nil
    @State private var lowNote: DetectedNote? = nil
    @State private var highNote: DetectedNote? = nil
    @State private var result: VocalRangeResult? = nil
    @State private var error: String? = nil

    // Computed properties
    private var currentPitchLabel: String {
        currentPitch?.noteLabel ?? "-"
    }

    private var currentPitchHz: Float {
        currentPitch?.frequencyHz ?? 0
    }

    private var isDetecting: Bool {
        phase == .detectingLow || phase == .detectingHigh
    }

    private var hasError: Bool {
        error != nil
    }

    /// Best note for current detection phase
    private var bestNoteForPhase: DetectedNote? {
        switch phase {
        case .detectingLow: return bestLowNote
        case .detectingHigh: return bestHighNote
        default: return nil
        }
    }

    /// Whether a Lock button should be shown
    private var canLockNote: Bool {
        bestNoteForPhase != nil
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Vocal Range Detector")
                .font(.headline)

            Text(error ?? phaseMessage)
                .font(.subheadline)
                .foregroundColor(hasError ? .red : .secondary)

            // Phase indicators
            phaseIndicators

            // Countdown
            if phase == .countdown && countdownSeconds > 0 {
                Text("\(countdownSeconds)")
                    .font(.system(size: 72, weight: .bold))
                    .frame(maxWidth: .infinity)
            }

            // Real-time pitch display during detection
            if isDetecting {
                pitchDisplayCard

                // Best note so far card
                if let best = bestNoteForPhase {
                    bestNoteCard(note: best)
                }

                // Stability progress
                VStack(alignment: .leading, spacing: 4) {
                    Text("Stability: Hold for 1 second")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    ProgressView(value: Double(min(max(stabilityProgress, 0), 1)))
                        .tint(stabilityProgress >= 1.0 ? .green : .blue)
                }

                // Level meter
                HStack {
                    Text("Level:")
                        .font(.caption)
                    ProgressView(value: Double(min(max(currentAmplitude, 0), 1)))
                }
            }

            // Results
            if let result = result {
                resultsCard(result: result)
            }

            // Controls
            controlButtons
        }
        .onDisappear {
            stop()
            cleanup()
        }
    }

    /// Whether the flow is in a state where phases should show as complete
    private var isFlowActive: Bool {
        phase != .cancelled && phase != .idle
    }

    private var phaseIndicators: some View {
        HStack {
            Spacer()
            PhaseIndicator(
                label: "Ready",
                isActive: phase == .countdown,
                isComplete: isFlowActive && phase.ordinal > VocalRangePhase.countdown.ordinal
            )
            Text("→")
                .padding(.top, 8)
            PhaseIndicator(
                label: "Low",
                isActive: phase == .detectingLow,
                isComplete: isFlowActive && phase.ordinal > VocalRangePhase.detectingLow.ordinal
            )
            Text("→")
                .padding(.top, 8)
            PhaseIndicator(
                label: "High",
                isActive: phase == .detectingHigh,
                isComplete: phase == .complete
            )
            Text("→")
                .padding(.top, 8)
            PhaseIndicator(
                label: "Done",
                isActive: phase == .complete,
                isComplete: false
            )
            Spacer()
        }
    }

    private var pitchDisplayCard: some View {
        VStack(spacing: 4) {
            Text(currentPitchLabel)
                .font(.system(size: 48, weight: .bold))
            Text(String(format: "%.1f Hz", currentPitchHz))
                .font(.title3)
        }
        .frame(maxWidth: .infinity)
        .padding(16)
        .background(stabilityProgress >= 1.0 ? Color.green.opacity(0.3) : Color.blue.opacity(0.2))
        .cornerRadius(12)
    }

    private func bestNoteCard(note: DetectedNote) -> some View {
        let label = phase == .detectingLow ? "Lowest so far" : "Highest so far"
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
                    _ = session?.confirmNote()
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
            switch phase {
            case .idle:
                Button("Start Detection") {
                    start()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            case .complete, .cancelled:
                Button("Detect Again") {
                    reset()
                    start()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)

            default:
                Button("Cancel") {
                    stop()
                }
                .buttonStyle(.borderedProminent)
                .tint(.red)
                .frame(maxWidth: .infinity)
            }
        }
    }

    private func start() {
        // Create session with custom config (3-second countdown)
        let config = VocalRangeSessionConfig.custom(countdownSeconds: 3)
        let newSession = VocalRangeSession.create(config: config)
        session = newSession

        // Create recorder
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("range_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice")

        guard let recorder = recorder else { return }

        // Observe session state via AsyncSequence
        observerTask = Task {
            for await state in newSession.state {
                await MainActor.run {
                    self.phase = state.phase
                    self.countdownSeconds = state.countdownSeconds
                    self.phaseMessage = state.phaseMessage
                    self.currentPitch = state.currentPitch
                    self.currentAmplitude = state.currentAmplitude
                    self.stabilityProgress = state.stabilityProgress
                    self.bestLowNote = state.bestLowNote
                    self.bestHighNote = state.bestHighNote
                    self.lowNote = state.lowNote
                    self.highNote = state.highNote
                    self.result = state.result
                    self.error = state.error
                }
            }
        }

        // Start session (runs auto-flow)
        newSession.start()

        // Start recording
        recorder.start()

        // Feed audio to session - resample to 16kHz first
        audioTask = Task {
            let hwRate = Int(Sonix.hardwareSampleRate)
            for await buffer in recorder.audioBuffers {
                // Resample to 16kHz for Calibra (expects 16kHz input)
                let samples16k = SonixResampler.resample(
                    samples: buffer.floatSamples,
                    fromRate: hwRate,
                    toRate: 16000
                )
                newSession.addAudio(samples: samples16k)
            }
        }
    }

    private func stop() {
        audioTask?.cancel()
        audioTask = nil
        observerTask?.cancel()
        observerTask = nil
        recorder?.stop()
        session?.cancel()

        // Reset UI state that might persist after cancel
        countdownSeconds = 0
        phase = .cancelled
    }

    private func reset() {
        stop()
        // Reset state primitives
        phase = .idle
        countdownSeconds = 0
        phaseMessage = "Ready to detect your vocal range"
        currentPitch = nil
        currentAmplitude = 0
        stabilityProgress = 0
        bestLowNote = nil
        bestHighNote = nil
        lowNote = nil
        highNote = nil
        result = nil
        error = nil
    }

    private func cleanup() {
        stop()
        recorder?.release()
        recorder = nil
        session?.release()
        session = nil
    }
}

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
                    Text("✓")
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
