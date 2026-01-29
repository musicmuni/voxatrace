import SwiftUI

/// Real-time pitch feedback during practice.
struct PitchFeedbackCard: View {
    let pitch: Float
    let progress: Float
    let noteName: String

    var body: some View {
        VStack(spacing: 8) {
            HStack {
                Text("Pitch: \(String(format: "%.1f", pitch)) Hz")
                Spacer()
                Text(noteName)
                    .font(.title2)
                    .fontWeight(.bold)
            }
            ProgressView(value: Double(progress))
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .cornerRadius(8)
    }
}
