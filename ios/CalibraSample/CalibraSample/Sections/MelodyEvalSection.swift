import SwiftUI
import vozos

/// Melody Eval Section - for offline melody/song evaluation.
///
/// Demonstrates:
/// - `CalibraMelodyEval` for offline melody evaluation
/// - `SonixRecorder` for audio capture
/// - `SonixDecoder` for loading reference audio
/// - `PitchContour` for pitch extraction
///
/// Flow:
/// 1. Load reference audio file (.m4a)
/// 2. Record student performance
/// 3. Evaluate recorded audio against reference
/// 4. Display overall score and per-segment breakdown
struct MelodyEvalSection: View {
    // Reference state
    @State private var referenceLoaded = false
    @State private var referenceName = "Alankaar 01"
    @State private var segments: [Segment] = []

    // Recording state
    @State private var isRecording = false
    @State private var hasRecording = false
    @State private var recordingDuration: Float = 0.0
    @State private var recordingLevel: Float = 0.0

    // Evaluation state
    @State private var isEvaluating = false
    @State private var result: SingingResult?
    @State private var status = "Ready"

    // Backend selection
    @State private var selectedBackend: CalibraMelodyEval.Backend = .kotlin

    // Resources
    @State private var reference: SingingReference?
    @State private var recorder: SonixRecorder?
    @State private var collectedAudio: [KotlinFloatArray] = []

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Offline Melody Evaluation")
                .font(.headline)

            Text(status)
                .font(.caption)
                .foregroundColor(.secondary)

