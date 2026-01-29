import Foundation
import Combine
import VoxaTrace

/// ViewModel for Singafter Practice.
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
/// // 1. Create session with singafter segments (includes studentStartSeconds/studentEndSeconds)
/// session = CalibraLiveEval.create(reference:session:detector:player:recorder:)
///
/// // 2. Setup callbacks
/// session.onPhaseChanged { }      // Handles LISTENING -> SINGING transition
/// session.onSegmentComplete { }
///
/// // 3. Control playback
/// session.startPracticingSegment(index:)
/// session.pausePlayback()
/// ```
@MainActor
final class SingafterViewModel: ObservableObject {

    // MARK: - Dependencies

    private let repository: SingafterRepositoryProtocol
    private let lessonName: String

    // MARK: - Published State

    @Published private(set) var uiState: SingafterUIState = .idle
    @Published private(set) var phrasePairs: [PhrasePair] = []
    @Published private(set) var currentPairIndex: Int = 0
    @Published private(set) var completedPairIndices: Set<Int> = []
    @Published private(set) var completedResults: [Int: [SegmentResult]] = [:]
    @Published private(set) var practicePhase: PracticePhase = .idle
    @Published private(set) var currentPitch: Float = -1
    @Published private(set) var segmentProgress: Float = 0
    @Published private(set) var lastResult: SegmentResult?
    @Published var selectedPreset: SessionPreset = .practice

    // MARK: - Computed Properties

    var canGoPrevious: Bool { currentPairIndex > 0 }
    var canGoNext: Bool { currentPairIndex < phrasePairs.count - 1 }
    var canRetry: Bool { practicePhase != .singing && practicePhase != .listening }
    var isPracticing: Bool { practicePhase == .singing || practicePhase == .listening }
    var currentLyrics: String { phrasePairs[safe: currentPairIndex]?.lyrics ?? "" }

    /// Status message based on current practice phase.
    var statusMessage: String {
        switch practicePhase {
        case .listening: return "Listen to the teacher..."
        case .singing: return "Your turn! Sing now..."
        case .evaluated: return "Score: \(Int((lastResult?.score ?? 0) * 100))%"
        case .idle: return "Ready"
        default: return "Ready"
        }
    }

    // MARK: - Private

    private var session: CalibraLiveEval?
    private var player: SonixPlayer?
    private var recorder: SonixRecorder?
    private var observerTasks: [Task<Void, Never>] = []

    // MARK: - Init

    init(lessonName: String = "Chalan", repository: SingafterRepositoryProtocol = BundleSingafterRepository()) {
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
        session?.startPracticingSegment(index: currentPairIndex)
    }

    func pause() {
        session?.pausePlayback()
        practicePhase = .idle
    }

    func goToPair(_ index: Int) {
        guard index >= 0, index < phrasePairs.count else { return }
        session?.seekToSegment(index: index)
    }

    func nextPair() {
        guard canGoNext else { return }
        session?.startPracticingSegment(index: currentPairIndex + 1)
    }

    func previousPair() {
        guard canGoPrevious else { return }
        session?.startPracticingSegment(index: currentPairIndex - 1)
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
        session?.restartSession(fromSegment: 0)
        uiState = .ready
        completedResults = [:]
        completedPairIndices = []
        lastResult = nil
        practicePhase = .idle
    }

    func changePreset(_ preset: SessionPreset) async {
        guard preset != selectedPreset else { return }
        if isPracticing { pause() }
        selectedPreset = preset
        cleanup()
        await loadSession()
    }

    // MARK: - Session Management

    private func loadSession() async {
        uiState = .loading

        // 1. Load lesson data
        let lessonData: SingafterLessonData
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
                .appendingPathComponent("singafter_\(UUID().uuidString).m4a").path
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
        phrasePairs = lessonData.phrasePairs
        uiState = .ready
    }

    private func setupCallbacks() {
        session?.onPhaseChanged { [weak self] phase in
            self?.practicePhase = phase
        }

        session?.onReferenceEnd { segment in
            Log.d(.session, "Reference ended for segment \(segment.index)")
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
            self?.currentPairIndex = Int(state.activeSegmentIndex ?? 0)
            self?.currentPitch = state.currentPitch
            self?.segmentProgress = state.segmentProgress
            self?.completedPairIndices = Set(state.completedSegments.map { Int($0) })
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
