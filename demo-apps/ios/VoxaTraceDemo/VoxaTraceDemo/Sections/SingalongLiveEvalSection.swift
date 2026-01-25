import SwiftUI
import VoxaTrace

enum PracticeState {
    case idle
    case practicing
    case evaluated
}

/// Singalong Live Eval Section - for lessons like Alankaar where user sings along with teacher.
///
/// Demonstrates:
/// - `CalibraLiveEval` for real-time singing evaluation
/// - `SonixRecorder` for audio capture
/// - `SonixPlayer` for reference audio playback
///
/// Flow:
/// 1. Load lesson assets (.trans, .mp3)
/// 2. User selects a segment
/// 3. Play reference audio while user sings along
/// 4. Capture user's pitch in real-time
/// 5. Evaluate and show score after segment ends
struct SingalongLiveEvalSection: View {
    // Lesson state
    @State private var lessonLoaded = false
    @State private var currentSegmentIndex = 0
    @State private var lessonName = "Alankaar 01"

    // Playback state (using SonixPlayer)
    @State private var player: SonixPlayer?
    @State private var isPlaying = false
    @State private var currentTimeMs: Int64 = 0
    @State private var playerObserverTask: Task<Void, Never>?

    // Recording state
    @State private var isRecording = false
    @State private var recordingLevel: Float = 0.0

    // Sonix recorder
    @State private var recorder: SonixRecorder?

    // Calibra new API - CalibraLiveEval
    @State private var session: CalibraLiveEval?
    @State private var pitchDetector: CalibraPitch.Detector?

    @State private var currentPitch: Float = 0.0
    @State private var currentNote = "-"

    // Evaluation state
    @State private var segmentScore: Float = 0.0
    @State private var practiceState: PracticeState = .idle
    @State private var status = "Ready"
    @State private var feedbackMessage = ""

    // Backend selection
    @State private var selectedBackend: LiveEvalBackend = .kotlin

    // Timer for checking segment end
    @State private var timer: Timer?
    @State private var segmentEndTime: Double = 0

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Singalong Practice (Alankaar)")
                .font(.headline)

            Text(status)
                .font(.caption)
                .foregroundColor(.secondary)

