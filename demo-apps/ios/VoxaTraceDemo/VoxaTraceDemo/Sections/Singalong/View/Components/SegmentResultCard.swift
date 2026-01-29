import SwiftUI
import VoxaTrace

/// Card displaying segment evaluation result with pitch graph.
struct SegmentResultCard: View {
    let result: SegmentResult

    var body: some View {
        VStack(spacing: 8) {
            // Score header
            HStack {
                Text("Segment \(result.segment.index + 1)")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                Spacer()
                Text("\(Int(result.score * 100))%")
                    .font(.title3)
                    .fontWeight(.bold)
                    .foregroundColor(scoreColor)
                Text(result.feedbackMessage)
                    .font(.caption)
                    .foregroundColor(.secondary)
            }

            // Pitch comparison graph
            let refData = result.referencePitchData
            let stdData = result.studentPitchData

            if !refData.pitchesMidi.isEmpty || !stdData.pitchesMidi.isEmpty {
                PitchGraphView(
                    contoursWithTimes: [
                        (times: refData.times, pitches: refData.pitchesMidi, color: .blue, label: "Reference"),
                        (times: stdData.times, pitches: stdData.pitchesMidi, color: .orange, label: "You")
                    ],
                    title: nil,
                    height: 120,
                    inputIsMidi: true
                )
            }
        }
        .padding()
        .background(Color(.secondarySystemBackground))
        .cornerRadius(12)
    }

    private var scoreColor: Color {
        if result.score >= 0.8 { return .green }
        if result.score >= 0.6 { return .orange }
        return .red
    }
}
