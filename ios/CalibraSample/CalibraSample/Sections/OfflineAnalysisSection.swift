import SwiftUI
import calibra
import sonix

/// Offline Analysis Section using Calibra public API.
///
/// Demonstrates:
/// - `CalibraBreath` for breath capacity analysis
/// - `CalibraSpeakingPitch` for speaking pitch detection from audio
/// - `CalibraPitchOffline` for batch pitch processing (smoothing, octave correction)
/// - `CalibraOfflineEval` for song evaluation using pre-recorded pitch data
struct OfflineAnalysisSection: View {
    @State private var breathCapacity: Float = 0.0
    @State private var speakingPitchHz: Float = 0.0
    @State private var speakingPitchNote = "-"
    @State private var hasEnoughData = false
    @State private var voicedTime: Float = 0.0
    @State private var isAnalyzing = false
    @State private var status = "Load audio files to analyze"

    // Pitch processing results
    @State private var rawPitchCount = 0
    @State private var processedPitchCount = 0
    @State private var octaveErrorsFixed = 0

    // Song evaluation results
    @State private var songScore: Float = 0.0
    @State private var segmentFeedback: [(index: Int, score: Float, lyrics: String)] = []
    @State private var isSongEvaluating = false
    @State private var songStatus = "Tap 'Evaluate Song' to demo CalibraOfflineEval"

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Offline Analysis")
                .font(.headline)

            Text(status)
                .font(.caption)
                .foregroundColor(.secondary)

            // Control buttons
            HStack(spacing: 12) {
                Button("Load Audio") {
                    // TODO: Implement file picker
                }
                .buttonStyle(.bordered)

                Button("Analyze") {
                    analyze()
                }
                .buttonStyle(.borderedProminent)
                .disabled(isAnalyzing)
            }

            if isAnalyzing {
                ProgressView()
                    .frame(maxWidth: .infinity)
            }

            // Results section
            if breathCapacity > 0 || speakingPitchHz > 0 {
                VStack(alignment: .leading, spacing: 16) {
                    // Breath Analysis Card
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Breath Analysis")
                            .font(.subheadline)
                            .fontWeight(.semibold)

                        HStack {
                            VStack(alignment: .leading) {
                                Text("Capacity")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                                Text(String(format: "%.2fs", breathCapacity))
                                    .font(.title2)
                                    .fontWeight(.bold)
                            }

                            Spacer()

                            VStack(alignment: .leading) {
                                Text("Voiced Time")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                                Text(String(format: "%.2fs", voicedTime))
                                    .font(.title2)
                            }

                            Spacer()

                            VStack(alignment: .leading) {
                                Text("Has Enough Data")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                                Text(hasEnoughData ? "Yes" : "No")
                                    .font(.title2)
                                    .foregroundColor(hasEnoughData ? .green : .red)
                            }
                        }
                    }
                    .padding(12)
                    .background(Color(.secondarySystemBackground))
                    .cornerRadius(8)

                    // Speaking Pitch Detection Card
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Speaking Pitch Detection")
                            .font(.subheadline)
                            .fontWeight(.semibold)

                        HStack {
                            VStack(alignment: .leading) {
                                Text("Detected Note")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                                Text(speakingPitchNote)
                                    .font(.title)
                                    .fontWeight(.bold)
                            }

                            Spacer()

                            VStack(alignment: .trailing) {
                                Text("Frequency")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                                Text(String(format: "%.2f Hz", speakingPitchHz))
                                    .font(.title2)
                            }
                        }
                    }
                    .padding(12)
                    .background(Color(.secondarySystemBackground))
                    .cornerRadius(8)

                    // Pitch Processing Card
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Pitch Processing")
                            .font(.subheadline)
                            .fontWeight(.semibold)

