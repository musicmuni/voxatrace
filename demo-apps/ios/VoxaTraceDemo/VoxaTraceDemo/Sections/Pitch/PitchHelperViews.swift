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
