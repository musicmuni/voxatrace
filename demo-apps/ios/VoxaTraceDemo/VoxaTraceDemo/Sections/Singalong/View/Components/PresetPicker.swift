import SwiftUI

/// Dropdown picker for session presets.
struct PresetPicker: View {
    let selectedPreset: SessionPreset
    let onSelect: (SessionPreset) -> Void

    var body: some View {
        Menu {
            ForEach(SessionPreset.allCases) { preset in
                Button {
                    onSelect(preset)
                } label: {
                    VStack(alignment: .leading) {
                        Text(preset.rawValue)
                        Text(preset.description)
                            .font(.caption)
                    }
                }
            }
        } label: {
            HStack(spacing: 4) {
                Text(selectedPreset.rawValue)
                    .font(.subheadline)
                Image(systemName: "chevron.down")
                    .font(.caption2)
            }
            .padding(.horizontal, 10)
            .padding(.vertical, 6)
            .background(Color(.tertiarySystemBackground))
            .cornerRadius(8)
        }
    }
}
