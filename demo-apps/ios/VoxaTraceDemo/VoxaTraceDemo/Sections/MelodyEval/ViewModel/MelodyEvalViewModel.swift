import Foundation
import Combine
import VoxaTrace

/// ViewModel for melody evaluation using CalibraMelodyEval with singalong mode.
///
/// ## VoxaTrace Integration
/// ```swift
/// // 1. Load reference (first 4 segments of Alankaar 01)
/// let transData = SonixParser.parseTransString(content: transContent)
/// let audioData = SonixDecoder.decode(path: audioURL.path)
/// reference = LessonMaterial.fromAudio(samples:, sampleRate:, segments:, keyHz:)
///
/// // 2. Singalong: Play reference + record student simultaneously
/// player = SonixPlayer.create(source: referencePath, audioSession: .playAndRecord)
/// recorder = SonixRecorder.create(outputPath: path, config: .voice, audioSession: .playAndRecord)
///
/// // 3. Evaluate
/// let extractor = CalibraPitch.createContourExtractor()
/// let result = CalibraMelodyEval.evaluate(reference: reference, student: studentMaterial, contourExtractor: extractor)
/// ```
@MainActor
final class MelodyEvalViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var referenceLoaded = false
    @Published private(set) var segments: [Segment] = []
    @Published private(set) var isSingalongActive = false
    @Published private(set) var hasRecording = false
    @Published private(set) var recordingDuration: Float = 0.0
    @Published private(set) var recordingLevel: Float = 0.0
    @Published private(set) var isEvaluating = false
    @Published private(set) var result: SingingResult?
    @Published private(set) var status = "Tap 'Load Reference' to begin"
    @Published private(set) var isPreparing = false
    @Published private(set) var isReady = false
    @Published private(set) var currentSegmentIndex: Int = -1

    let referenceName = "Alankaar 01"

    /// Display names for the 4 segments
    let segmentNames = ["SRGM", "PDNS", "SNDP", "MGRS"]

    // MARK: - Private

    private var reference: LessonMaterial?
    private var recorder: SonixRecorder?
    private var collectedAudio: [Float] = []

    // Singalong
    private var referencePlayer: SonixPlayer?
    private var playerObserverTask: Task<Void, Never>?
    private var timeObserverTask: Task<Void, Never>?
    private var audioFilePath: String?
    private var segmentStartTimeMs: Int64 = 0
    private var segmentEndTimeMs: Int64 = 0
    private var originalSampleRate: Int = 44100
    private var currentPlayerTimeMs: Int64 = 0

    // Original segment times (for player position tracking)
    private var originalSegmentTimes: [(start: Double, end: Double)] = []

    // MARK: - Lifecycle

    func onDisappear() {
        cleanup()
    }

    // MARK: - Actions

    func loadReference() {
        status = "Loading reference..."
        isPreparing = true

        guard let transURL = Bundle.main.url(forResource: referenceName, withExtension: "trans"),
              let transContent = try? String(contentsOf: transURL),
              let transData = SonixParser.parseTransString(content: transContent) else {
            status = "Failed to load trans file"
            isPreparing = false
            return
        }

        // Use first 4 segments: SRGM, PDNS, SNDP, MGRS
        let segmentCount = min(4, transData.segments.count)

        // Store original segment times for player position tracking
        originalSegmentTimes = (0..<segmentCount).map { i in
            let seg = transData.segments[i]
            return (start: seg.startTime, end: seg.endTime)
        }

        segments = (0..<segmentCount).map { i in
            let seg = transData.segments[i]
            return .create(
                index: i,
                startSeconds: seg.startTime,
                endSeconds: seg.endTime,
                lyrics: seg.lyrics
            )
        }

        // Calculate the time range for the 4 segments
        let startTime = originalSegmentTimes.first?.start ?? 0
        let endTime = originalSegmentTimes.last?.end ?? 0
        segmentStartTimeMs = Int64(startTime * 1000)
        segmentEndTimeMs = Int64(endTime * 1000)

        guard let audioURL = Bundle.main.url(forResource: referenceName, withExtension: "m4a") else {
            status = "Failed to load audio file"
            isPreparing = false
            return
        }

        guard let audioData = SonixDecoder.decode(path: audioURL.path) else {
            status = "Failed to decode audio"
            isPreparing = false
            return
        }

        originalSampleRate = audioData.sampleRate

        // Trim audio for evaluation (we need trimmed audio for CalibraMelodyEval)
        let startSample = max(0, Int(startTime * Double(audioData.sampleRate)))
        let endSample = min(audioData.samples.count, Int(endTime * Double(audioData.sampleRate)))
        let trimmedSamples = Array(audioData.samples[startSample..<endSample])

        // Create adjusted segments for the trimmed reference (times relative to start of trimmed audio)
        // Note: Use max(0, ...) to handle floating-point precision issues when subtracting
        let adjustedSegments = segments.map { seg in
            Segment.create(
                index: seg.index,
                startSeconds: max(0, seg.startSeconds - startTime),
                endSeconds: max(0, seg.endSeconds - startTime),
                lyrics: seg.lyrics
            )
        }

        reference = LessonMaterial.fromAudio(
            samples: trimmedSamples,
            sampleRate: audioData.sampleRate,
            segments: adjustedSegments,
            keyHz: 196.0
        )

        // Copy audio file to temp directory for playback
        audioFilePath = copyAssetToFile(name: referenceName, ext: "m4a")

        // Load player for singalong
        Task {
            await loadPlayerForSingalong()
        }

        referenceLoaded = true
    }

    private func loadPlayerForSingalong() async {
        guard let path = audioFilePath else {
            await MainActor.run {
                status = "Audio file not found"
                isPreparing = false
            }
            return
        }

        playerObserverTask?.cancel()
        timeObserverTask?.cancel()
        referencePlayer?.release()

        do {
            let player = try await SonixPlayer.create(source: path, audioSession: .playAndRecord)
            referencePlayer = player

            // Auto-stop when playback finishes OR when we reach the end of our segments
            playerObserverTask = player.observeIsPlaying { [weak self] playing in
                guard let self = self else { return }
                if !playing && self.isSingalongActive {
                    self.stopSingalong()
                }
            }

            // Monitor time to stop at segment end and track current segment
            timeObserverTask = player.observeCurrentTime { [weak self] timeMs in
                guard let self = self else { return }
                self.currentPlayerTimeMs = timeMs

                if self.isSingalongActive {
                    // Stop at segment end
                    if timeMs >= self.segmentEndTimeMs {
                        self.stopSingalong()
                    } else {
                        // Update current segment indicator
                        let currentTimeSec = Double(timeMs) / 1000.0
                        self.updateCurrentSegment(currentTimeSec: currentTimeSec)
                    }
                }
            }

            await MainActor.run {
                isReady = true
                isPreparing = false
                status = "Ready! Tap 'Start Singalong' to sing along."
            }
        } catch {
            await MainActor.run {
                status = "Player error: \(error.localizedDescription)"
                isPreparing = false
            }
        }
    }

    func startSingalong() {
        guard isReady else {
            status = "Reference not ready"
            return
        }

        collectedAudio = []
        hasRecording = false
        result = nil
        recordingDuration = 0.0
        currentSegmentIndex = 0
        status = "Sing along: \(segmentNames[0])"

        // Create recorder for simultaneous playback+recording with echo cancellation
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("melody_eval_temp.m4a").path
        let recorderConfig = SonixRecorderConfig.Builder()
            .preset(.voice)
            .echoCancellation(true)
            .build()
        recorder = SonixRecorder.create(outputPath: tempPath, config: recorderConfig, audioSession: .playAndRecord)

        isSingalongActive = true

        // Start collecting audio
        Task {
            let hwRate = AudioSessionManager.hardwareSampleRate
            var sampleCount = 0

            guard let recorder = recorder else { return }

            for await buffer in recorder.audioBuffers {
                let samples16k = SonixResampler.resample(
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                collectedAudio.append(contentsOf: samples16k)
                sampleCount += samples16k.count

                let sum = samples16k.reduce(0) { $0 + $1 * $1 }
                let rms = sqrt(sum / Float(samples16k.count))

                await MainActor.run {
                    recordingLevel = min(rms * 5, 1.0)
                    recordingDuration = Float(sampleCount) / 16000.0
                }
            }
        }

        // Seek to the start of the first segment and play
        referencePlayer?.seek(positionMs: segmentStartTimeMs)

        // Start recorder and player together
        recorder?.start()
        referencePlayer?.play()
    }

    private func updateCurrentSegment(currentTimeSec: Double) {
        // Use original segment times for tracking since we play from original audio file
        for (index, times) in originalSegmentTimes.enumerated() {
            if currentTimeSec >= times.start && currentTimeSec < times.end {
                if currentSegmentIndex != index {
                    currentSegmentIndex = index
                    status = "Sing along: \(segmentNames[index])"
                }
                return
            }
        }
    }

    func stopSingalong() {
        recorder?.stop()
        referencePlayer?.stop()
        isSingalongActive = false
        currentSegmentIndex = -1

        if !collectedAudio.isEmpty {
            hasRecording = true
            status = "Recording complete. Evaluating..."
            evaluate()
        } else {
            status = "No audio recorded. Try again."
        }
    }

    func evaluate() {
        guard let reference = reference, !collectedAudio.isEmpty else {
            status = "Missing reference or recording"
            return
        }

        isEvaluating = true
        status = "Evaluating..."

        Task {
            let studentAudio = collectedAudio

            let studentMaterial = LessonMaterial.fromAudio(
                samples: studentAudio,
                sampleRate: 16000,
                segments: [],
                keyHz: reference.keyHz
            )

            let extractor = CalibraPitch.createContourExtractor()

            let evalResult = CalibraMelodyEval.evaluate(
                reference: reference,
                student: studentMaterial,
                contourExtractor: extractor
            )

            extractor.release()

            await MainActor.run {
                result = evalResult
                isEvaluating = false
                status = "Evaluation complete: \(Int(evalResult.overallScore * 100))%"
            }
        }
    }

    /// Get the score for a specific segment index
    func segmentScore(at index: Int) -> Float? {
        result?.latestScore(forSegment: index)
    }

    // MARK: - Private Methods

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        playerObserverTask?.cancel()
        timeObserverTask?.cancel()
        referencePlayer?.release()
        referencePlayer = nil
    }
}
