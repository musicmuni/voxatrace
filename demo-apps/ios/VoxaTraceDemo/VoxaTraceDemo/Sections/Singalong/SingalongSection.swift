import SwiftUI
import VoxaTrace

/// Singalong Practice Demo - showcases CalibraLiveEval session management.
///
/// Demonstrates:
/// - Session lifecycle (prepare, practice, finish)
/// - Segment navigation and seeking
/// - Session presets (Default, Practice, Karaoke, Performance)
/// - Pitch contour visualization from SegmentResult
/// - Session summary with aggregation modes
struct SingalongSection: View {
    // CalibraLiveEval session
    @State private var session: CalibraLiveEval?

    // Audio I/O
    @State private var player: SonixPlayer?
    @State private var recorder: SonixRecorder?
    @State private var playerObserverTask: Task<Void, Never>?
    @State private var currentTimeMs: Int = 0

    // Observed state from CalibraLiveEval
    @State private var sessionState: SessionState = .idle
    @State private var completedResults: [Int: [SegmentResult]] = [:]

    // Observer tasks
    @State private var stateTask: Task<Void, Never>?
    @State private var resultsTask: Task<Void, Never>?

    // Segment timing
    @State private var segmentEndTime: Double = 0
    @State private var timer: Timer?

    // UI state
    @State private var selectedPreset: SessionPreset = .practice
    @State private var isLoading = false
    @State private var loadError: String? = nil
    @State private var showingSummary = false
    @State private var selectedAggregation: ResultAggregation = .latest
    @State private var lastCompletedIndex: Int? = nil
    @State private var isSessionFinished = false  // Prevents audio loop from pending callbacks

    private let lessonName = "Alankaar 01"

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Header with preset picker
            HStack {
                Text("Singalong Practice")
                    .font(.headline)
                Spacer()
                presetPicker
            }

            // Loading state
            if isLoading {
                HStack {
                    ProgressView()
                        .scaleEffect(0.8)
                    Text("Loading lesson...")
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
            }

            // Error state
            if let error = loadError {
                HStack {
                    Image(systemName: "exclamationmark.triangle.fill")
                        .foregroundColor(.orange)
                    Text(error)
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
            }

            // Main content
            if session != nil && !showingSummary {
                practiceView
            }

            // Summary
            if showingSummary {
                SingalongSummaryView(
                    segments: session?.segments ?? [],
                    completedResults: completedResults,
                    selectedAggregation: $selectedAggregation,
                    onPracticeAgain: resetSession
                )
            }
        }
        .task {
            await loadLesson()
        }
        .onDisappear {
            cleanup()
        }
    }

    // MARK: - Preset Picker

    private var presetPicker: some View {
        Menu {
            ForEach(SessionPreset.allCases) { preset in
                Button {
                    changePreset(to: preset)
                } label: {
                    VStack(alignment: .leading) {
                        Text(preset.rawValue)
                        Text(preset.description)
                            .font(.caption)
                    }
                }
            }
        } label: {
            HStack(spacing: 4) {
                Text(selectedPreset.rawValue)
                    .font(.subheadline)
                Image(systemName: "chevron.down")
                    .font(.caption2)
            }
            .padding(.horizontal, 10)
            .padding(.vertical, 6)
            .background(Color(.tertiarySystemBackground))
            .cornerRadius(8)
        }
    }

    // MARK: - Practice View

    private var practiceView: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Progress bar
            progressBar

            Divider()

            // Current segment
            currentSegmentView

            // Last segment result (inline)
            lastSegmentResultView

            // Practice button
            practiceButton

            Divider()

            // Controls
            controlsView

