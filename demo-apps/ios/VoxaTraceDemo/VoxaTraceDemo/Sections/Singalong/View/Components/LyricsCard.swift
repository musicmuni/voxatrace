import SwiftUI

/// Card displaying current segment lyrics.
struct LyricsCard: View {
    let lyrics: String

    var body: some View {
        Text(lyrics)
            .font(.body)
            .padding(12)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(Color.blue.opacity(0.1))
            .cornerRadius(8)
    }
}
