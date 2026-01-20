import SwiftUI
import vozos

/// Pitch Processor Benchmark Section using Calibra public API.
///
/// Demonstrates:
/// - `CalibraPitchProcessor` for backend comparison (Kotlin vs Native)
/// - Benchmarking both implementations with timing
/// - Loading pitch data from .pitchPP files
struct PitchProcessorSection: View {
    @State private var pitchData: KotlinFloatArray?
    @State private var pitchCount: Int = 0
    @State private var voicedCount: Int = 0

    // Benchmark results
    @State private var isRunningBenchmark = false
    @State private var benchmarkResult: CalibraPitchProcessor.BenchmarkResult?

    // Individual processing results
    @State private var kotlinTimeMs: Int64 = 0
    @State private var nativeTimeMs: Int64 = 0
    @State private var kotlinProcessedCount: Int = 0
    @State private var nativeProcessedCount: Int = 0
    @State private var matchPercent: Float = 0.0

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Pitch Processor Benchmark")
                .font(.headline)

            Text("Compare Kotlin vs Native (C++) implementations")
                .font(.caption)
                .foregroundColor(.secondary)

            // Data loading section
            dataLoadingSection

            if pitchData != nil {
                Divider()
                    .padding(.vertical, 8)

                // Benchmark section
                benchmarkSection

                Divider()
                    .padding(.vertical, 8)

                // Results section
                if benchmarkResult != nil {
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

        // Convert to KotlinFloatArray
        let kotlinArray = KotlinFloatArray(size: Int32(pitches.count))
        for (i, pitch) in pitches.enumerated() {
            kotlinArray.set(index: Int32(i), value: pitch)
        }

        pitchData = kotlinArray
        pitchCount = pitches.count
        voicedCount = voiced
    }

    // MARK: - Benchmark

    private var benchmarkSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Benchmark")
                .font(.subheadline)
                .fontWeight(.semibold)

            HStack {
                Button("Run Benchmark (100 iterations)") {
                    runBenchmark()
                }
                .buttonStyle(.borderedProminent)
                .disabled(isRunningBenchmark || pitchData == nil)

                if isRunningBenchmark {
                    ProgressView()
                        .padding(.leading, 8)
                }
            }
        }
    }

    private func runBenchmark() {
        guard let data = pitchData else { return }

        isRunningBenchmark = true
        benchmarkResult = nil

        Task {
            // Run benchmark on background thread
            // Note: Kotlin object methods are accessed via .shared in Swift
            let result = await Task.detached {
                CalibraPitchProcessor.shared.benchmark(pitchesHz: data, iterations: 100)
            }.value

            await MainActor.run {
                benchmarkResult = result
                isRunningBenchmark = false
            }
        }
    }

    // MARK: - Results

    private var resultsSection: some View {
        guard let result = benchmarkResult else {
            return AnyView(EmptyView())
        }

        return AnyView(
            VStack(alignment: .leading, spacing: 12) {
                Text("Benchmark Results")
                    .font(.subheadline)
                    .fontWeight(.semibold)

                // Timing comparison
                HStack(spacing: 16) {
                    // Kotlin results
                    VStack {
                        Text("Kotlin")
                            .font(.caption)
                            .foregroundColor(.secondary)
                        Text(String(format: "%.2f ms", result.kotlinAvgMs))
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.orange)
                        Text("avg per iteration")
                            .font(.caption2)
                            .foregroundColor(.secondary)
                    }
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.orange.opacity(0.1))
                    .cornerRadius(8)

                    // Native results
                    VStack {
                        Text("Native (C++)")
                            .font(.caption)
                            .foregroundColor(.secondary)
                        Text(String(format: "%.2f ms", result.nativeAvgMs))
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.blue)
                        Text("avg per iteration")
                            .font(.caption2)
                            .foregroundColor(.secondary)
                    }
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.blue.opacity(0.1))
                    .cornerRadius(8)
                }

                // Speed ratio
                HStack {
                    Text("Speed Ratio:")
                        .font(.subheadline)
                    Spacer()
                    Text(String(format: "%.2fx", result.speedRatio))
                        .font(.headline)
                        .foregroundColor(result.speedRatio <= 3.0 ? .green : .orange)
                    Text(result.speedRatio <= 3.0 ? "(Acceptable)" : "(Needs optimization)")
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
                .padding()
                .background(Color(.secondarySystemBackground))
                .cornerRadius(8)

                // Match percentage
                HStack {
                    Text("Output Match:")
                        .font(.subheadline)
                    Spacer()
                    Text(String(format: "%.1f%%", result.matchPercent))
                        .font(.headline)
                        .foregroundColor(result.matchPercent >= 99.0 ? .green : .red)
                    Text(result.matchPercent >= 99.0 ? "(Pass)" : "(FAIL)")
                        .font(.caption)
                        .foregroundColor(result.matchPercent >= 99.0 ? .green : .red)
                }
                .padding()
                .background(Color(.secondarySystemBackground))
                .cornerRadius(8)

                // Summary
                VStack(alignment: .leading, spacing: 4) {
                    Text("Summary")
                        .font(.caption)
                        .fontWeight(.medium)
                    Text("• Input: \(result.inputSize) samples")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                    Text("• Iterations: \(result.iterations)")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                    Text("• Total Kotlin: \(result.kotlinTimeMs)ms, Native: \(result.nativeTimeMs)ms")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
                .padding(8)
                .background(Color(.tertiarySystemBackground))
                .cornerRadius(6)
            }
        )
    }

    // MARK: - API Info

    private var apiInfoSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("APIs Demonstrated:")
                .font(.caption)
                .fontWeight(.medium)

            Text("• CalibraPitchProcessor.benchmark() - Compare backends")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitchProcessor.process(backend:) - Process with specific backend")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitchProcessor.Backend.KOTLIN - Pure Kotlin implementation")
                .font(.caption2)
                .foregroundColor(.secondary)

            Text("• CalibraPitchProcessor.Backend.NATIVE - C++ via cinterop")
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}
