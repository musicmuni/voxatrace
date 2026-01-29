import SwiftUI
import Charts
import VoxaTrace

/// A pitch point for charting purposes.
struct PitchDataPoint: Identifiable {
    let id = UUID()
    let time: Float
    let pitch: Float  // In MIDI note numbers (e.g., 60 = C4)
    let series: String      // User-facing series name (for legend)
    let segmentId: String   // Internal segment ID (series + segment number)
}

/// Segments a pitch contour into consecutive voiced regions.
/// Returns array of PitchDataPoint with MIDI note numbers.
/// - Parameter inputIsMidi: If true, pitches are already in MIDI format (skip Hz→MIDI conversion)
private func segmentPitchContour(
    times: [Float],
    pitches: [Float],
    seriesName: String,
    inputIsMidi: Bool = false,
    gapThreshold: Float = 0.025  // 25ms gap threshold (2.5x the 10ms hop)
) -> [PitchDataPoint] {
    var points: [PitchDataPoint] = []
    var segmentIndex = 0
    var lastValidTime: Float? = nil

    for (time, pitch) in zip(times, pitches) {
        let midiNote: Float
        if inputIsMidi {
            // Input is already MIDI - use directly
            midiNote = pitch
        } else {
            // Input is Hz - convert to MIDI
            midiNote = CalibraMusic.hzToMidi(pitch)
        }

        // Skip invalid pitches (unvoiced sentinel is -1 for MIDI, <= 0 for Hz)
        if midiNote.isNaN || midiNote <= 0 {
            // Gap detected - next valid point starts a new segment
            lastValidTime = nil
            continue
        }

        // Check for time gap (unvoiced region passed)
        if let last = lastValidTime, (time - last) > gapThreshold {
            segmentIndex += 1
        }

        let segmentId = "\(seriesName)_\(segmentIndex)"
        points.append(PitchDataPoint(
            time: time,
            pitch: midiNote,
            series: seriesName,
            segmentId: segmentId
        ))
        lastValidTime = time
    }

    return points
}

/// A reusable pitch contour graph using Swift Charts.
///
/// Displays pitch values over time with musical note labels on Y-axis.
/// Supports both single contour and multi-contour overlay modes.
struct PitchGraphView: View {
    let dataPoints: [PitchDataPoint]
    let title: String?
    let height: CGFloat
    let seriesColors: [String: Color]
    let seriesOrder: [String]

    init(
        pitchesHz: [Float],
        times: [Float]? = nil,
        color: Color = .blue,
        title: String? = nil,
        series: String = "Pitch",
        height: CGFloat = 200,
        inputIsMidi: Bool = false
    ) {
        // Generate time values if not provided (assuming 10ms hop)
        let timeValues = times ?? pitchesHz.indices.map { Float($0) * 0.01 }

        // Segment the contour to avoid connecting across gaps
        self.dataPoints = segmentPitchContour(
            times: timeValues,
            pitches: pitchesHz,
            seriesName: series,
            inputIsMidi: inputIsMidi
        )
        self.title = title
        self.height = height
        self.seriesColors = [series: color]
        self.seriesOrder = [series]
    }

    /// Initialize with multiple pitch contours for overlay comparison.
    /// This version uses a shared times array for all contours (legacy mode).
    init(
        contours: [(pitches: [Float], color: Color, label: String)],
        times: [Float]? = nil,
        title: String? = nil,
        height: CGFloat = 200,
        inputIsMidi: Bool = false
    ) {
        var allPoints: [PitchDataPoint] = []
        var colors: [String: Color] = [:]
        var order: [String] = []

        for contour in contours {
            let timeValues = times ?? contour.pitches.indices.map { Float($0) * 0.01 }
            // Segment each contour to avoid connecting across gaps
            let points = segmentPitchContour(
                times: timeValues,
                pitches: contour.pitches,
                seriesName: contour.label,
                inputIsMidi: inputIsMidi
            )
            allPoints.append(contentsOf: points)
            colors[contour.label] = contour.color
            order.append(contour.label)
        }

        self.dataPoints = allPoints
        self.title = title
        self.height = height
        self.seriesColors = colors
        self.seriesOrder = order
    }

