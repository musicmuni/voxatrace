import SwiftUI
import VozOS

/// Note Eval Section - for note/exercise evaluation (scales, arpeggios, patterns).
///
/// Demonstrates:
/// - `CalibraNoteEval` for note-by-note exercise evaluation
/// - `ExercisePattern` for defining scale/arpeggio patterns
/// - `ExerciseResult` and `NoteResult` for per-note feedback
/// - `SonixRecorder` for audio capture
///
/// Flow:
/// 1. Select exercise type from picker
/// 2. View visual pattern of notes to play
/// 3. Record student attempt
/// 4. Evaluate and show per-note scores
struct NoteEvalSection: View {
    // Exercise selection
    @State private var selectedExercise = 0

    // Recording state
    @State private var isRecording = false
    @State private var hasRecording = false
    @State private var recordingDuration: Float = 0.0
    @State private var recordingLevel: Float = 0.0

    // Evaluation state
    @State private var isEvaluating = false
    @State private var result: ExerciseResult?
    @State private var status = "Select an exercise and record"

    // Backend selection
    @State private var selectedBackend: CalibraNoteEval.Backend = .kotlin

    // Resources
    @State private var recorder: SonixRecorder?
    @State private var collectedAudio: [Float] = []

    // Exercise definitions: (name, MIDI notes, key MIDI note)
    // Using C3-B3 range (MIDI 48-59) for easier voice testing
    private let exercises: [(String, [Int32], Int32)] = [
        ("C Major Scale (ascending)", [48, 50, 52, 53, 55, 57, 59, 60], 48),
        ("C Major Scale (descending)", [60, 59, 57, 55, 53, 52, 50, 48], 48),
        ("C Minor Scale", [48, 50, 51, 53, 55, 56, 58, 60], 48),
        ("C Major Arpeggio", [48, 52, 55, 60], 48),
        ("G Major Scale", [55, 57, 59, 60, 62, 64, 66, 67], 55),
        ("Sa Re Ga (C)", [48, 50, 52], 48),
        ("Sa Re Ga Ma Pa (G)", [55, 57, 59, 60, 62], 55)
    ]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Note/Exercise Evaluation")
                .font(.headline)

            Text(status)
                .font(.caption)
                .foregroundColor(.secondary)

            // Step 1: Select exercise
            exercisePickerSection

            // Visual pattern display
            patternDisplaySection