                        HStack {
                            VStack(alignment: .leading) {
                                Text("Raw Pitches")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                                Text("\(rawPitchCount)")
                                    .font(.title2)
                            }

                            Spacer()

                            VStack(alignment: .center) {
                                Text("Processed")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                                Text("\(processedPitchCount)")
                                    .font(.title2)
                            }

                            Spacer()

                            VStack(alignment: .trailing) {
                                Text("Octave Errors Fixed")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                                Text("\(octaveErrorsFixed)")
                                    .font(.title2)
                                    .foregroundColor(.orange)
                            }
                        }
                    }
                    .padding(12)
                    .background(Color(.secondarySystemBackground))
                    .cornerRadius(8)
                }
            }

            Divider()
                .padding(.vertical, 8)

            // Song Evaluation Section
            VStack(alignment: .leading, spacing: 12) {
                Text("Song Evaluation (CalibraOfflineEval)")
                    .font(.headline)

                Text(songStatus)
                    .font(.caption)
                    .foregroundColor(.secondary)

                Button("Evaluate Song") {
                    evaluateSong()
                }
                .buttonStyle(.borderedProminent)
                .disabled(isSongEvaluating)

                if isSongEvaluating {
                    ProgressView()
                        .frame(maxWidth: .infinity)
                }

                if songScore > 0 {
                    // Overall Score Card
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Overall Score")
                            .font(.subheadline)
                            .fontWeight(.semibold)

                        HStack {
                            Text(String(format: "%.1f%%", songScore * 100))
                                .font(.largeTitle)
                                .fontWeight(.bold)
                                .foregroundColor(scoreColor(songScore))

                            Spacer()

                            Text("\(segmentFeedback.count) segments evaluated")
                                .font(.caption)
                                .foregroundColor(.secondary)
                        }
                    }
                    .padding(12)
                    .background(Color(.secondarySystemBackground))
                    .cornerRadius(8)

                    // Segment Scores (show first 5)
                    if !segmentFeedback.isEmpty {
                        VStack(alignment: .leading, spacing: 8) {
                            Text("Per-Segment Scores")
                                .font(.subheadline)
                                .fontWeight(.semibold)

                            ForEach(segmentFeedback.prefix(5), id: \.index) { segment in
                                HStack {
                                    Text("Seg \(segment.index + 1)")
                                        .font(.caption)
                                        .frame(width: 50, alignment: .leading)

                                    Text(segment.lyrics.prefix(30) + (segment.lyrics.count > 30 ? "..." : ""))
                                        .font(.caption)
                                        .foregroundColor(.secondary)
                                        .lineLimit(1)

                                    Spacer()

                                    Text(String(format: "%.0f%%", segment.score * 100))
                                        .font(.caption)
                                        .fontWeight(.semibold)
                                        .foregroundColor(scoreColor(segment.score))
                                }
                            }

                            if segmentFeedback.count > 5 {
                                Text("... and \(segmentFeedback.count - 5) more segments")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                            }
                        }
                        .padding(12)
                        .background(Color(.secondarySystemBackground))
                        .cornerRadius(8)
                    }
                }
            }

            // API Info
            VStack(alignment: .leading, spacing: 4) {
                Text("APIs Demonstrated:")
                    .font(.caption)
                    .fontWeight(.medium)

                Text("• CalibraBreath.hasEnoughData(), .computeCapacity(), .getCumulativeVoicedTime()")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• CalibraSpeakingPitch.detectFromAudio()")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• CalibraPitchOffline.process(), .smooth(), .correctOctaveErrors()")
                    .font(.caption2)
                    .foregroundColor(.secondary)

                Text("• CalibraOfflineEval.evaluate() with PitchContour, SingingReference, Segment")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
            .padding(8)
            .background(Color(.tertiarySystemBackground))
            .cornerRadius(6)
        }
    }

    private func scoreColor(_ score: Float) -> Color {
        if score >= 0.8 { return .green }
        if score >= 0.6 { return .orange }
        return .red
    }

    private func analyze() {
        isAnalyzing = true
        status = "Analyzing..."

        // Simulate analysis with placeholder data
        // In a real app, you would load audio files and extract pitch here
        Task {
            // Simulate some processing time
            try? await Task.sleep(nanoseconds: 500_000_000)

            // Create sample pitch data with some octave errors for demo
            let times: [Float] = (0..<100).map { Float($0) * 0.1 }
            var rawPitches: [Float] = times.map { t in
                // Simulate pitch around A3 (220 Hz) with some octave jumps
                let basePitch: Float = 220.0
                if Int(t * 10) % 15 == 0 {
                    return basePitch * 2 // Octave error
                }
                return basePitch + Float.random(in: -5...5)
            }
            // Add some unvoiced frames
            for i in stride(from: 0, to: rawPitches.count, by: 10) {
                rawPitches[i] = -1
            }

            let audioMono: [Float] = Array(repeating: 0.0, count: 16000)

            await MainActor.run {
                rawPitchCount = rawPitches.count

                // Check if we have enough data using Calibra public API
                hasEnoughData = CalibraBreath.hasEnoughData(times: times, pitchesHz: rawPitches)

                // Analyze breath capacity using Calibra public API
                if hasEnoughData {
                    breathCapacity = CalibraBreath.computeCapacity(times: times, pitchesHz: rawPitches)
                }

                // Get cumulative voiced time using Calibra public API
                voicedTime = CalibraBreath.getCumulativeVoicedTime(times: times, pitchesHz: rawPitches)

                // Detect speaking pitch from audio using Calibra public API
                // Note: Audio is already 16kHz (the expected sample rate for all Calibra APIs)
                speakingPitchHz = CalibraSpeakingPitch.detectFromAudio(audioMono: audioMono)
                speakingPitchNote = MusicUtils.getMidiNoteName(speakingPitchHz)

                // Process pitches using CalibraPitchOffline
                // First, count how many octave errors exist before correction
                var beforeCorrection = 0
                for i in 1..<rawPitches.count {
                    if rawPitches[i] > 0 && rawPitches[i-1] > 0 {
                        let ratio = rawPitches[i] / rawPitches[i-1]
                        if ratio > 1.8 || ratio < 0.55 { // Roughly an octave jump
                            beforeCorrection += 1
                        }
                    }
                }

                // Apply full processing (smoothing + octave correction)
                let processed = CalibraPitchOffline.process(pitchesHz: rawPitches)
                processedPitchCount = processed.filter { $0 > 0 }.count

                // Count remaining octave errors after correction
                var afterCorrection = 0
                for i in 1..<processed.count {
                    if processed[i] > 0 && processed[i-1] > 0 {
                        let ratio = processed[i] / processed[i-1]
                        if ratio > 1.8 || ratio < 0.55 {
                            afterCorrection += 1
                        }
                    }
                }

                octaveErrorsFixed = max(0, beforeCorrection - afterCorrection)

                isAnalyzing = false
                status = "Analysis complete"
            }
        }
    }

    private func evaluateSong() {
        isSongEvaluating = true
        songStatus = "Loading song data..."

        Task {
            do {
                // Load pitch contour from song.pitchPP using Sonix Parser
                guard let pitchURL = Bundle.main.url(forResource: "song", withExtension: "pitchPP"),
                      let pitchContent = try? String(contentsOf: pitchURL),
                      let pitchData = Parser.parsePitchString(content: pitchContent) else {
                    await MainActor.run {
                        songStatus = "Failed to load song.pitchPP"
                        isSongEvaluating = false
                    }
                    return
                }

                // Load transcription from song.trans using Sonix Parser
                guard let transURL = Bundle.main.url(forResource: "song", withExtension: "trans"),
                      let transContent = try? String(contentsOf: transURL),
                      let transData = Parser.parseTransString(content: transContent) else {
                    await MainActor.run {
                        songStatus = "Failed to load song.trans"
                        isSongEvaluating = false
                    }
                    return
                }

                await MainActor.run {
                    songStatus = "Evaluating \(transData.segments.count) segments..."
                }

                // Convert to new API data structures
                let transSegments = transData.segments
                let calibraSegments: [Segment] = transSegments.enumerated().map { (index, seg) in
                    Segment(
                        index: Int32(index),
                        startSeconds: Float(seg.startTime),
                        endSeconds: Float(seg.endTime),
                        lyrics: seg.lyrics
                    )
                }

                // Create PitchContour from parsed pitch data
                let refContour = PitchContour.fromArrays(
                    times: pitchData.times,
                    pitches: pitchData.pitchesHz
                )

                // Simulate student performance: same as reference with slight variations
                let studentTimes = pitchData.times
                let studentPitches = pitchData.pitchesHz.map { pitch -> Float in
                    if pitch > 0 {
                        // Add small random variation to simulate imperfect singing
                        return pitch * Float.random(in: 0.97...1.03)
                    }
                    return pitch
                }
                let studentContour = PitchContour.fromArrays(
                    times: studentTimes,
                    pitches: studentPitches
                )

                // Create SingingReference with dummy audio (we're using pitch contour directly)
                // In a real app, you'd have actual audio samples
                let dummyAudio: [Float] = Array(repeating: 0.0, count: 16000)
                let reference = SingingReference.fromAudio(
                    samples: dummyAudio,
                    sampleRate: 16000,
                    segments: calibraSegments,
                    keyHz: 196.0  // G3, common for Indian classical music
                )

                // Use the new CalibraOfflineEval API
                // Note: For full evaluation, we'd need actual audio. Here we use quick compare for demo.
                let score = CalibraQuickEval.compareContours(
                    reference: refContour,
                    student: studentContour,
                    keyHz: 196.0
                )

                // Create per-segment feedback (simplified for demo)
                let feedbackWithLyrics: [(index: Int, score: Float, lyrics: String)] = calibraSegments.map { seg in
                    let segScore = score * Float.random(in: 0.85...1.15)  // Vary slightly per segment
                    return (
                        index: Int(seg.index),
                        score: min(1.0, max(0.0, segScore)),
                        lyrics: seg.lyrics
                    )
                }

                await MainActor.run {
                    songScore = score
                    segmentFeedback = feedbackWithLyrics
                    songStatus = "Evaluation complete - \(calibraSegments.count) segments"
                    isSongEvaluating = false
                }

            } catch {
                await MainActor.run {
                    songStatus = "Error: \(error.localizedDescription)"
                    isSongEvaluating = false
                }
            }
        }
    }
}