    /// Initialize with multiple pitch contours, each with its own times array.
    /// This ensures proper time alignment when contours have different frame counts.
    init(
        contoursWithTimes: [(times: [Float], pitches: [Float], color: Color, label: String)],
        title: String? = nil,
        height: CGFloat = 200,
        inputIsMidi: Bool = false
    ) {
        var allPoints: [PitchDataPoint] = []
        var colors: [String: Color] = [:]
        var order: [String] = []

        for contour in contoursWithTimes {
            // Each contour uses its own times array
            let points = segmentPitchContour(
                times: contour.times,
                pitches: contour.pitches,
                seriesName: contour.label,
                inputIsMidi: inputIsMidi
            )
            allPoints.append(contentsOf: points)
            colors[contour.label] = contour.color
            order.append(contour.label)
        }

        self.dataPoints = allPoints
        self.title = title
        self.height = height
        self.seriesColors = colors
        self.seriesOrder = order
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            if let title = title {
                Text(title)
                    .font(.caption)
                    .foregroundColor(.secondary)
            }

            Chart(dataPoints) { point in
                LineMark(
                    x: .value("Time (s)", point.time),
                    y: .value("MIDI", point.pitch),
                    series: .value("Segment", point.segmentId)
                )
                // Color by series name (not segment) so all segments of same series have same color
                .foregroundStyle(by: .value("Series", point.series))
                .lineStyle(StrokeStyle(lineWidth: 2))
            }
            // Map series names to colors
            .chartForegroundStyleScale(domain: seriesOrder, range: seriesOrder.map { seriesColors[$0] ?? .gray })
            .chartYScale(domain: yAxisDomain)
            .chartXAxisLabel("Time (s)")
            .chartYAxisLabel("Note")
            .chartYAxis {
                AxisMarks(values: yAxisTickValues) { value in
                    AxisGridLine()
                    AxisTick()
                    AxisValueLabel {
                        if let midi = value.as(Float.self) {
                            Text(CalibraMusic.midiToNoteLabel(midi))
                                .font(.caption2)
                        }
                    }
                }
            }
            .chartLegend(seriesNames.count > 1 ? .visible : .hidden)
            .frame(height: height)
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    /// Calculate Y-axis domain based on MIDI note range.
    private var yAxisDomain: ClosedRange<Float> {
        let pitches = dataPoints.map { $0.pitch }
        guard !pitches.isEmpty else {
            // Default to C3-C5 range (MIDI 48-72)
            return 48...72
        }

        let minMidi = pitches.min() ?? 48
        let maxMidi = pitches.max() ?? 72

        // Round to nearest semitone and add padding (at least 2 semitones each side)
        var lowerBound = floor(minMidi) - 2
        var upperBound = ceil(maxMidi) + 2

        // Enforce minimum range of 12 semitones (1 octave) for visual clarity
        let minRange: Float = 12.0
        let currentRange = upperBound - lowerBound
        if currentRange < minRange {
            let expansion = (minRange - currentRange) / 2.0
            lowerBound -= expansion
            upperBound += expansion
        }

        return lowerBound...upperBound
    }

    /// Generate tick values at whole semitone boundaries within the domain.
    private var yAxisTickValues: [Float] {
        let domain = yAxisDomain
        let range = domain.upperBound - domain.lowerBound

        // Determine stride: every semitone if range <= 12, otherwise thin out
        let stride: Float
        if range <= 12 {
            stride = 1.0  // Every semitone
        } else if range <= 24 {
            stride = 2.0  // Every whole tone
        } else {
            stride = 3.0  // Every minor third
        }

        var ticks: [Float] = []
        var current = ceil(domain.lowerBound)
        while current <= domain.upperBound {
            ticks.append(current)
            current += stride
        }

        return ticks
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
        // Single contour - G3 to E4 range
        PitchGraphView(
            pitchesHz: [196, 220, 247, -1, -1, 262, 277, 294, 311, 330],
            title: "Single Contour (G3-E4)"
        )

        // Comparison - showing raw (noisy) vs processed (smooth)
        PitchComparisonGraphView(
            rawPitches: [196, 392, 220, 208, 440, 247, 262, 277],
            processedPitches: [196, 196, 208, 208, 220, 247, 262, 277]
        )
    }
    .padding()
}