            // Step 2: Record
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
        .onDisappear {
            cleanup()
        }
    }

    // MARK: - Subviews

    private var exercisePickerSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 1: Select Exercise")
                .font(.subheadline)
                .fontWeight(.medium)

            Picker("Exercise", selection: $selectedExercise) {
                ForEach(0..<exercises.count, id: \.self) { index in
                    Text(exercises[index].0).tag(index)
                }
            }
            .pickerStyle(.menu)
            .onChange(of: selectedExercise) { _ in
                // Reset when exercise changes
                result = nil
                hasRecording = false
                status = "Selected: \(exercises[selectedExercise].0)"
            }
        }
    }

    private var patternDisplaySection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Notes to Play:")
                .font(.caption)
                .foregroundColor(.secondary)

            // Display note names horizontally
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 8) {
                    ForEach(0..<exercises[selectedExercise].1.count, id: \.self) { index in
                        noteChip(
                            index: index,
                            midiNote: Int(exercises[selectedExercise].1[index]),
                            result: result?.noteResults.first { $0.noteIndex == Int32(index) }
                        )
                    }
                }
            }
        }
        .padding(12)
        .background(Color.blue.opacity(0.1))
        .cornerRadius(8)
    }

    private func noteChip(index: Int, midiNote: Int, result: NoteResult?) -> some View {
        let noteName = MusicUtils.getMidiNoteName(MusicUtils.midiToHz(midiNote))

        let backgroundColor: Color = {
            guard let result = result else {
                return Color(.secondarySystemBackground)
            }
            if result.score >= 0.8 {
                return .green
            } else if result.score >= 0.5 {
                return .orange
            } else {
                return .red
            }
        }()

        let foregroundColor: Color = result != nil ? .white : .primary

        return VStack(spacing: 2) {
            Text(noteName)
                .font(.caption)
                .fontWeight(.medium)
            if let result = result {
                Text("\(Int(result.score * 100))%")
                    .font(.system(size: 10))
            }
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 6)
        .background(backgroundColor)
        .foregroundColor(foregroundColor)
        .cornerRadius(6)
    }

    private var recordingSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Step 2: Record Your Performance")
                .font(.subheadline)
                .fontWeight(.medium)

            Text("Play each note for about 0.5 seconds")
                .font(.caption)
                .foregroundColor(.secondary)

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
                    Text("Kotlin").tag(CalibraNoteEval.Backend.kotlin)
                    Text("Native (C++)").tag(CalibraNoteEval.Backend.native)
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

    private func resultsSection(_ result: ExerciseResult) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Results")
                .font(.subheadline)
                .fontWeight(.medium)

            // Overall score
            overallScoreCard(result)

            // Stats
            HStack {
                statBox(title: "Notes", value: "\(result.noteCount)")
                statBox(title: "Passing", value: "\(result.passingNotes)/\(result.noteCount)")
                statBox(title: "Pass Rate", value: "\(Int(result.passingRatio * 100))%")
            }
        }
    }

    private func overallScoreCard(_ result: ExerciseResult) -> some View {
        let backgroundColor: Color = {
            if result.score >= 0.8 {
                return .green
            } else if result.score >= 0.6 {
                return .orange
            } else {
                return .red
            }
        }()

        let feedbackMessage: String = {
            if result.score >= 0.9 {
                return "Excellent! Perfect performance."
            } else if result.score >= 0.8 {
                return "Great job! Almost perfect."
            } else if result.score >= 0.7 {
                return "Good job! Keep practicing."
            } else if result.score >= 0.5 {
                return "Not bad. Focus on pitch accuracy."
            } else {
                return "Keep practicing! Match each note carefully."
            }
        }()

        return VStack(spacing: 4) {
            Text("Overall Score")
                .font(.caption)
                .foregroundColor(.white.opacity(0.8))
            Text("\(result.scorePercent)%")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.white)
            Text(feedbackMessage)
                .font(.caption)
                .foregroundColor(.white.opacity(0.9))
        }
        .frame(maxWidth: .infinity)
        .padding(16)
        .background(backgroundColor)
        .cornerRadius(12)
    }

    private func statBox(title: String, value: String) -> some View {
        VStack(spacing: 2) {
            Text(title)
                .font(.caption2)
                .foregroundColor(.secondary)
            Text(value)
                .font(.caption)
                .fontWeight(.medium)
        }
        .frame(maxWidth: .infinity)
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(6)
    }

    // MARK: - Actions

    private func startRecording() {
        // Reset state
        collectedAudio = []
        hasRecording = false
        result = nil
        recordingDuration = 0.0
        status = "Recording..."

        // Create recorder
        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("note_eval_temp.m4a").path
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
                    samples: buffer.samples,
                    fromRate: hwRate,
                    toRate: 16000
                )

                collectedAudio.append(contentsOf: samples16k)
                sampleCount += samples16k.count

                // Calculate RMS for level meter
                let sum = samples16k.reduce(0) { $0 + $1 * $1 }
                let rms = sqrt(sum / Float(samples16k.count))

                await MainActor.run {
                    recordingLevel = min(rms * 5, 1.0)
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
        guard !collectedAudio.isEmpty else {
            status = "No recording to evaluate"
            return
        }

        isEvaluating = true
        status = "Evaluating..."

        Task {
            // Get selected exercise
            let exercise = exercises[selectedExercise]
            let midiNotes = exercise.1
            let keyMidi = exercise.2

            // Create exercise pattern
            let pattern = ExercisePattern.fromMidiNotes(
                midiNotes: midiNotes,
                noteDurationMs: 500
            )

            // Use collected audio directly (already merged as [Float])
            let studentAudio = collectedAudio

            // Extract pitch contour from student audio using ContourExtractor
            let extractor = CalibraPitch.createContourExtractor()
            let studentContour = extractor.extract(audio: studentAudio)
            extractor.release()

            // Key frequency from MIDI
            let keyHz = MusicUtils.midiToHz(Int(keyMidi))

            // Evaluate using CalibraNoteEval with selected backend
            let evalResult = CalibraNoteEval.evaluate(
                pattern: pattern,
                student: studentContour,
                referenceKeyHz: keyHz,
                studentKeyHz: 0,
                scoreType: 0,
                leewaySamples: 0,
                backend: selectedBackend
            )

            await MainActor.run {
                result = evalResult
                isEvaluating = false
                status = "Evaluation complete: \(evalResult.scorePercent)%"
            }
        }
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil
    }

}