            // Session buttons
            sessionButtons
        }
    }

    private var progressBar: some View {
        let segments = session?.segments ?? []
        let currentIndex = sessionState.activeSegmentIndex ?? 0
        let completedIndices = sessionState.completedSegments
        let isManualMode = !selectedPreset.isAutoAdvance

        return VStack(spacing: 4) {
            HStack(spacing: 4) {
                ForEach(0..<segments.count, id: \.self) { index in
                    SegmentChip(
                        index: index,
                        isActive: index == currentIndex,
                        isCompleted: completedIndices.contains(index),
                        onTap: { seekToSegment(index) }
                    )
                    .disabled(sessionState.isPracticing || !isManualMode)
                }
            }
            Text("Segment \(currentIndex + 1) of \(segments.count)")
                .font(.caption)
                .foregroundColor(.secondary)
        }
    }

    private var currentSegmentView: some View {
        let segments = session?.segments ?? []
        let currentIndex = sessionState.activeSegmentIndex ?? 0

        return Group {
            if !segments.isEmpty && currentIndex < segments.count {
                let segment = segments[currentIndex]

                Text(segment.lyrics)
                    .font(.body)
                    .padding(12)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(Color.blue.opacity(0.1))
                    .cornerRadius(8)

                if sessionState.isPracticing {
                    VStack(spacing: 8) {
                        HStack {
                            Text("Pitch: \(String(format: "%.1f", sessionState.currentPitch)) Hz")
                            Spacer()
                            Text(MusicUtils.getMidiNoteName(sessionState.currentPitch))
                                .font(.title2)
                                .fontWeight(.bold)
                        }
                        ProgressView(value: Double(sessionState.segmentProgress))
                    }
                    .padding(8)
                    .background(Color(.secondarySystemBackground))
                    .cornerRadius(8)
                }
            }
        }
    }

    @ViewBuilder
    private var lastSegmentResultView: some View {
        // Show result regardless of whether currently practicing next segment
        // This allows the result to stay visible during auto-advance
        if let index = lastCompletedIndex,
           let results = completedResults[index],
           let result = results.last {
            VStack(spacing: 8) {
                // Score badge
                HStack {
                    Text("Segment \(index + 1)")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                    Spacer()
                    Text("\(Int(result.score * 100))%")
                        .font(.title3)
                        .fontWeight(.bold)
                        .foregroundColor(scoreColor(result.score))
                    Text(result.feedbackMessage)
                        .font(.caption)
                        .foregroundColor(.secondary)
                }

                // Pitch graph
                let refData = result.referencePitchData
                let stdData = result.studentPitchData

                // Debug logging for pitch data
                let _ = print("📊 Inline Segment \(index) REF: times[\(refData.times.count)] = \(refData.times)")
                let _ = print("📊 Inline Segment \(index) REF: midi[\(refData.pitchesMidi.count)] = \(refData.pitchesMidi)")
                let _ = print("📊 Inline Segment \(index) STD: times[\(stdData.times.count)] = \(stdData.times)")
                let _ = print("📊 Inline Segment \(index) STD: midi[\(stdData.pitchesMidi.count)] = \(stdData.pitchesMidi)")

                if !refData.pitchesMidi.isEmpty || !stdData.pitchesMidi.isEmpty {
                    PitchGraphView(
                        contoursWithTimes: [
                            (times: refData.times, pitches: refData.pitchesMidi, color: .blue, label: "Reference"),
                            (times: stdData.times, pitches: stdData.pitchesMidi, color: .orange, label: "You")
                        ],
                        title: nil,
                        height: 120,
                        inputIsMidi: true
                    )
                }
            }
            .padding()
            .background(Color(.secondarySystemBackground))
            .cornerRadius(12)
        }
    }

    private func scoreColor(_ score: Float) -> Color {
        if score >= 0.8 { return .green }
        if score >= 0.6 { return .orange }
        return .red
    }

    private var practiceButton: some View {
        HStack {
            if sessionState.isPracticing {
                Button("Stop") {
                    stopPractice()
                }
                .buttonStyle(.borderedProminent)
                .tint(.red)
                .frame(maxWidth: .infinity)
            } else {
                Button("Start Practice") {
                    startPractice()
                }
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)
            }
        }
    }

    private var controlsView: some View {
        let isManualMode = !selectedPreset.isAutoAdvance

        return VStack(spacing: 12) {
            HStack(spacing: 12) {
                Button(action: { prevSegment() }) {
                    Label("Prev", systemImage: "chevron.left")
                }
                .disabled(!isManualMode || sessionState.isPracticing || (sessionState.activeSegmentIndex ?? 0) == 0)

                Button(action: { retrySegment() }) {
                    Label("Retry", systemImage: "arrow.counterclockwise")
                }
                .disabled(!isManualMode || sessionState.isPracticing)

                Button(action: { nextSegment() }) {
                    Label("Next", systemImage: "chevron.right")
                }
                .disabled(!isManualMode || sessionState.isPracticing || (sessionState.activeSegmentIndex ?? 0) >= (session?.segments.count ?? 1) - 1)
            }
            .buttonStyle(.bordered)

            if !isManualMode {
                Text("Navigation disabled in auto-advance mode")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
        }
    }

    private var sessionButtons: some View {
        HStack(spacing: 12) {
            Button("Start Over") {
                resetSession()
            }
            .buttonStyle(.bordered)
            .frame(maxWidth: .infinity)

            Button("Finish") {
                finishSession()
            }
            .buttonStyle(.borderedProminent)
            .frame(maxWidth: .infinity)
        }
    }

    // MARK: - Actions

    private func changePreset(to preset: SessionPreset) {
        guard preset != selectedPreset else { return }

        // If practicing, stop first
        if sessionState.isPracticing {
            stopPractice()
        }

        selectedPreset = preset

        // Reload session with new config
        Task {
            // Close existing session
            session?.close()
            stateTask?.cancel()
            resultsTask?.cancel()
            session = nil

            // Clear state
            await MainActor.run {
                completedResults = [:]
                lastCompletedIndex = nil
                sessionState = .idle
            }

            // Reload with new preset
            await loadLesson()
        }
    }

    private func loadLesson() async {
        isLoading = true
        loadError = nil

        guard let transURL = Bundle.main.url(forResource: lessonName, withExtension: "trans"),
              let transContent = try? String(contentsOf: transURL),
              let transData = Parser.parseTransString(content: transContent) else {
            await MainActor.run {
                isLoading = false
                loadError = "Failed to load lesson - trans file not found"
            }
            return
        }

        let segments: [Segment] = transData.segments.enumerated().map { (index, seg) in
            .create(
                index: index,
                startSeconds: seg.startTime,
                endSeconds: seg.endTime,
                lyrics: seg.lyrics
            )
        }

        guard let audioURL = Bundle.main.url(forResource: lessonName, withExtension: "m4a") else {
            await MainActor.run {
                isLoading = false
                loadError = "Failed to load lesson - audio file not found"
            }
            return
        }

        do {
            player = try await SonixPlayer.create(source: audioURL.path)
            playerObserverTask = player?.observeCurrentTime { timeMs in
                self.currentTimeMs = Int(timeMs)
            }
        } catch {
            await MainActor.run {
                isLoading = false
                loadError = "Error loading audio: \(error.localizedDescription)"
            }
            return
        }

        // Setup recorder
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("singalong_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice", echoCancellation: true)

        // Load pitch contour if available
        var pitchContour: PitchContour? = nil
        if let pitchURL = Bundle.main.url(forResource: lessonName, withExtension: "pitchPP"),
           let pitchContent = try? String(contentsOf: pitchURL),
           let pitchData = Parser.parsePitchString(content: pitchContent) {
            pitchContour = PitchContour.fromArrays(times: pitchData.times, pitches: pitchData.pitchesHz)
        }

        // Create session
        if let audioData = SonixDecoder.decode(path: audioURL.path) {
            let reference = SingingReference.fromAudio(
                samples: audioData.samples,
                sampleRate: audioData.sampleRate,
                segments: segments,
                keyHz: 196.0,
                pitchContour: pitchContour
            )

            // Create detector with YIN algorithm (no model required)
            let detector = CalibraPitch.createDetector(
                config: PitchDetectorConfig.balanced,
                modelProvider: nil
            )

            let config = selectedPreset.config
            session = CalibraLiveEval.create(reference: reference, session: config, detector: detector)
            try? await session?.prepare()

            // Setup observers
            setupObservers()

            await MainActor.run {
                isLoading = false
            }
        } else {
            await MainActor.run {
                isLoading = false
                loadError = "Failed to decode audio"
            }
        }
    }

    private func setupObservers() {
        guard let session = session else { return }

        stateTask = session.observeState { state in
            self.sessionState = state
        }

        resultsTask = session.observeCompletedSegments { results in
            self.completedResults = results
        }
    }

    private func startPractice() {
        // Block pending callbacks after session is finished
        guard !isSessionFinished else { return }
        guard let session = session, let player = player else { return }

        let index = sessionState.activeSegmentIndex ?? 0
        guard index < session.segments.count else { return }

        let segment = session.segments[index]
        segmentEndTime = Double(segment.endSeconds)

        player.seek(positionMs: Int(segment.startSeconds * 1000))
        player.play()
        session.beginSegment(index: index)

        startRecording()

        timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { _ in
            let currentTimeSec = Double(currentTimeMs) / 1000.0
            if currentTimeSec >= segmentEndTime {
                stopPractice()
            }
        }
    }

    private func stopPractice() {
        timer?.invalidate()
        timer = nil
        player?.pause()
        recorder?.stop()

        let index = sessionState.activeSegmentIndex ?? 0
        lastCompletedIndex = index

        _ = session?.endSegmentEarly()

        // Skip auto-advance if session is finished (prevents audio loop)
        guard !isSessionFinished else { return }

        // Auto-advance if enabled
        if selectedPreset.isAutoAdvance {
            let count = session?.segments.count ?? 0
            if index < count - 1 {
                DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                    seekToSegment(index + 1)
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
                        startPractice()
                    }
                }
            } else {
                // Last segment - finish session
                DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                    finishSession()
                }
            }
        }
    }

    private func startRecording() {
        guard let recorder = recorder else { return }

        Task {
            let hwRate = Sonix.hardwareSampleRateInt
            for await buffer in recorder.audioBuffers {
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )
                session?.addAudio(samples: samples16k)
            }
        }

        recorder.start()
    }

    private func seekToSegment(_ index: Int) {
        session?.beginSegment(index: index)
    }

    private func prevSegment() {
        let current = sessionState.activeSegmentIndex ?? 0
        if current > 0 {
            seekToSegment(current - 1)
        }
    }

    private func nextSegment() {
        let current = sessionState.activeSegmentIndex ?? 0
        let count = session?.segments.count ?? 0
        if current < count - 1 {
            seekToSegment(current + 1)
        }
    }

    private func retrySegment() {
        session?.retrySegment()
        startPractice()
    }

    private func finishSession() {
        // Set flag FIRST to block any pending callbacks from auto-advance
        isSessionFinished = true

        // Always stop playback and recording regardless of current state
        timer?.invalidate()
        timer = nil
        player?.pause()
        recorder?.stop()

        // Finish segment if practicing
        if sessionState.isPracticing {
            _ = session?.endSegmentEarly()
        }

        // Clear inline result (summary will show all results)
        lastCompletedIndex = nil

        _ = session?.finish()
        showingSummary = true
    }

    private func resetSession() {
        isSessionFinished = false  // Reset flag to allow new practice
        showingSummary = false
        completedResults = [:]
        lastCompletedIndex = nil
        session?.beginSegment(index: 0)
    }

    private func cleanup() {
        timer?.invalidate()
        stateTask?.cancel()
        resultsTask?.cancel()
        playerObserverTask?.cancel()
        player?.stop()
        player?.release()
        recorder?.stop()
        recorder?.release()
        session?.close()
    }
}

