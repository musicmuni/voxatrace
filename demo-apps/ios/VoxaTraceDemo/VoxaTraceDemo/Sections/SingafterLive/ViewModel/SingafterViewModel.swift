import Foundation
import Combine
import VoxaTrace

/// ViewModel for singafter live evaluation using CalibraLiveEval.
///
/// ## VoxaTrace Integration (~25 lines)
/// ```swift
/// // 1. Create session with singafter segments
/// let segments: [Segment] = phrasePairs.map { pair in
///     .create(index: index,
///             startSeconds: Float(pair.teacherStartTime),
///             endSeconds: Float(pair.studentEndTime),
///             lyrics: pair.lyrics,
///             studentStartSeconds: Float(pair.studentStartTime),
///             studentEndSeconds: Float(pair.studentEndTime))
/// }
/// session = CalibraLiveEval.create(reference: reference, session: .practice,
///                                   detector: detector, player: player, recorder: recorder)
///
/// // 2. Setup callbacks
/// session.onPhaseChanged { phase in }
/// session.onSegmentComplete { result in }
///
/// // 3. Start practice - handles LISTENING -> SINGING transition automatically
/// session.startPracticingSegment(index: currentPairIndex)
/// ```
@MainActor
final class SingafterViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var lessonLoaded = false
    @Published private(set) var phrasePairs: [PhrasePair] = []
    @Published private(set) var currentPairIndex = 0
    @Published private(set) var practicePhase: PracticePhase = .idle
    @Published private(set) var status = "Ready"
    @Published private(set) var segmentScore: Float = 0.0
    @Published private(set) var feedbackMessage = ""
    @Published private(set) var lastResult: SegmentResult?

    private let lessonName = "Chalan"

    // MARK: - Computed Properties

    var currentLyrics: String {
        guard !phrasePairs.isEmpty, currentPairIndex < phrasePairs.count else { return "" }
        return phrasePairs[currentPairIndex].lyrics
    }

    var canNavigatePrevious: Bool {
        currentPairIndex > 0 && practicePhase == .idle
    }

    var canNavigateNext: Bool {
        currentPairIndex < phrasePairs.count - 1 && practicePhase == .idle
    }

    // MARK: - Private

    private var player: SonixPlayer?
    private var recorder: SonixRecorder?
    private var session: CalibraLiveEval?

    // MARK: - Lifecycle

    func onDisappear() {
        cleanup()
    }

    // MARK: - Actions

    func loadLesson() {
        status = "Loading lesson..."

        // Load and parse trans file from bundle
        guard let transURL = Bundle.main.url(forResource: lessonName, withExtension: "trans"),
              let transData = try? Data(contentsOf: transURL),
              let segments = try? JSONDecoder().decode([SingafterSegment].self, from: transData) else {
            status = "Failed to load lesson - trans file not found"
            return
        }

        // Group into phrase pairs
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

        // Load audio file
        guard let audioURL = Bundle.main.url(forResource: lessonName, withExtension: "m4a") else {
            status = "Failed to load lesson - audio file not found"
            return
        }

        Task {
            do {
                player = try await SonixPlayer.create(source: audioURL.path, audioSession: .playAndRecord)

                let tempPath = FileManager.default.temporaryDirectory
                    .appendingPathComponent("singafter_temp.m4a").path
                let recorderConfig = SonixRecorderConfig.Builder().preset(.voice).echoCancellation(true).build()
                recorder = SonixRecorder.create(outputPath: tempPath, config: recorderConfig, audioSession: .playAndRecord)
            } catch {
                await MainActor.run {
                    status = "Error loading audio: \(error)"
                }
                return
            }

            // Create session with singafter segments
            if let audioData = SonixDecoder.decode(path: audioURL.path) {
                let calibraSegments: [Segment] = phrasePairs.enumerated().map { (index, pair) in
                    .create(
                        index: index,
                        startSeconds: Float(pair.teacherStartTime),
                        endSeconds: Float(pair.studentEndTime),
                        lyrics: pair.lyrics,
                        studentStartSeconds: Float(pair.studentStartTime),
                        studentEndSeconds: Float(pair.studentEndTime)
                    )
                }

                // Load pitch contour if available
                var pitchContour: PitchContour? = nil
                if let pitchURL = Bundle.main.url(forResource: lessonName, withExtension: "pitchPP"),
                   let pitchContent = try? String(contentsOf: pitchURL),
                   let pitchData = Parser.parsePitchString(content: pitchContent) {
                    pitchContour = PitchContour.fromArrays(
                        times: pitchData.times,
                        pitches: pitchData.pitchesHz
                    )
                }

                let reference = LessonMaterial.fromAudio(
                    samples: audioData.samples,
                    sampleRate: audioData.sampleRate,
                    segments: calibraSegments,
                    keyHz: 196.0,
                    pitchContour: pitchContour
                )

                let detector = CalibraPitch.createDetector(config: PitchDetectorConfig.balanced)

                session = CalibraLiveEval.create(
                    reference: reference,
                    session: .practice,
                    detector: detector,
                    player: player,
                    recorder: recorder
                )

                setupCallbacks()

                try? await session?.prepareSession()
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

    func navigatePrevious() {
        guard canNavigatePrevious else { return }
        currentPairIndex -= 1
    }

    func navigateNext() {
        guard canNavigateNext else { return }
        currentPairIndex += 1
    }

    func startPractice() {
        guard lessonLoaded else { return }

        segmentScore = 0
        feedbackMessage = ""

        session?.startPracticingSegment(index: currentPairIndex)
    }

    func forceStop() {
        session?.pausePlayback()
        practicePhase = .idle
        status = "Stopped"
    }

    // MARK: - Private Methods

    private func setupCallbacks() {
        guard let session = session else { return }

        session.onPhaseChanged { [weak self] phase in
            self?.practicePhase = phase

            switch phase {
            case .listening:
                self?.status = "Listen to the teacher..."
            case .singing:
                self?.status = "Your turn! Sing now..."
            case .evaluated:
                self?.status = "Score: \(Int(self?.segmentScore ?? 0 * 100))%"
            case .idle:
                self?.status = "Ready"
            default:
                break
            }
        }

        session.onReferenceEnd { segment in
            Log.d(.session, "Reference ended for segment \(segment.index)")
        }

        session.onSegmentComplete { [weak self] result in
            self?.lastResult = result
            self?.segmentScore = result.score
            self?.feedbackMessage = result.feedbackMessage
        }
    }

    private func cleanup() {
        player?.stop()
        player?.release()
        recorder?.stop()
        recorder?.release()
        session?.close()
    }
}
