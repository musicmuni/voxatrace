import SwiftUI
import VoxaTrace

// MARK: - Helper Views

struct StatCard: View {
    let title: String
    let value: String
    var highlight: Bool = false

    var body: some View {
        VStack {
            Text(title)
                .font(.caption2)
                .foregroundColor(.secondary)
            Text(value)
                .font(.caption)
                .fontWeight(.semibold)
                .foregroundColor(highlight ? .green : .primary)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity)
        .padding(8)
        .background(Color(.tertiarySystemBackground))
        .cornerRadius(6)
    }
}

struct ToggleChip: View {
    let label: String
    let color: Color
    @Binding var isOn: Bool

    var body: some View {
        Button {
            isOn.toggle()
        } label: {
            HStack(spacing: 4) {
                Circle()
                    .fill(color)
                    .frame(width: 8, height: 8)
                Text(label)
                    .font(.caption)
            }
            .padding(.horizontal, 10)
            .padding(.vertical, 6)
            .background(isOn ? color.opacity(0.2) : Color(.tertiarySystemBackground))
            .cornerRadius(16)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(isOn ? color : .clear, lineWidth: 1)
            )
        }
        .buttonStyle(.plain)
    }
}

struct PropertyRow: View {
    let name: String
    let value: String

    var body: some View {
        HStack {
            Text(name)
                .font(.caption)
                .foregroundColor(.secondary)
            Spacer()
            Text(value)
                .font(.caption)
                .fontWeight(.medium)
                .foregroundColor(.primary)
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 4)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(4)
    }
}

struct TuningIndicatorView: View {
    let centsOff: Int32
    let tuning: PitchPoint.Tuning

    var body: some View {
        VStack(spacing: 4) {
            // Tuning bar
            GeometryReader { geo in
                ZStack {
                    // Background
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color(.tertiarySystemBackground))

                    // Center line
                    Rectangle()
                        .fill(Color.green)
                        .frame(width: 2)

                    // Indicator
                    Circle()
                        .fill(tuningColor)
                        .frame(width: 16, height: 16)
                        .offset(x: CGFloat(centsOff) / 50.0 * (geo.size.width / 2 - 8))
                }
            }
            .frame(height: 24)

            // Labels
            HStack {
                Text("-50¢")
                    .font(.caption2)
                    .foregroundColor(.secondary)
                Spacer()
                Text("0")
                    .font(.caption2)
                    .foregroundColor(.green)
                Spacer()
                Text("+50¢")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }

            // Status
            Text(tuningLabel)
                .font(.caption)
                .fontWeight(.medium)
                .foregroundColor(tuningColor)
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }

    private var tuningColor: Color {
        switch tuning {
        case .inTune: return .green
        case .flat, .sharp: return .orange
        case .silent: return .gray
        default: return .gray
        }
    }

    private var tuningLabel: String {
        switch tuning {
        case .inTune: return "IN TUNE"
        case .flat: return "FLAT"
        case .sharp: return "SHARP"
        case .silent: return "SILENT"
        default: return "-"
        }
    }
}

// MARK: - Scrolling Pitch Contour View

/// A real-time scrolling pitch contour visualization using Canvas for performance.
/// Current pitch is fixed at horizontal center, historical values scroll left.
struct ScrollingPitchContourView: View {
    let pitchHistory: [Float]  // Recent pitch values (Hz), newest last
    let maxPoints: Int         // Number of points to display
    let height: CGFloat

    // Fixed Y-axis range: B2 (MIDI 47) to B4 (MIDI 71) - two octaves
    private let minMidi: Float = 47  // B2
    private let maxMidi: Float = 71  // B4

    // Note labels to display on Y-axis (every 2 semitones for readability)
    private var noteLabels: [(midi: Float, label: String)] {
        var labels: [(Float, String)] = []
        var midi: Float = minMidi
        while midi <= maxMidi {
            labels.append((midi, CalibraMusic.midiToNoteLabel(midi)))
            midi += 2  // Every whole tone
        }
        return labels
    }

