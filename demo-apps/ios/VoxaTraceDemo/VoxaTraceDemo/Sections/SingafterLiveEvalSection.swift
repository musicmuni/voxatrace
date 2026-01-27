import SwiftUI
import VoxaTrace

enum SingafterPhase: Int {
    case idle = 0
    case listening = 1
    case singing = 2
    case evaluated = 3
}

/// Model for singafter segment with type and related_seg from trans file
struct SingafterSegment: Decodable {
    let id: Int
    let type: String
    let lyrics: String
    let relatedSeg: Int
    let timeStamp: [Double]

    enum CodingKeys: String, CodingKey {
        case id, type, lyrics
        case relatedSeg = "related_seg"
        case timeStamp = "time_stamp"
    }

    var startTime: Double { timeStamp[0] }
    var endTime: Double { timeStamp[1] }
}

/// Phrase pair for singafter practice (teacher + student segments)
struct PhrasePair {
    let index: Int
    let lyrics: String
    let teacherStartTime: Double
    let teacherEndTime: Double
    let studentStartTime: Double
    let studentEndTime: Double
    let teacherId: Int  // For evaluator reference
}

/// Singafter Live Eval Section - for lessons like Chalan where teacher sings first, then student repeats.
///
/// Demonstrates:
/// - `CalibraLiveEval` for call-and-response singing evaluation
/// - `SonixRecorder` for audio capture
///
/// Flow:
/// 1. Load lesson assets (.trans with teacher/student pairs, .mp3)
/// 2. User selects a phrase pair
/// 3. Play teacher audio first
/// 4. When teacher segment ends, start recording student
/// 5. Capture user's pitch during student segment
/// 6. Evaluate and show score when student segment ends
struct SingafterLiveEvalSection: View {
    // Lesson state
    @State private var lessonLoaded = false
    @State private var phrasePairs: [PhrasePair] = []
    @State private var currentPairIndex = 0
    @State private var lessonName = "Chalan"

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
    @State private var practicePhase: SingafterPhase = .idle
    @State private var status = "Ready"
    @State private var feedbackMessage = ""

    // Timer for checking phase transitions
    @State private var timer: Timer?
    @State private var teacherEndTime: Double = 0
    @State private var studentEndTime: Double = 0

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Singafter Practice (Chalan)")
                .font(.headline)

            Text(status)
                .font(.caption)
                .foregroundColor(.secondary)

            if !lessonLoaded {
                Button("Load Lesson") {
                    loadLesson()
                }
                .buttonStyle(.borderedProminent)
            } else {
                // Phrase pair selector
                HStack {
                    Text("Phrase \(currentPairIndex + 1) of \(phrasePairs.count)")
                        .font(.subheadline)

                    Spacer()

                    HStack(spacing: 8) {
                        Button("<") {
                            if currentPairIndex > 0 {
                                currentPairIndex -= 1
                            }
                        }
                        .disabled(currentPairIndex == 0 || practicePhase != .idle)

                        Button(">") {
                            if currentPairIndex < phrasePairs.count - 1 {
                                currentPairIndex += 1
                            }
                        }
                        .disabled(currentPairIndex >= phrasePairs.count - 1 || practicePhase != .idle)
                    }
                    .buttonStyle(.bordered)
                }

                // Current phrase lyrics
                if !phrasePairs.isEmpty {
                    Text(phrasePairs[currentPairIndex].lyrics)
                        .font(.body)
                        .padding(12)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .background(Color.blue.opacity(0.1))
                        .cornerRadius(8)
                }

                // Phase indicator
                phaseIndicators

                // Practice controls
                HStack {
                    switch practicePhase {
                    case .idle, .evaluated:
                        Button("Start Practice") {
                            startPractice()
                        }
                        .buttonStyle(.borderedProminent)
                        .frame(maxWidth: .infinity)

                    case .listening, .singing:
                        Button("Stop") {
                            forceStop()
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(.red)
                        .frame(maxWidth: .infinity)
                    }
                }

                // Real-time feedback during singing phase
                if practicePhase == .singing {
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
                if practicePhase == .evaluated {
                    scoreCard
                }
            }
        }
        .onDisappear {
            cleanup()
        }
    }

