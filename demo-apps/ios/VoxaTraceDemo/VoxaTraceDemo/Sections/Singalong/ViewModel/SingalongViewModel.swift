import Foundation
import VoxaTrace
import Combine

/// ViewModel for Singalong Practice.
///
/// Responsibilities:
/// - Manages CalibraLiveEval session lifecycle
/// - Transforms session state for UI consumption
/// - Exposes actions for user interactions
///
/// ## CalibraLiveEval Integration
///
/// The actual library integration is minimal (~30 lines):
/// ```swift
/// // 1. Create session
/// session = CalibraLiveEval.create(reference:session:detector:player:recorder:)
///
/// // 2. Setup callbacks
/// session.onPhaseChanged { }
/// session.onSegmentComplete { }
///
/// // 3. Control playback
/// session.startPracticingSegment(index:)
/// session.pausePlayback()
/// session.seekToSegment(index:)
/// ```
@MainActor
final class SingalongViewModel: ObservableObject {

    // MARK: - Dependencies

    private let repository: LessonRepositoryProtocol
    private let lessonName: String

    // MARK: - Published State

    @Published private(set) var uiState: SingalongUIState = .idle
    @Published private(set) var segments: [Segment] = []
    @Published private(set) var currentSegmentIndex: Int = 0
    @Published private(set) var completedSegmentIndices: Set<Int> = []
    @Published private(set) var completedResults: [Int: [SegmentResult]] = [:]
    @Published private(set) var practicePhase: PracticePhase = .idle
    @Published private(set) var currentPitch: Float = -1
    @Published private(set) var segmentProgress: Float = 0
    @Published private(set) var lastResult: SegmentResult?
    @Published var selectedPreset: SessionPreset = .practice

    // MARK: - Computed Properties

    var canGoPrevious: Bool { currentSegmentIndex > 0 }
    var canGoNext: Bool { currentSegmentIndex < segments.count - 1 }
    var canRetry: Bool { practicePhase != .singing }
    var isPracticing: Bool { practicePhase == .singing }
    var currentLyrics: String { segments[safe: currentSegmentIndex]?.lyrics ?? "" }

    // MARK: - Private

    private var session: CalibraLiveEval?
    private var player: SonixPlayer?
    private var recorder: SonixRecorder?
    private var observerTasks: [Task<Void, Never>] = []

    // MARK: - Init

    init(lessonName: String = "Alankaar 01", repository: LessonRepositoryProtocol = BundleLessonRepository()) {
        self.lessonName = lessonName
        self.repository = repository
    }

    // MARK: - Lifecycle

    func onAppear() async {
        await loadSession()
    }

    func onDisappear() {
        cleanup()
    }

    // MARK: - Actions

    func play() {
        session?.startPracticingSegment(index: currentSegmentIndex)
    }

    func pause() {
        session?.pausePlayback()
        practicePhase = .idle
    }

    func goToSegment(_ index: Int) {
        guard index >= 0, index < segments.count else { return }
        session?.seekToSegment(index: index)
    }

    func nextSegment() {
        guard canGoNext else { return }
        session?.startPracticingSegment(index: currentSegmentIndex + 1)
    }

    func previousSegment() {
        guard canGoPrevious else { return }
        session?.startPracticingSegment(index: currentSegmentIndex - 1)
    }

    func retry() {
        session?.retryCurrentSegment()
        play()
    }

    func finish() {
        session?.pausePlayback()
        lastResult = nil
        _ = session?.finishSession()
        uiState = .summary
    }

    func reset() {
        // Use session's restartSession() to properly clear all internal state
        session?.restartSession(fromSegment: 0)

        // Clear local UI state
        uiState = .ready
        completedResults = [:]
        completedSegmentIndices = []
        lastResult = nil
        practicePhase = .idle
    }

    func changePreset(_ preset: SessionPreset) async {
        guard preset != selectedPreset else { return }
        if practicePhase == .singing { pause() }
        selectedPreset = preset
        cleanup()
        await loadSession()
    }

    // MARK: - Session Management

    private func loadSession() async {
        uiState = .loading

        // 1. Load lesson data
        let lessonData: LessonData
        do {
            lessonData = try await repository.loadLesson(name: lessonName)
        } catch {
            uiState = .error(error.localizedDescription)
            return
        }

        // 2. Create audio I/O
        do {
            player = try await SonixPlayer.create(
                source: lessonData.audioPath,
                audioSession: .playAndRecord
            )
            let tempPath = FileManager.default.temporaryDirectory
                .appendingPathComponent("singalong_\(UUID().uuidString).m4a").path
            recorder = SonixRecorder.create(
                outputPath: tempPath,
                config: SonixRecorderConfig.Builder().preset(.voice).echoCancellation(true).build(),
                audioSession: .playAndRecord
            )
        } catch {
            uiState = .error("Audio setup failed: \(error.localizedDescription)")
            return
        }

        // 3. Create CalibraLiveEval session
        let detector = CalibraPitch.createDetector(config: .balanced)
        session = CalibraLiveEval.create(
            reference: lessonData.reference,
            session: selectedPreset.config,
            detector: detector,
            player: player,
            recorder: recorder
        )

        // 4. Setup callbacks & observers
        setupCallbacks()
        setupObservers()

        // 5. Prepare session
        try? await session?.prepareSession()

        // 6. Update UI
        segments = session?.segments ?? []
        uiState = .ready
    }

    private func setupCallbacks() {
        session?.onPhaseChanged { [weak self] phase in
            self?.practicePhase = phase
        }

        session?.onSegmentComplete { [weak self] result in
            self?.lastResult = result
        }

        session?.onSessionComplete { [weak self] _ in
            self?.uiState = .summary
        }
    }

    private func setupObservers() {
        guard let session = session else { return }

        let stateTask = session.observeState { [weak self] state in
            self?.currentSegmentIndex = Int(state.activeSegmentIndex ?? 0)
            self?.currentPitch = state.currentPitch
            self?.segmentProgress = state.segmentProgress
            self?.completedSegmentIndices = Set(state.completedSegments.map { Int($0) })
        }
        observerTasks.append(stateTask)

        let resultsTask = session.observeCompletedSegments { [weak self] results in
            self?.completedResults = results
        }
        observerTasks.append(resultsTask)
    }

    private func cleanup() {
        observerTasks.forEach { $0.cancel() }
        observerTasks.removeAll()
        player?.stop()
        player?.release()
        recorder?.stop()
        recorder?.release()
        session?.closeSession()
        session = nil
        player = nil
        recorder = nil
    }
}

// MARK: - Array Safe Subscript

private extension Array {
    subscript(safe index: Int) -> Element? {
        indices.contains(index) ? self[index] : nil
    }
}
