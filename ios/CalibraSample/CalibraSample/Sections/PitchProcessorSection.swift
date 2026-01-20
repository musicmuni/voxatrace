import SwiftUI
import vozos

/// Pitch Processor Section using Calibra public API.
///
/// Demonstrates:
/// - `CalibraPitch.PostProcess` for batch pitch processing
/// - Loading pitch data from .pitchPP files
/// - Smoothing, octave correction, and median filtering
struct PitchProcessorSection: View {
    @State private var pitchData: [Float]?
    @State private var pitchCount: Int = 0
    @State private var voicedCount: Int = 0

    // Processing results
    @State private var isProcessing = false
    @State private var processedCount: Int = 0
    @State private var processingTimeMs: Int64 = 0

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Pitch Processor")
                .font(.headline)

            Text("Offline pitch processing with smoothing and octave correction")
                .font(.caption)
                .foregroundColor(.secondary)

            // Data loading section
            dataLoadingSection

            if pitchData != nil {
                Divider()
                    .padding(.vertical, 8)

                // Processing section
                processingSection

                Divider()
                    .padding(.vertical, 8)

                // Results section
                if processedCount > 0 {
                    resultsSection
                }
            }

            Divider()
                .padding(.vertical, 8)

            // API Info
            apiInfoSection
        }
    }

    // MARK: - Data Loading

    private var dataLoadingSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Pitch Data")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                Button("Load Alankaar 01.pitchPP") {
                    loadPitchFile()
                }
                .buttonStyle(.bordered)

                Spacer()

                if pitchCount > 0 {
                    VStack(alignment: .trailing) {
                        Text("\(pitchCount) samples")
                            .font(.caption)
                            .fontWeight(.medium)
                        Text("\(voicedCount) voiced")
                            .font(.caption2)
                            .foregroundColor(.secondary)
                    }
                }
            }
        }
    }

    private func loadPitchFile() {
        guard let url = Bundle.main.url(forResource: "Alankaar 01", withExtension: "pitchPP"),
              let contents = try? String(contentsOf: url, encoding: .utf8) else {
            print("Failed to load pitch file")
            return
        }

        // Parse TSV format: time\tpitchHz
        let lines = contents.components(separatedBy: .newlines)
        var pitches: [Float] = []
        var voiced = 0

        for line in lines {
            let parts = line.split(separator: "\t")
            if parts.count >= 2, let pitch = Float(parts[1]) {
                pitches.append(pitch)
                if pitch > 0 {
                    voiced += 1
                }
            }
        }

        pitchData = pitches
        pitchCount = pitches.count
        voicedCount = voiced
    }

    // MARK: - Processing

    private var processingSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Process Pitch Data")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                Button("Run Processing") {
                    runProcessing()
                }
                .buttonStyle(.borderedProminent)
                .disabled(isProcessing || pitchData == nil)

                if isProcessing {
                    ProgressView()
                        .padding(.leading, 8)
                }
            }
        }
    }

    private func runProcessing() {
        guard let data = pitchData else { return }

        isProcessing = true
        processedCount = 0

        Task {
            let startTime = CFAbsoluteTimeGetCurrent()

            // Run processing on background thread
            let result = await Task.detached {
                CalibraPitch.PostProcess.process(pitchesHz: data)
            }.value

            let endTime = CFAbsoluteTimeGetCurrent()
            let timeMs = Int64((endTime - startTime) * 1000)

            await MainActor.run {
                processedCount = result.count
                processingTimeMs = timeMs
                isProcessing = false
            }
        }
    }

    // MARK: - Results

    private var resultsSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Processing Results")
                .font(.subheadline)
                .fontWeight(.semibold)

            VStack {
                Text("Processed")
                    .font(.caption)
                    .foregroundColor(.secondary)
                Text("\(processedCount) samples")
                    .font(.title2)
                    .fontWeight(.bold)
                    .foregroundColor(.blue)
                Text("in \(processingTimeMs) ms")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
            .frame(maxWidth: .infinity)
            .padding()
            .background(Color.blue.opacity(0.1))
            .cornerRadius(8)

            // Summary
            VStack(alignment: .leading, spacing: 4) {
                Text("Processing Info")
                    .font(.caption)
                    .fontWeight(.medium)
                Text("• Input: \(pitchCount) samples")
                    .font(.caption2)
                    .foregroundColor(.secondary)
                Text("• Output: \(processedCount) samples")
                    .font(.caption2)
                    .foregroundColor(.secondary)
                Text("• Processing time: \(processingTimeMs)ms")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
            .padding(8)
            .background(Color(.tertiarySystemBackground))
            .cornerRadius(6)
        }
    }

    // MARK: - API Info

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• CalibraPitch.PostProcess.process() - Full processing pipeline")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.smooth() - Smoothing filter only")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.correctOctaveErrors() - Octave correction only")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitch.PostProcess.medianFilter() - Median filter only")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}
