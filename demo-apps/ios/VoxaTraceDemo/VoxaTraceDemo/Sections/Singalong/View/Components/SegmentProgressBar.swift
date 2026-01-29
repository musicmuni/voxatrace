import SwiftUI

/// Progress bar showing all segments with completion status.
struct SegmentProgressBar: View {
    let segmentCount: Int
    let currentIndex: Int
    let completedIndices: Set<Int>
    let isDisabled: Bool
    let onSegmentTap: (Int) -> Void

    var body: some View {
        VStack(spacing: 4) {
            HStack(spacing: 4) {
                ForEach(0..<segmentCount, id: \.self) { index in
                    SegmentChip(
                        index: index,
                        isActive: index == currentIndex,
                        isCompleted: completedIndices.contains(index)
                    ) {
                        onSegmentTap(index)
                    }
                    .disabled(isDisabled)
                }
            }
            Text("Segment \(currentIndex + 1) of \(segmentCount)")
                .font(.caption)
                .foregroundColor(.secondary)
        }
    }
}

/// Individual segment indicator chip.
struct SegmentChip: View {
    let index: Int
    let isActive: Bool
    let isCompleted: Bool
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            ZStack {
                Circle()
                    .fill(backgroundColor)
                    .frame(width: 28, height: 28)

                if isCompleted {
                    Image(systemName: "checkmark")
                        .font(.caption2)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                } else {
                    Text("\(index + 1)")
                        .font(.caption2)
                        .fontWeight(isActive ? .bold : .regular)
                        .foregroundColor(isActive ? .white : .primary)
                }
            }
        }
        .buttonStyle(.plain)
        .overlay(
            Circle()
                .stroke(isActive ? Color.blue : Color.clear, lineWidth: 2)
                .frame(width: 32, height: 32)
        )
    }

    private var backgroundColor: Color {
        if isCompleted { return .green }
        if isActive { return .blue }
        return Color(.tertiarySystemBackground)
    }
}
