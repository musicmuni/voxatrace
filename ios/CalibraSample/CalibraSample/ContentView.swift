import SwiftUI
import AVFoundation

struct ContentView: View {
    @State private var selectedSection: String? = nil

    init() {
        logTiming("ContentView.init()")
    }

    var body: some View {
        logTiming("ContentView.body START")
        return Group {
            if let section = selectedSection {
                DetailView(sectionName: section, onBack: { selectedSection = nil })
            } else {
                MenuView(onSelect: { selectedSection = $0 })
            }
        }
    }
}

// MARK: - Menu View (Home Screen)

struct MenuView: View {
    let onSelect: (String) -> Void

    private let sections = [
        ("Realtime Pitch", "waveform", "Real-time pitch detection"),
        ("VAD", "mic.fill", "Voice activity detection"),
        ("Breath Monitor", "lungs.fill", "Measure breath duration"),
        ("Vocal Range", "arrow.up.and.down", "Detect your vocal range"),
        ("Speaking Pitch", "tuningfork", "Detect your speaking pitch"),
        ("Singalong", "music.note.list", "Sing along practice"),
        ("Singafter", "repeat", "Call and response practice"),
        ("Effects", "slider.horizontal.3", "Audio effects"),
        ("Offline Analysis", "doc.text.magnifyingglass", "Analyze audio files")
    ]

    init(onSelect: @escaping (String) -> Void) {
        self.onSelect = onSelect
        logTiming("MenuView.init()")
    }

    var body: some View {
        logTiming("MenuView.body START")
        return NavigationView {
            List {
                ForEach(sections, id: \.0) { section in
                    Button {
                        onSelect(section.0)
                    } label: {
                        HStack(spacing: 16) {
                            Image(systemName: section.1)
                                .font(.title2)
                                .foregroundColor(.accentColor)
                                .frame(width: 32)
                            VStack(alignment: .leading) {
                                Text(section.0)
                                    .font(.headline)
                                    .foregroundColor(.primary)
                                Text(section.2)
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                            }
                        }
                        .padding(.vertical, 4)
                    }
                }
            }
            .navigationTitle("Calibra Sample")
        }
        .navigationViewStyle(.stack)
        .onAppear {
            logTiming("MenuView.onAppear (menu is now visible)")
            AVAudioSession.sharedInstance().requestRecordPermission { _ in }
        }
    }
}

// MARK: - Detail View

struct DetailView: View {
    let sectionName: String
    let onBack: () -> Void

    init(sectionName: String, onBack: @escaping () -> Void) {
        self.sectionName = sectionName
        self.onBack = onBack
        logTiming("DetailView.init(\(sectionName))")
    }

    var body: some View {
        logTiming("DetailView.body START for \(sectionName)")
        return NavigationView {
            ScrollView {
                sectionContent
                    .padding()
            }
            .navigationTitle(sectionName)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("Back") { onBack() }
                }
            }
        }
        .navigationViewStyle(.stack)
        .onAppear {
            logTiming("DetailView.onAppear for \(sectionName)")
        }
    }

    @ViewBuilder
    private var sectionContent: some View {
        switch sectionName {
        case "Realtime Pitch":
            let _ = logTiming("Creating RealtimePitchSection")
            RealtimePitchSection()
        case "VAD":
            let _ = logTiming("Creating VADSection")
            VADSection()
        case "Breath Monitor":
            let _ = logTiming("Creating BreathMonitorSection")
            BreathMonitorSection()
        case "Vocal Range":
            let _ = logTiming("Creating VocalRangeSection")
            VocalRangeSection()
        case "Speaking Pitch":
            let _ = logTiming("Creating SpeakingPitchDetectorSection")
            SpeakingPitchDetectorSection()
        case "Singalong":
            let _ = logTiming("Creating SingalongPracticeSection")
            SingalongPracticeSection()
        case "Singafter":
            let _ = logTiming("Creating SingafterPracticeSection")
            SingafterPracticeSection()
        case "Effects":
            let _ = logTiming("Creating EffectsSection")
            EffectsSection()
        case "Offline Analysis":
            let _ = logTiming("Creating OfflineAnalysisSection")
            OfflineAnalysisSection()
        default:
            Text("Unknown section")
        }
    }
}

// MARK: - Preview

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
