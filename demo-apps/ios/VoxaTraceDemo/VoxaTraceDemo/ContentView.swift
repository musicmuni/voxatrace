import SwiftUI
import AVFoundation
import VoxaTrace

// MARK: - Navigation Types

enum NavigationDestination: Hashable {
    case sonix
    case calibra
    case sonixFeature(String, ApiMode)
    case calibraFeature(String)
}

enum ApiMode: Hashable {
    case simple
    case advanced
}

// MARK: - Content View (Navigation Router)

struct ContentView: View {
    @State private var path: [NavigationDestination] = []

    var body: some View {
        NavigationStack(path: $path) {
            HomeView(onSelect: { destination in
                path.append(destination)
            })
            .navigationDestination(for: NavigationDestination.self) { dest in
                switch dest {
                case .sonix:
                    SonixMenuView(onSelect: { feature, mode in
                        path.append(.sonixFeature(feature, mode))
                    })
                case .calibra:
                    CalibraMenuView(onSelect: { feature in
                        path.append(.calibraFeature(feature))
                    })
                case .sonixFeature(let name, let mode):
                    SonixDetailView(featureName: name, apiMode: mode)
                case .calibraFeature(let name):
                    CalibraDetailView(featureName: name)
                }
            }
        }
        .onAppear {
            AVAudioSession.sharedInstance().requestRecordPermission { _ in }
        }
    }
}

// MARK: - Home View

struct HomeView: View {
    let onSelect: (NavigationDestination) -> Void

    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                CategoryCard(
                    icon: "waveform.path.ecg",
                    title: "Sonix",
                    subtitle: "Audio Transport",
                    description: "Playback, Recording, MIDI, Metronome, Multi-Track",
                    color: .blue,
                    onTap: { onSelect(.sonix) }
                )

                CategoryCard(
                    icon: "waveform",
                    title: "Calibra",
                    subtitle: "Audio Analysis",
                    description: "Pitch Detection, Voice Analysis, Singing Evaluation",
                    color: .purple,
                    onTap: { onSelect(.calibra) }
                )
            }
            .padding()
        }
        .navigationTitle("VoxaTrace Demo")
    }
}

struct CategoryCard: View {
    let icon: String
    let title: String
    let subtitle: String
    let description: String
    let color: Color
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            VStack(alignment: .leading, spacing: 12) {
                HStack {
                    Image(systemName: icon)
                        .font(.system(size: 32))
                        .foregroundColor(color)
                    Spacer()
                    Image(systemName: "chevron.right")
                        .foregroundColor(.secondary)
                }

                VStack(alignment: .leading, spacing: 4) {
                    Text(title)
                        .font(.title2)
                        .fontWeight(.semibold)
                        .foregroundColor(.primary)
                    Text(subtitle)
                        .font(.subheadline)
                        .foregroundColor(color)
                }

                Text(description)
                    .font(.caption)
                    .foregroundColor(.secondary)
                    .lineLimit(2)
            }
            .padding()
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(Color(.secondarySystemBackground))
            .cornerRadius(16)
        }
        .buttonStyle(.plain)
    }
}

// MARK: - Sonix Menu View

struct SonixMenuView: View {
    @State private var apiMode: ApiMode = .simple
    let onSelect: (String, ApiMode) -> Void

    private var visibleFeatures: [(String, String, String)] {
        var features = [
            ("Playback", "play.circle.fill", "Audio playback with pitch shifting"),
            ("Recording", "mic.circle.fill", "Audio recording to M4A/MP3"),
            ("Multi-Track", "square.stack.3d.up.fill", "Multi-track mixing"),
            ("Metronome", "metronome.fill", "Click track with visual feedback"),
            ("MIDI Synthesis", "pianokeys", "SoundFont-based synthesis"),
        ]
        if apiMode == .advanced {
            features.append(("Decoding", "doc.text", "Audio decode/encode"))
            features.append(("Parser", "doc.plaintext", "Parse notation files"))
        }
        return features
    }

    var body: some View {
        List {
            Section {
                VStack(alignment: .leading, spacing: 8) {
                    Text("API Mode")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                    Picker("API Mode", selection: $apiMode) {
                        Text("Simple").tag(ApiMode.simple)
                        Text("Advanced").tag(ApiMode.advanced)
                    }
                    .pickerStyle(.segmented)

                    Text(apiMode == .simple
                         ? "Recommended for most apps"
                         : "Full control with callbacks")
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
                .padding(.vertical, 4)
            }

            Section {
                ForEach(visibleFeatures, id: \.0) { feature in
                    Button {
                        onSelect(feature.0, apiMode)
                    } label: {
                        FeatureRow(icon: feature.1, title: feature.0, description: feature.2)
                    }
                }
            }
        }
        .navigationTitle("Sonix")
    }
}

// MARK: - Calibra Menu View

struct CalibraMenuView: View {
    let onSelect: (String) -> Void