    var body: some View {
        Canvas { context, size in
            let leftMargin: CGFloat = 30  // Space for Y-axis labels
            let rightMargin: CGFloat = 10
            let topMargin: CGFloat = 8
            let bottomMargin: CGFloat = 8

            let graphWidth = size.width - leftMargin - rightMargin
            let graphHeight = size.height - topMargin - bottomMargin
            let graphRect = CGRect(x: leftMargin, y: topMargin, width: graphWidth, height: graphHeight)

            // Center X position (current pitch location)
            let centerX = leftMargin + graphWidth / 2

            // Helper to convert MIDI to Y coordinate
            func midiToY(_ midi: Float) -> CGFloat {
                let normalizedMidi = (midi - minMidi) / (maxMidi - minMidi)
                return graphRect.maxY - CGFloat(normalizedMidi) * graphHeight
            }

            // Draw background
            context.fill(
                Path(roundedRect: CGRect(origin: .zero, size: size), cornerRadius: 8),
                with: .color(Color(.secondarySystemBackground))
            )

            // Draw horizontal grid lines and Y-axis labels
            for (midi, label) in noteLabels {
                let y = midiToY(midi)

                // Grid line
                var gridPath = Path()
                gridPath.move(to: CGPoint(x: leftMargin, y: y))
                gridPath.addLine(to: CGPoint(x: size.width - rightMargin, y: y))
                context.stroke(gridPath, with: .color(Color.gray.opacity(0.2)), lineWidth: 0.5)

                // Y-axis label
                let text = Text(label).font(.system(size: 8)).foregroundColor(.secondary)
                context.draw(text, at: CGPoint(x: leftMargin - 4, y: y), anchor: .trailing)
            }

            // Draw center vertical line (current position indicator)
            var centerLine = Path()
            centerLine.move(to: CGPoint(x: centerX, y: topMargin))
            centerLine.addLine(to: CGPoint(x: centerX, y: size.height - bottomMargin))
            context.stroke(centerLine, with: .color(Color.blue.opacity(0.3)), lineWidth: 1)

            // Draw pitch contour
            guard !pitchHistory.isEmpty else { return }

            // Calculate point spacing - history fills left half, current at center
            // Points are positioned such that the newest point (last in array) is at center
            let pointSpacing = graphWidth / CGFloat(maxPoints)

            // Start drawing from the oldest visible point
            var contourPath = Path()
            var isDrawing = false

            for (index, pitchHz) in pitchHistory.enumerated() {
                // X position: newest point at center, older points to the left
                let pointsFromEnd = pitchHistory.count - 1 - index
                let x = centerX - CGFloat(pointsFromEnd) * pointSpacing

                // Skip points outside visible area
                if x < leftMargin { continue }

                // Check if voiced (valid pitch)
                let midi = CalibraMusic.hzToMidi(pitchHz)
                let isVoiced = pitchHz > 0 && midi > 0 && !midi.isNaN

                if isVoiced {
                    // Clamp MIDI to visible range
                    let clampedMidi = min(max(midi, minMidi), maxMidi)
                    let y = midiToY(clampedMidi)

                    if isDrawing {
                        contourPath.addLine(to: CGPoint(x: x, y: y))
                    } else {
                        contourPath.move(to: CGPoint(x: x, y: y))
                        isDrawing = true
                    }
                } else {
                    // Gap in pitch - break the line
                    isDrawing = false
                }
            }

            // Stroke the contour
            context.stroke(contourPath, with: .color(.blue), style: StrokeStyle(lineWidth: 2, lineCap: .round, lineJoin: .round))

            // Draw current pitch indicator (dot at center)
            if let lastPitch = pitchHistory.last {
                let midi = CalibraMusic.hzToMidi(lastPitch)
                let isVoiced = lastPitch > 0 && midi > 0 && !midi.isNaN

                if isVoiced {
                    let clampedMidi = min(max(midi, minMidi), maxMidi)
                    let y = midiToY(clampedMidi)

                    // Draw a dot at the current pitch position
                    let dotPath = Path(ellipseIn: CGRect(x: centerX - 4, y: y - 4, width: 8, height: 8))
                    context.fill(dotPath, with: .color(.blue))
                }
            }
        }
        .frame(height: height)
    }
}
