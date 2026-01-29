import Foundation
import Combine
import VoxaTrace

/// ViewModel for MIDI synthesis using SonixMidiSynthesizer.Builder.
///
/// ## VoxaTrace Integration (~15 lines)
/// ```swift
/// // 1. Create synthesizer with Builder pattern
/// let synth = SonixMidiSynthesizer.Builder()
///     .soundFontPath(path: sfPath)
///     .sampleRate(rate: sampleRate)
///     .build()
///
/// // 2. Synthesize
/// let success = synth.synthesizeFromNotes(notes: notes, outputPath: outputPath)
///
/// // 3. Play result with SonixPlayer
/// player = try await SonixPlayer.create(source: outputPath, audioSession: .playback)
/// ```
@MainActor
final class MIDIViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var status = "Ready"
    @Published private(set) var isSynthesizing = false
    @Published private(set) var isPlaying = false
    @Published private(set) var synthesizerVersion: String?
    @Published var selectedSampleRate: Int32 = 44100

    // MARK: - Private

    private var outputFile: URL?
    private var player: SonixPlayer?
    private var playingTask: Task<Void, Never>?

    let sampleRates: [Int32] = [44100, 48000]

    // MARK: - Lifecycle

    func onDisappear() {
        playingTask?.cancel()
        player?.release()
    }

    // MARK: - Actions

    func synthesize() {
        Task {
            isSynthesizing = true
            status = "Synthesizing (\(selectedSampleRate / 1000)kHz)..."

            do {
                guard let sfPath = copyAssetToFile(name: "harmonium", ext: "sf2") else {
                    status = "Soundfont not found"
                    isSynthesizing = false
                    return
                }

                // C major scale (timing in milliseconds)
                let notes: [MidiNote] = [
                    MidiNote(note: 60, startTime: 0, endTime: 400),     // C4
                    MidiNote(note: 62, startTime: 500, endTime: 900),   // D4
                    MidiNote(note: 64, startTime: 1000, endTime: 1400), // E4
                    MidiNote(note: 65, startTime: 1500, endTime: 1900), // F4
                    MidiNote(note: 67, startTime: 2000, endTime: 2400), // G4
                    MidiNote(note: 69, startTime: 2500, endTime: 2900), // A4
                    MidiNote(note: 71, startTime: 3000, endTime: 3400), // B4
                    MidiNote(note: 72, startTime: 3500, endTime: 4400), // C5
                ]

                let tempDir = FileManager.default.temporaryDirectory
                let outFile = tempDir.appendingPathComponent("midi_output.wav")

                // Builder API: Advanced configuration
                let synth = SonixMidiSynthesizer.Builder()
                    .soundFontPath(path: sfPath)
                    .sampleRate(rate: selectedSampleRate)
                    .build()

                synthesizerVersion = synth.version

                let success = synth.synthesizeFromNotes(
                    notes: notes,
                    outputPath: outFile.path
                )

                if success {
                    let attrs = try FileManager.default.attributesOfItem(atPath: outFile.path)
                    let fileSize = (attrs[.size] as? Int64) ?? 0
                    outputFile = outFile
                    status = "Generated (\(selectedSampleRate / 1000)kHz): \(fileSize / 1024) KB"

                    await loadPlayer(path: outFile.path)
                } else {
                    status = "Synthesis failed"
                }
            } catch {
                status = "Error: \(error.localizedDescription)"
            }

            isSynthesizing = false
        }
    }

    func play() {
        player?.play()
    }

    func stop() {
        player?.stop()
    }

    // MARK: - Computed Properties

    var canPlay: Bool {
        outputFile != nil && !isSynthesizing && !isPlaying
    }

    // MARK: - Private Methods

    private func loadPlayer(path: String) async {
        playingTask?.cancel()
        player?.release()

        do {
            let newPlayer = try await SonixPlayer.create(source: path, audioSession: .playback)
            player = newPlayer
            playingTask = newPlayer.observeIsPlaying { [weak self] playing in
                self?.isPlaying = playing
            }
        } catch {
            status = "Player error: \(error.localizedDescription)"
        }
    }
}