    private let analysisFeatures = [
        ("Pitch", "waveform", "Pitch detection & processing"),
        ("VAD", "mic.fill", "Voice activity detection"),
        ("Breath Monitor", "lungs.fill", "Measure breath duration"),
        ("Vocal Range", "arrow.up.and.down", "Detect your vocal range"),
        ("Speaking Pitch", "tuningfork", "Speaking pitch (shruti)"),
    ]

    private let realtimeEvalFeatures = [
        ("Singalong Live", "music.note.list", "Sing along evaluation"),
        ("Singafter Live", "repeat", "Call and response"),
    ]

    private let offlineEvalFeatures = [
        ("Melody Eval", "waveform.path", "Melody matching"),
        ("Note Eval", "list.number", "Note accuracy"),
    ]

    var body: some View {
        List {
            Section(header: Text("Analysis")) {
                ForEach(analysisFeatures, id: \.0) { feature in
                    Button {
                        onSelect(feature.0)
                    } label: {
                        FeatureRow(icon: feature.1, title: feature.0, description: feature.2)
                    }
                }
            }

            Section(header: Text("Realtime Evaluation")) {
                ForEach(realtimeEvalFeatures, id: \.0) { feature in
                    Button {
                        onSelect(feature.0)
                    } label: {
                        FeatureRow(icon: feature.1, title: feature.0, description: feature.2)
                    }
                }
            }

            Section(header: Text("Offline Evaluation")) {
                ForEach(offlineEvalFeatures, id: \.0) { feature in
                    Button {
                        onSelect(feature.0)
                    } label: {
                        FeatureRow(icon: feature.1, title: feature.0, description: feature.2)
                    }
                }
            }
        }
        .navigationTitle("Calibra")
    }
}

// MARK: - Feature Row

struct FeatureRow: View {
    let icon: String
    let title: String
    let description: String

    var body: some View {
        HStack(spacing: 16) {
            Image(systemName: icon)
                .font(.title2)
                .foregroundColor(.accentColor)
                .frame(width: 32)
            VStack(alignment: .leading) {
                Text(title)
                    .font(.headline)
                    .foregroundColor(.primary)
                Text(description)
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
            Spacer()
            Image(systemName: "chevron.right")
                .foregroundColor(.secondary)
                .font(.caption)
        }
        .padding(.vertical, 4)
    }
}

// MARK: - Sonix Detail View

struct SonixDetailView: View {
    let featureName: String
    let apiMode: ApiMode

    var body: some View {
        ScrollView {
            sectionContent
                .padding()
        }
        .navigationTitle(featureName)
        .navigationBarTitleDisplayMode(.inline)
    }

    @ViewBuilder
    private var sectionContent: some View {
        switch featureName {
        case "Playback":
            if apiMode == .simple {
                PlaybackSectionSimplified()
            } else {
                PlaybackSection()
            }
        case "Recording":
            if apiMode == .simple {
                RecordingSectionSimplified()
            } else {
                RecordingSection()
            }
        case "Multi-Track":
            if apiMode == .simple {
                MultiTrackSectionSimplified()
            } else {
                MultiTrackSection()
            }
        case "Metronome":
            if apiMode == .simple {
                MetronomeSectionSimplified()
            } else {
                MetronomeSection()
            }
        case "MIDI Synthesis":
            if apiMode == .simple {
                MidiSectionSimplified()
            } else {
                MidiSection()
            }
        case "Decoding":
            DecodingSection()
        case "Parser":
            ParserSection()
        default:
            Text("Unknown Sonix section: \(featureName)")
        }
    }
}

// MARK: - Calibra Detail View

struct CalibraDetailView: View {
    let featureName: String

    var body: some View {
        ScrollView {
            sectionContent
                .padding()
        }
        .navigationTitle(featureName)
        .navigationBarTitleDisplayMode(.inline)
    }

    @ViewBuilder
    private var sectionContent: some View {
        switch featureName {
        case "Pitch":
            PitchSection()
        case "VAD":
            VADSection()
        case "Breath Monitor":
            BreathMonitorSection()
        case "Vocal Range":
            VocalRangeSection()
        case "Speaking Pitch":
            SpeakingPitchDetectorSection()
        case "Singalong Live":
            SingalongSection()
        case "Singafter Live":
            SingafterLiveEvalSection()
        case "Melody Eval":
            MelodyEvalSection()
        case "Note Eval":
            NoteEvalSection()
        default:
            Text("Unknown Calibra section: \(featureName)")
        }
    }
}

// MARK: - Section Card (for inline display)

struct SectionCard<Content: View>: View {
    let content: () -> Content

    init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }

    var body: some View {
        content()
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(16)
            .background(Color(.secondarySystemBackground))
            .cornerRadius(12)
    }
}

// MARK: - Preview

#Preview {
    ContentView()
}
