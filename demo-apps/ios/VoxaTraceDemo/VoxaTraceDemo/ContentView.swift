import SwiftUI
import AVFoundation
import VoxaTrace

// MARK: - Navigation Types

enum NavigationDestination: Hashable {
    case sonix
    case tona
    case accura
    case tessera
    case calibra
    case feature(String)
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
                    SonixMenuView(onSelect: { path.append(.feature($0)) })
                case .tona:
                    ModuleMenuView(title: "Tona", features: tonaFeatures, onSelect: { path.append(.feature($0)) })
                case .accura:
                    ModuleMenuView(title: "Accura", features: accuraFeatures, onSelect: { path.append(.feature($0)) })
                case .tessera:
                    ModuleMenuView(title: "Tessera", features: tesseraFeatures, onSelect: { path.append(.feature($0)) })
                case .calibra:
                    CalibraMenuView(onSelect: { path.append(.feature($0)) })
                case .feature(let name):
                    FeatureDetailView(featureName: name)
                }
            }
        }
        .onAppear {
            AVAudioSession.sharedInstance().requestRecordPermission { _ in }
        }
    }
}

// MARK: - Feature Lists

private let tonaFeatures: [(String, String, String)] = [
    ("Pitch", "waveform", "Pitch detection & processing"),
    ("Pitch Analysis", "chart.bar", "Histogram & tonal segments"),
]

private let accuraFeatures: [(String, String, String)] = [
    ("Intonation", "tuningfork", "Pitch accuracy (EQ/JI)"),
]

private let tesseraFeatures: [(String, String, String)] = [
    ("Breath Monitor", "lungs.fill", "Breath capacity & control"),
    ("Vocal Agility", "hare", "Agility scoring"),
    ("Vocal Range", "arrow.up.and.down", "Detect your vocal range"),
    ("Song Matching", "music.note.list", "Range-based song matching"),
    ("Speaking Pitch", "tuningfork", "Natural speaking pitch"),
    ("Voice Profile", "person.crop.circle", "Multi-metric dashboard"),
]

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
                    title: "Tona",
                    subtitle: "Pitch Detection & Processing",
                    description: "Realtime Detection, Contour Extraction, Cleanup, Analysis",
                    color: .orange,
                    onTap: { onSelect(.tona) }
                )

                CategoryCard(
                    icon: "tuningfork",
                    title: "Accura",
                    subtitle: "Intonation Analysis",
                    description: "Pitch accuracy scoring (Equal Temperament / Just Intonation)",
                    color: .green,
                    onTap: { onSelect(.accura) }
                )

                CategoryCard(
                    icon: "person.wave.2",
                    title: "Tessera",
                    subtitle: "Voice Metrics",
                    description: "Breath, Agility, Range, Speaking Pitch, Voice Profile",
                    color: .teal,
                    onTap: { onSelect(.tessera) }
                )

                CategoryCard(
                    icon: "music.note",
                    title: "Calibra",
                    subtitle: "Singing Evaluation",
                    description: "Singalong, Singafter, Melody Eval, Note Eval, VAD",
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
                        .fontWeight(.bold)
                        .foregroundColor(.primary)
                    Text(subtitle)
                        .font(.subheadline)
                        .foregroundColor(color)
                    Text(description)
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
            }
            .padding()
            .background(Color(.secondarySystemBackground))
            .cornerRadius(16)
        }
        .buttonStyle(.plain)
    }
}

// MARK: - Sonix Menu View

struct SonixMenuView: View {
    let onSelect: (String) -> Void

    private let features = [
        ("Playback", "play.circle.fill", "Audio playback with pitch shifting"),
        ("Recording", "mic.circle.fill", "Audio recording to M4A/MP3"),
        ("Multi-Track", "square.stack.3d.up.fill", "Multi-track mixing"),
        ("Metronome", "metronome.fill", "Click track with visual feedback"),
        ("MIDI Synthesis", "pianokeys", "SoundFont-based synthesis"),
        ("Decoding", "doc.text", "Audio decode/encode"),
        ("Parser", "doc.plaintext", "Parse notation files"),
    ]

    var body: some View {
        List {
            Section {
                ForEach(features, id: \.0) { feature in
                    Button {
                        onSelect(feature.0)
                    } label: {
                        FeatureRow(icon: feature.1, title: feature.0, description: feature.2)
                    }
                }
            }
        }
        .navigationTitle("Sonix")
    }
}

// MARK: - Generic Module Menu View (Tona, Accura, Tessera)

struct ModuleMenuView: View {
    let title: String
    let features: [(String, String, String)]
    let onSelect: (String) -> Void

    var body: some View {
        List {
            Section {
                ForEach(features, id: \.0) { feature in
                    Button {
                        onSelect(feature.0)
                    } label: {
                        FeatureRow(icon: feature.1, title: feature.0, description: feature.2)
                    }
                }
            }
        }
        .navigationTitle(title)
    }
}

// MARK: - Calibra Menu View

struct CalibraMenuView: View {
    let onSelect: (String) -> Void

    private let analysisFeatures = [
        ("VAD", "mic.fill", "Voice activity detection"),
        // ("Effects", "slider.horizontal.3", "Audio effects processing"), // disabled: CalibraEffects is internal until effects are public-ready
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

// MARK: - Unified Feature Detail View

struct FeatureDetailView: View {
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
        // Sonix
        case "Playback":
            PlaybackView()
        case "Recording":
            RecordingView()
        case "Multi-Track":
            MultiTrackView()
        case "Metronome":
            MetronomeView()
        case "MIDI Synthesis":
            MIDIView()
        case "Decoding":
            DecodingView()
        case "Parser":
            ParserView()
        // Tona
        case "Pitch":
            PitchSection()
        case "Pitch Analysis":
            PitchAnalysisView()
        // Accura
        case "Intonation":
            IntonationView()
        // Tessera
        case "Breath Monitor":
            BreathMonitorView()
        case "Vocal Agility":
            AgilityView()
        case "Vocal Range":
            VocalRangeView()
        case "Song Matching":
            SongMatchingView()
        case "Speaking Pitch":
            SpeakingPitchView()
        case "Voice Profile":
            VoiceProfileView()
        // Calibra
        case "VAD":
            VADSection()
        // case "Effects":
        //     EffectsView() // disabled: CalibraEffects is internal until effects are public-ready
        case "Singalong Live":
            SingalongSection()
        case "Singafter Live":
            SingafterView()
        case "Melody Eval":
            MelodyEvalView()
        case "Note Eval":
            NoteEvalView()
        default:
            Text("Unknown section: \(featureName)")
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