            // Step 1: Load reference
            if !referenceLoaded {
                loadReferenceSection
            } else {
                // Show reference info
                referenceInfoSection

                // Step 2: Record performance
                recordingSection

                // Step 3: Evaluate
                if hasRecording && !isRecording {
                    evaluateSection
                }

                // Step 4: Results
                if let result = result {
                    resultsSection(result)
                }
            }
        }
        .onDisappear {
            cleanup()
        }
    }

    // MARK: - Subviews

    private var loadReferenceSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 1: Load Reference")
                .font(.subheadline)
                .fontWeight(.medium)

            Button("Load \(referenceName)") {
                loadReference()
            }
            .buttonStyle(.borderedProminent)
        }
    }

    private var referenceInfoSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Image(systemName: "checkmark.circle.fill")
                    .foregroundColor(.green)
                Text("Reference: \(referenceName)")
                    .font(.subheadline)
            }

            Text("\(segments.count) segments loaded")
                .font(.caption)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color.green.opacity(0.1))
        .cornerRadius(8)
    }

    private var recordingSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 2: Record Your Performance")
                .font(.subheadline)
                .fontWeight(.medium)

            HStack {
                if isRecording {
                    Button("Stop Recording") {
                        stopRecording()
                    }
                    .buttonStyle(.borderedProminent)
                    .tint(.red)

                    Spacer()

                    // Recording indicator
                    HStack(spacing: 4) {
                        Circle()
                            .fill(Color.red)
                            .frame(width: 10, height: 10)
                        Text(MusicUtils.formatTime(recordingDuration))
                            .font(.caption)
                            .monospacedDigit()
                    }
                } else {
                    Button(hasRecording ? "Re-record" : "Start Recording") {
                        startRecording()
                    }
                    .buttonStyle(.borderedProminent)

                    if hasRecording {
                        Spacer()
                        Image(systemName: "checkmark.circle.fill")
                            .foregroundColor(.green)
                        Text("Recording ready")
                            .font(.caption)
                    }
                }
            }

            if isRecording {
                HStack {
                    Text("Level:")
                        .font(.caption)
                    ProgressView(value: Double(min(max(recordingLevel, 0), 1)))
                }
            }
        }
    }

    private var evaluateSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 3: Evaluate")
                .font(.subheadline)
                .fontWeight(.medium)

            // Backend selection
            HStack {
                Text("Backend:")
                    .font(.caption)
                Picker("Backend", selection: $selectedBackend) {
                    Text("Kotlin").tag(CalibraMelodyEval.Backend.kotlin)
                    Text("Native (C++)").tag(CalibraMelodyEval.Backend.native)
                }
                .pickerStyle(.segmented)
            }

            Button(isEvaluating ? "Evaluating..." : "Evaluate Performance") {
                evaluate()
            }
            .buttonStyle(.borderedProminent)
            .disabled(isEvaluating)
        }
    }

    private func resultsSection(_ result: SingingResult) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Results")
                .font(.subheadline)
                .fontWeight(.medium)

            // Overall score
            overallScoreCard(result)

            // Per-segment breakdown
            if !result.segmentResults.isEmpty {
                Text("Per-Segment Scores")
                    .font(.caption)
                    .foregroundColor(.secondary)

                ForEach(result.sortedSegmentResults, id: \.index) { entry in
                    if let segmentResult = entry.attempts.first {
                        segmentRow(index: entry.index, result: segmentResult)
                    }
                }
            }
        }
    }

    private func overallScoreCard(_ result: SingingResult) -> some View {
        let backgroundColor: Color = {
            if result.overallScore >= 0.8 {
                return .green
            } else if result.overallScore >= 0.6 {
                return .orange
            } else {
                return .red
            }
        }()

        return VStack(spacing: 4) {
            Text("Overall Score")
                .font(.caption)
                .foregroundColor(.white.opacity(0.8))
            Text("\(Int(result.overallScore * 100))%")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.white)
        }
        .frame(maxWidth: .infinity)
        .padding(16)
        .background(backgroundColor)
        .cornerRadius(12)
    }

    private func segmentRow(index: Int, result: SegmentResult) -> some View {
        let backgroundColor: Color = {
            if result.score >= 0.8 {
                return .green.opacity(0.2)
            } else if result.score >= 0.6 {
                return .orange.opacity(0.2)
            } else {
                return .red.opacity(0.2)
            }
        }()

        return HStack {
            Text("Segment \(index + 1)")
                .font(.caption)
            if !result.segment.lyrics.isEmpty {
                Text("(\(result.segment.lyrics))")
                    .font(.caption)
                    .foregroundColor(.secondary)
                    .lineLimit(1)
            }
            Spacer()
            Text("\(Int(result.score * 100))%")
                .font(.caption)
                .fontWeight(.medium)
        }
        .padding(8)
        .background(backgroundColor)
        .cornerRadius(6)
    }

    // MARK: - Actions

    private func loadReference() {
        status = "Loading reference..."

        // Load and parse trans file from bundle
        guard let transURL = Bundle.main.url(forResource: referenceName, withExtension: "trans"),
              let transContent = try? String(contentsOf: transURL),
              let transData = Parser.parseTransString(content: transContent) else {
            status = "Failed to load trans file"
            return
        }

        // Convert to Calibra Segment model - use only first segment for easier testing
        let firstSeg = transData.segments[0]
        segments = [
            Segment(
                index: Int32(0),
                startSeconds: Float(firstSeg.startTime),
                endSeconds: Float(firstSeg.endTime),
                lyrics: firstSeg.lyrics
            )
        ]

        // Load audio file from bundle
        guard let audioURL = Bundle.main.url(forResource: referenceName, withExtension: "m4a") else {
            status = "Failed to load audio file"
            return
        }

        // Decode reference audio using SonixDecoder
        guard let audioData = SonixDecoder.decode(path: audioURL.path) else {
            status = "Failed to decode audio"
            return
        }

        // Create SingingReference
        reference = SingingReference.fromAudio(
            samples: audioData.floatSamples,
            sampleRate: Int32(audioData.sampleRate),
            segments: segments,
            keyHz: 196.0  // G3 - common for Indian classical
        )

        referenceLoaded = true
        status = "Reference loaded. Record your performance."
    }

    private func startRecording() {
        // Reset state
        collectedAudio = []
        hasRecording = false
        result = nil
        recordingDuration = 0.0
        status = "Recording..."

        // Create recorder
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("melody_eval_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, format: "m4a", quality: "voice")

        isRecording = true

        // Collect audio buffers
        Task {
            let hwRate = Int(Sonix.hardwareSampleRate)
            var sampleCount = 0

            guard let recorder = recorder else { return }

            for await buffer in recorder.audioBuffers {
                // Resample to 16kHz for Calibra
                let samples16k = SonixResampler.resample(
                    samples: buffer.floatSamples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                collectedAudio.append(samples16k)
                sampleCount += Int(samples16k.size)

                // Calculate RMS for level meter
                var sum: Float = 0
                for i in 0..<samples16k.size {
                    let sample = samples16k.get(index: i)
                    sum += sample * sample
                }
                let rms = sqrt(sum / Float(samples16k.size))

                await MainActor.run {
                    recordingLevel = min(rms * 5, 1.0)  // Scale for visibility
                    recordingDuration = Float(sampleCount) / 16000.0
                }
            }
        }

        recorder?.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false

        if !collectedAudio.isEmpty {
            hasRecording = true
            status = "Recording complete. Ready to evaluate."
        } else {
            status = "No audio recorded. Try again."
        }
    }

    private func evaluate() {
        guard let reference = reference, !collectedAudio.isEmpty else {
            status = "Missing reference or recording"
            return
        }

        isEvaluating = true
        status = "Evaluating..."

        Task {
            // Merge all audio chunks
            let studentAudio = mergeKotlinFloatArrays(collectedAudio)

            // Extract pitch contour from student audio
            let studentContour = PitchContour.fromAudio(audioSamples: studentAudio, sampleRate: 16000)

            // Evaluate using CalibraMelodyEval with selected backend
            let evalResult = CalibraMelodyEval.evaluate(
                reference: reference,
                student: studentContour,
                studentSegments: nil,
                backend: selectedBackend
            )

            await MainActor.run {
                result = evalResult
                isEvaluating = false
                status = "Evaluation complete: \(Int(evalResult.overallScore * 100))%"
            }
        }
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil
    }

    // MARK: - Helpers

    private func mergeKotlinFloatArrays(_ arrays: [KotlinFloatArray]) -> KotlinFloatArray {
        let totalSize = arrays.reduce(0) { $0 + Int($1.size) }
        let result = KotlinFloatArray(size: Int32(totalSize))
        var offset: Int32 = 0
        for arr in arrays {
            for i in 0..<arr.size {
                result.set(index: offset + i, value: arr.get(index: i))
            }
            offset += arr.size
        }
        return result
    }
}