    private var phaseIndicators: some View {
        HStack {
            Spacer()
            PhaseIndicatorView(
                label: "Listen",
                isActive: practicePhase == .listening,
                isComplete: practicePhase.rawValue > SingafterPhase.listening.rawValue
            )
            Text("\u{2192}")
            PhaseIndicatorView(
                label: "Sing",
                isActive: practicePhase == .singing,
                isComplete: practicePhase == .evaluated
            )
            Text("\u{2192}")
            PhaseIndicatorView(
                label: "Score",
                isActive: practicePhase == .evaluated,
                isComplete: false
            )
            Spacer()
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
        // Use YIN algorithm since it doesn't require a model provider (SWIFT_F0 requires model)
        pitchDetector = CalibraPitch.createDetector(algorithm: .yin, enableProcessing: true)

        // Create recorder using Sonix
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("singafter_temp.m4a").path
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
        setupAudioIfNeeded()
        status = "Loading lesson..."

        // Load and parse trans file from bundle
        guard let transURL = Bundle.main.url(forResource: lessonName, withExtension: "trans"),
              let transData = try? Data(contentsOf: transURL),
              let segments = try? JSONDecoder().decode([SingafterSegment].self, from: transData) else {
            status = "Failed to load lesson - trans file not found"
            return
        }

        // Group into phrase pairs (teacher followed by related student)
        let teacherSegments = segments.filter { $0.type == "teacher_vocal" }
        phrasePairs = teacherSegments.compactMap { teacher in
            guard let student = segments.first(where: { $0.id == teacher.relatedSeg && $0.type == "student_vocal" }) else {
                return nil
            }
            return PhrasePair(
                index: teacher.id,
                lyrics: teacher.lyrics,
                teacherStartTime: teacher.startTime,
                teacherEndTime: teacher.endTime,
                studentStartTime: student.startTime,
                studentEndTime: student.endTime,
                teacherId: teacher.id
            )
        }

        // Load audio file from bundle (m4a format)
        guard let audioURL = Bundle.main.url(forResource: lessonName, withExtension: "m4a") else {
            status = "Failed to load lesson - audio file not found"
            return
        }

        // Load player using Sonix (async)
        Task {
            do {
                player = try await SonixPlayer.create(source: audioURL.path)

                // Observe current time for phase transitions
                playerObserverTask = player?.observeCurrentTime { timeMs in
                    self.currentTimeMs = timeMs
                }
            } catch {
                await MainActor.run {
                    status = "Error loading audio: \(error)"
                }
                return
            }

            // Decode reference audio using Sonix and create session with new API
            if let audioData = SonixDecoder.decode(path: audioURL.path) {
                // Create segments from phrase pairs (using teacher timing for reference)
                let calibraSegments: [Segment] = phrasePairs.enumerated().map { (index, pair) in
                    Segment(
                        index: Int32(index),
                        startSeconds: Float(pair.teacherStartTime),
                        endSeconds: Float(pair.teacherEndTime),
                        lyrics: pair.lyrics
                    )
                }

                // Load pre-computed pitch contour if available (fast path)
                var pitchContour: PitchContour? = nil
                if let pitchURL = Bundle.main.url(forResource: lessonName, withExtension: "pitchPP"),
                   let pitchContent = try? String(contentsOf: pitchURL),
                   let pitchData = Parser.parsePitchString(content: pitchContent) {
                    pitchContour = PitchContour.fromArrays(
                        times: pitchData.times,
                        pitches: pitchData.pitchesHz
                    )
                    print("[DEBUG] Loaded .pitchPP with \(pitchData.count) samples")
                } else {
                    print("[DEBUG] No .pitchPP file found, will use slow path (audio extraction)")
                }

                // Create SingingReference - resampling handled internally
                let reference = SingingReference.fromAudio(
                    samples: audioData.samples,
                    sampleRate: audioData.sampleRate,
                    segments: calibraSegments,
                    keyHz: 196.0,  // G3, common for Indian classical
                    pitchContour: pitchContour
                )

                // Create session with config (manual phase control)
                let config = SessionConfig(
                    autoAdvance: false,
                    resultAggregation: .latest,
                    processingRate: 16000,
                    pitchTolerance: 0.15,
                    frameSize: 1024,
                    hopSize: 128,
                    studentKeyHz: 0,
                    yinMinPitch: -1,
                    yinMaxPitch: -1
                )
                session = CalibraLiveEval.create(reference: reference, config: config)

                // Prepare session (precomputes reference features on background thread)
                try await session?.prepare()
            } else {
                await MainActor.run {
                    status = "Failed to decode reference audio"
                }
                return
            }

            await MainActor.run {
                lessonLoaded = true
                currentPairIndex = 0
                status = "Lesson loaded: \(phrasePairs.count) phrase pairs"
            }
        }
    }

    private func startPractice() {
        guard lessonLoaded, !phrasePairs.isEmpty, let player = player else { return }

        let pair = phrasePairs[currentPairIndex]
        practicePhase = .listening
        segmentScore = 0
        feedbackMessage = ""
        status = "Listen to the teacher..."

        // Seek player to teacher segment start and play (convert seconds to milliseconds)
        let startMs = Int64(pair.teacherStartTime * 1000)
        teacherEndTime = pair.teacherEndTime
        studentEndTime = pair.studentEndTime
        player.seek(positionMs: startMs)
        player.play()
        isPlaying = true

        // Start timer to check for phase transitions (using observed currentTimeMs)
        timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { _ in
            let currentTimeSec = Double(currentTimeMs) / 1000.0

            if practicePhase == .listening && currentTimeSec >= teacherEndTime {
                // Transition to singing phase
                startSingingPhase()
            } else if practicePhase == .singing && currentTimeSec >= studentEndTime {
                // End practice
                endPractice()
            }
        }
    }

    private func startSingingPhase() {
        practicePhase = .singing
        status = "Your turn! Sing now..."

        // Begin segment using new API (use currentPairIndex as segment index)
        session?.beginSegment(index: Int32(currentPairIndex))

        // Start recording
        startRecording()
    }

    private func endPractice() {
        timer?.invalidate()
        timer = nil

        player?.pause()
        isPlaying = false

        stopRecording()

        // End segment and get result using new API
        if let result = session?.endSegmentEarly() {
            segmentScore = result.score
            feedbackMessage = result.feedbackMessage
        }

        practicePhase = .evaluated
        status = "Score: \(Int(segmentScore * 100))%"
    }

    private func forceStop() {
        timer?.invalidate()
        timer = nil

        player?.pause()
        isPlaying = false

        if practicePhase == .singing {
            stopRecording()
            session?.cancelSegment()
        }

        practicePhase = .idle
        status = "Stopped"
    }

    private func startRecording() {
        guard let recorder = recorder else { return }

        isRecording = true

        // Collect audio buffers from Sonix
        Task {
            let hwRate = Int(Sonix.hardwareSampleRate)

            for await buffer in recorder.audioBuffers {
                // Resample to 16kHz for Calibra (expects 16kHz input)
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                // Feed resampled audio to session
                session?.addAudio(samples: samples16k)

                // Use pitch detector for real-time display
                let result = pitchDetector?.detect(buffer: samples16k)
                let detectedPitch = result?.pitch ?? -1.0
                let calculatedRms = pitchDetector?.getAmplitude(buffer: samples16k) ?? 0.0

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

private struct PhaseIndicatorView: View {
    let label: String
    let isActive: Bool
    let isComplete: Bool

    var body: some View {
        VStack(spacing: 4) {
            ZStack {
                Circle()
                    .fill(backgroundColor)
                    .frame(width: 32, height: 32)
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