            if !lessonLoaded {
                // Backend selection before loading
                HStack {
                    Text("Backend:")
                        .font(.caption)
                    Picker("Backend", selection: $selectedBackend) {
                        Text("Kotlin").tag(LiveEvalBackend.kotlin)
                        Text("Native (C++)").tag(LiveEvalBackend.native)
                    }
                    .pickerStyle(.segmented)
                }

                Button("Load Lesson") {
                    loadLesson()
                }
                .buttonStyle(.borderedProminent)
            } else if let session = session {
                // Segment selector
                let segments = session.segments
                HStack {
                    Text("Segment \(currentSegmentIndex + 1) of \(segments.count)")
                        .font(.subheadline)

                    Spacer()

                    HStack(spacing: 8) {
                        Button("<") {
                            if currentSegmentIndex > 0 {
                                currentSegmentIndex -= 1
                            }
                        }
                        .disabled(currentSegmentIndex == 0 || practiceState == .practicing)

                        Button(">") {
                            if currentSegmentIndex < segments.count - 1 {
                                currentSegmentIndex += 1
                            }
                        }
                        .disabled(currentSegmentIndex >= segments.count - 1 || practiceState == .practicing)
                    }
                    .buttonStyle(.bordered)
                }

                // Current segment lyrics
                if !segments.isEmpty {
                    Text(segments[currentSegmentIndex].lyrics)
                        .font(.body)
                        .padding(12)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .background(Color.blue.opacity(0.1))
                        .cornerRadius(8)
                }

                // Practice controls
                HStack {
                    switch practiceState {
                    case .idle, .evaluated:
                        Button("Start Practice") {
                            startPractice()
                        }
                        .buttonStyle(.borderedProminent)
                        .frame(maxWidth: .infinity)

                    case .practicing:
                        Button("Stop") {
                            stopPractice()
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(.red)
                        .frame(maxWidth: .infinity)
                    }
                }

                // Real-time feedback during practice
                if practiceState == .practicing {
                    VStack(spacing: 8) {
                        HStack {
                            Text("Pitch: \(String(format: "%.1f", currentPitch)) Hz")
                            Spacer()
                            Text(currentNote)
                                .font(.title2)
                                .fontWeight(.bold)
                        }

                        HStack {
                            Text("Level:")
                                .font(.caption)
                            ProgressView(value: Double(min(max(recordingLevel, 0), 1)))
                        }
                    }
                }

                // Score display after evaluation
                if practiceState == .evaluated {
                    scoreCard
                }
            }
        }
        .onDisappear {
            cleanup()
        }
    }

    private var scoreCard: some View {
        let backgroundColor: Color = {
            if segmentScore >= 0.8 {
                return .green
            } else if segmentScore >= 0.6 {
                return .orange
            } else {
                return .red
            }
        }()

        return VStack(spacing: 8) {
            Text("\(Int(segmentScore * 100))%")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.white)
            Text(feedbackMessage)
                .foregroundColor(.white)
        }
        .frame(maxWidth: .infinity)
        .padding(16)
        .background(backgroundColor)
        .cornerRadius(12)
    }

    private func setupAudioIfNeeded() {
        guard pitchDetector == nil else { return }

        // Create pitch detector using Calibra public API (with processing for smoothing + octave correction)
        pitchDetector = CalibraPitch.createDetector(enableProcessing: true)

        // Create recorder using Sonix with echo cancellation enabled
        // This removes the lesson audio from the mic input so pitch detection works correctly
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("singalong_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice", echoCancellation: true)
    }

    private func cleanup() {
        timer?.invalidate()
        playerObserverTask?.cancel()
        player?.stop()
        player?.release()
        player = nil
        recorder?.stop()
        recorder?.release()
        recorder = nil

        // Clean up Calibra objects
        pitchDetector?.release()
        pitchDetector = nil
        session?.close()
        session = nil
    }

    private func loadLesson() {
        let totalStart = CFAbsoluteTimeGetCurrent()

        let setupStart = CFAbsoluteTimeGetCurrent()
        setupAudioIfNeeded()
        print("[TIMING] setupAudioIfNeeded: \(String(format: "%.3f", CFAbsoluteTimeGetCurrent() - setupStart))s")

        status = "Loading lesson..."

        // Load and parse trans file from bundle
        let parseStart = CFAbsoluteTimeGetCurrent()
        guard let transURL = Bundle.main.url(forResource: lessonName, withExtension: "trans"),
              let transContent = try? String(contentsOf: transURL),
              let transData = Parser.parseTransString(content: transContent) else {
            status = "Failed to load lesson - trans file not found"
            return
        }
        print("[TIMING] Parse .trans file: \(String(format: "%.3f", CFAbsoluteTimeGetCurrent() - parseStart))s")

        // Convert TransData segments to Calibra Segment model
        let segments: [Segment] = transData.segments.enumerated().map { (index, seg) in
            Segment(
                index: Int32(index),
                startSeconds: Float(seg.startTime),
                endSeconds: Float(seg.endTime),
                lyrics: seg.lyrics
            )
        }
        print("[TIMING] Segment count: \(segments.count)")

        // Load audio file from bundle (m4a format)
        guard let audioURL = Bundle.main.url(forResource: lessonName, withExtension: "m4a") else {
            status = "Failed to load lesson - audio file not found"
            return
        }

        // Load player using Sonix (async)
        Task {
            let playerStart = CFAbsoluteTimeGetCurrent()
            do {
                player = try await SonixPlayer.create(source: audioURL.path)
                print("[TIMING] SonixPlayer.create (includes decode #1): \(String(format: "%.3f", CFAbsoluteTimeGetCurrent() - playerStart))s")

                // Observe current time for segment end detection
                playerObserverTask = player?.observeCurrentTime { timeMs in
                    self.currentTimeMs = timeMs
                }
            } catch {
                await MainActor.run {
                    status = "Error loading audio: \(error)"
                }
                return
            }

            // Load pre-computed pitch contour if available (fast path)
            let pitchStart = CFAbsoluteTimeGetCurrent()
            var pitchContour: PitchContour? = nil
            if let pitchURL = Bundle.main.url(forResource: lessonName, withExtension: "pitchPP"),
               let pitchContent = try? String(contentsOf: pitchURL),
               let pitchData = Parser.parsePitchString(content: pitchContent) {
                // Convert PitchData (Sonix) to PitchContour (Calibra)
                pitchContour = PitchContour.fromArrays(
                    times: pitchData.times,
                    pitches: pitchData.pitchesHz
                )
                print("[TIMING] Load .pitchPP (\(pitchData.count) samples): \(String(format: "%.3f", CFAbsoluteTimeGetCurrent() - pitchStart))s")
            } else {
                print("[TIMING] No .pitchPP file found, will use slow path (YIN extraction)")
            }

            // Decode reference audio using Sonix and create session with new API
            // SonixDecoder.decode() resamples to 16kHz by default
            let decodeStart = CFAbsoluteTimeGetCurrent()
            if let audioData = SonixDecoder.decode(path: audioURL.path) {
                print("[TIMING] SonixDecoder.decode (decode #2 + resample to 16kHz): \(String(format: "%.3f", CFAbsoluteTimeGetCurrent() - decodeStart))s")
                print("[DEBUG] Decoded audio: \(audioData.samples.count) samples at \(audioData.sampleRate) Hz")

                // Create SingingReference with decoded audio and optional pitch contour
                let refStart = CFAbsoluteTimeGetCurrent()
                let reference = SingingReference.fromAudio(
                    samples: audioData.samples,
                    sampleRate: Int32(audioData.sampleRate),
                    segments: segments,
                    keyHz: 196.0,  // G3, common for Indian classical
                    pitchContour: pitchContour
                )
                print("[TIMING] SingingReference.fromAudio: \(String(format: "%.3f", CFAbsoluteTimeGetCurrent() - refStart))s")

                // Create session with config (manual segment control + selected backend)
                let sessionCreateStart = CFAbsoluteTimeGetCurrent()
                let config = SessionConfig(
                    autoAdvance: false,
                    resultAggregation: .latest,
                    processingRate: 16000,
                    pitchTolerance: 0.15,
                    frameSize: 1024,
                    hopSize: 128,
                    studentKeyHz: 0,
                    yinMinPitch: -1,
                    yinMaxPitch: -1,
                    backend: selectedBackend
                )
                session = CalibraLiveEval.create(reference: reference, config: config)
                print("[TIMING] CalibraLiveEval.create: \(String(format: "%.3f", CFAbsoluteTimeGetCurrent() - sessionCreateStart))s")

                // Prepare session (precomputes reference features on background thread)
                let prepareStart = CFAbsoluteTimeGetCurrent()
                try await session?.prepare()
                print("[TIMING] session.prepare (precompute ALL \(segments.count) segments): \(String(format: "%.3f", CFAbsoluteTimeGetCurrent() - prepareStart))s")
            } else {
                await MainActor.run {
                    status = "Failed to decode reference audio"
                }
                return
            }

            let totalTime = CFAbsoluteTimeGetCurrent() - totalStart
            print("[TIMING] ========================================")
            print("[TIMING] TOTAL loadLesson time: \(String(format: "%.3f", totalTime))s")
            print("[TIMING] ========================================")

            await MainActor.run {
                lessonLoaded = true
                currentSegmentIndex = 0
                status = "Lesson loaded: \(segments.count) segments"
            }
        }
    }

    private func startPractice() {
        guard lessonLoaded, let session = session, let player = player else { return }

        let segments = session.segments
        guard currentSegmentIndex < segments.count else { return }

        let segment = segments[currentSegmentIndex]
        practiceState = .practicing
        segmentScore = 0
        feedbackMessage = ""
        status = "Practicing segment \(currentSegmentIndex + 1)..."

        // Seek player to segment start and play (convert seconds to milliseconds)
        let startMs = Int64(segment.startSeconds * 1000)
        segmentEndTime = Double(segment.endSeconds)
        player.seek(positionMs: startMs)
        player.play()
        isPlaying = true

        // Begin segment using new CalibraLiveEval API
        session.beginSegment(index: Int32(currentSegmentIndex))

        // Start recording
        startRecording()

        // Start timer to check for segment end (using observed currentTimeMs)
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
        isPlaying = false

        stopRecording()

        // End segment early and get result using new API
        if let result = session?.endSegmentEarly() {
            segmentScore = result.score
            feedbackMessage = result.feedbackMessage
        }

        practiceState = .evaluated
        status = "Segment \(currentSegmentIndex + 1) score: \(Int(segmentScore * 100))%"
    }

    private func startRecording() {
        guard let recorder = recorder else { return }

        isRecording = true

        // Collect audio buffers from Sonix
        var frameCount = 0
        Task {
            let hwRate = Int(Sonix.hardwareSampleRate)

            for await buffer in recorder.audioBuffers {
                frameCount += 1

                // Resample to 16kHz for Calibra (expects 16kHz input)
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                // Feed resampled audio to session
                session?.addAudio(samples: samples16k)

                // Use pitch detector for real-time pitch display
                let result = pitchDetector?.detect(buffer: samples16k)
                let detectedPitch = result?.pitch ?? -1.0
                let calculatedRms = pitchDetector?.getAmplitude(buffer: samples16k) ?? 0.0

                // Log first few frames and every 50th frame
                if frameCount <= 3 || frameCount % 50 == 0 {
                    print("[DEBUG] Frame \(frameCount): \(samples16k.count) samples, pitch: \(detectedPitch) Hz")
                }

                await MainActor.run {
                    currentPitch = detectedPitch
                    recordingLevel = calculatedRms
                    currentNote = MusicUtils.getMidiNoteName(detectedPitch)
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false
    }
}
