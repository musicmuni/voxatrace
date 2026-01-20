import SwiftUI
import Charts

/// A pitch point for charting purposes.
struct PitchDataPoint: Identifiable {
    let id = UUID()
    let time: Float
    let pitch: Float
    let series: String
}

/// A reusable pitch contour graph using Swift Charts.
///
/// Displays pitch values over time with automatic Y-axis scaling.
/// Supports both single contour and multi-contour overlay modes.
struct PitchGraphView: View {
    let dataPoints: [PitchDataPoint]
    let title: String?
    let height: CGFloat

    init(
        pitchesHz: [Float],
        times: [Float]? = nil,
        color: Color = .blue,
        title: String? = nil,
        series: String = "Pitch",
        height: CGFloat = 200
    ) {
        // Generate time values if not provided (assuming 10ms hop)
        let timeValues = times ?? pitchesHz.indices.map { Float($0) * 0.01 }

        self.dataPoints = zip(timeValues, pitchesHz).map { time, pitch in
            PitchDataPoint(time: time, pitch: pitch, series: series)
        }
        self.title = title
        self.height = height
    }

    /// Initialize with multiple pitch contours for overlay comparison.
    init(
        contours: [(pitches: [Float], color: Color, label: String)],
        times: [Float]? = nil,
        title: String? = nil,
        height: CGFloat = 200
    ) {
        var allPoints: [PitchDataPoint] = []

        for contour in contours {
            let timeValues = times ?? contour.pitches.indices.map { Float($0) * 0.01 }
            let points = zip(timeValues, contour.pitches).map { time, pitch in
                PitchDataPoint(time: time, pitch: pitch, series: contour.label)
            }
            allPoints.append(contentsOf: points)
        }

        self.dataPoints = allPoints
        self.title = title
        self.height = height
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            if let title = title {
                Text(title)
                    .font(.caption)
                    .foregroundColor(.secondary)
            }

            Chart(voicedDataPoints) { point in
                LineMark(
                    x: .value("Time (s)", point.time),
                    y: .value("Pitch (Hz)", point.pitch)
                )
                .foregroundStyle(by: .value("Series", point.series))
            }
            .chartYScale(domain: yAxisDomain)
            .chartXAxisLabel("Time (s)")
            .chartYAxisLabel("Pitch (Hz)")
            .chartLegend(seriesNames.count > 1 ? .visible : .hidden)
            .frame(height: height)
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    /// Filter out unvoiced frames (pitch <= 0).
    private var voicedDataPoints: [PitchDataPoint] {
        dataPoints.filter { $0.pitch > 0 }
    }

    /// Calculate Y-axis domain based on voiced pitch range.
    private var yAxisDomain: ClosedRange<Float> {
        let voicedPitches = voicedDataPoints.map { $0.pitch }
        guard !voicedPitches.isEmpty else { return 50...500 }

        let minPitch = voicedPitches.min() ?? 50
        let maxPitch = voicedPitches.max() ?? 500

        // Add 10% padding
        let padding = (maxPitch - minPitch) * 0.1
        let lowerBound = max(minPitch - padding, 30)
        let upperBound = min(maxPitch + padding, 2000)

        return lowerBound...upperBound
    }

    /// Get unique series names for legend.
    private var seriesNames: Set<String> {
        Set(dataPoints.map { $0.series })
    }
}

/// A specialized overlay graph for comparing raw vs processed pitch contours.
struct PitchComparisonGraphView: View {
    let rawPitches: [Float]
    let processedPitches: [Float]
    let times: [Float]?
    let rawLabel: String
    let processedLabel: String
    let height: CGFloat

    init(
        rawPitches: [Float],
        processedPitches: [Float],
        times: [Float]? = nil,
        rawLabel: String = "Raw",
        processedLabel: String = "Processed",
        height: CGFloat = 200
    ) {
        self.rawPitches = rawPitches
        self.processedPitches = processedPitches
        self.times = times
        self.rawLabel = rawLabel
        self.processedLabel = processedLabel
        self.height = height
    }

    var body: some View {
        PitchGraphView(
            contours: [
                (pitches: rawPitches, color: .gray, label: rawLabel),
                (pitches: processedPitches, color: .blue, label: processedLabel)
            ],
            times: times,
            title: "Raw vs Processed",
            height: height
        )
    }
}

/// Multi-contour overlay for post-processing demo.
struct MultiContourGraphView: View {
    struct ContourData {
        let pitches: [Float]
        let label: String
        let color: Color
        let isVisible: Bool
    }

    let contours: [ContourData]
    let times: [Float]?
    let height: CGFloat

    var body: some View {
        let visibleContours = contours.filter { $0.isVisible }

        VStack(alignment: .leading, spacing: 4) {
            Text("Pitch Contours")
                .font(.caption)
                .foregroundColor(.secondary)

            if visibleContours.isEmpty {
                Text("Select contours to display")
                    .foregroundColor(.secondary)
                    .frame(height: height)
                    .frame(maxWidth: .infinity)
            } else {
                PitchGraphView(
                    contours: visibleContours.map { ($0.pitches, $0.color, $0.label) },
                    times: times,
                    height: height
                )
            }
        }
    }
}

// MARK: - Preview

#Preview {
    VStack(spacing: 16) {
        // Single contour
        PitchGraphView(
            pitchesHz: [220, 225, 230, -1, -1, 235, 240, 245, 250, 255],
            title: "Single Contour"
        )

        // Comparison
        PitchComparisonGraphView(
            rawPitches: [220, 440, 230, 225, 450, 235, 240, 245],
            processedPitches: [220, 220, 225, 225, 230, 235, 240, 245]
        )
    }
    .padding()
}