// MARK: - Session Preset

enum SessionPreset: String, CaseIterable, Identifiable {
    case `default` = "Default"
    case practice = "Practice"
    case karaoke = "Karaoke"
    case performance = "Performance"

    var id: String { rawValue }

    var config: SessionConfig {
        switch self {
        case .default: return .default
        case .practice: return .practice
        case .karaoke: return .karaoke
        case .performance: return .performance
        }
    }

    var description: String {
        switch self {
        case .default: return "Balanced, auto-advancing"
        case .practice: return "70% threshold, 3 attempts, best score"
        case .karaoke: return "Relaxed, always advances"
        case .performance: return "Precise, one attempt"
        }
    }

    var isAutoAdvance: Bool { config.autoAdvance }
}

// MARK: - Segment Chip

private struct SegmentChip: View {
    let index: Int
    let isActive: Bool
    let isCompleted: Bool
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            ZStack {
                Circle()
                    .fill(backgroundColor)
                    .frame(width: 28, height: 28)

                if isCompleted {
                    Image(systemName: "checkmark")
                        .font(.caption2)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                } else {
                    Text("\(index + 1)")
                        .font(.caption2)
                        .fontWeight(isActive ? .bold : .regular)
                        .foregroundColor(isActive ? .white : .primary)
                }
            }
        }
        .buttonStyle(.plain)
        .overlay(
            Circle()
                .stroke(isActive ? Color.blue : Color.clear, lineWidth: 2)
                .frame(width: 32, height: 32)
        )
    }

    private var backgroundColor: Color {
        if isCompleted { return .green }
        if isActive { return .blue }
        return Color(.tertiarySystemBackground)
    }
}
