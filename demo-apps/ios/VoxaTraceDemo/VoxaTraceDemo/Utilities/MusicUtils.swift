import Foundation

/// Shared music/pitch conversion utilities
enum MusicUtils {
    /// Convert frequency in Hz to MIDI note name (e.g., 440 Hz -> "A4")
    /// - Parameter pitchHz: Frequency in Hz
    /// - Returns: Note name string, or "-" if invalid
    static func getMidiNoteName(_ pitchHz: Float) -> String {
        guard pitchHz > 0 else { return "-" }
        let midiNote = Int(round(69 + 12 * log2(pitchHz / 440.0)))
        let noteNames = ["C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"]
        let octave = (midiNote / 12) - 1
        let noteName = noteNames[midiNote % 12]
        return "\(noteName)\(octave)"
    }

    /// Format time in seconds to minutes:seconds.milliseconds (e.g., 65.5 -> "1:05.50")
    /// - Parameter seconds: Time in seconds
    /// - Returns: Formatted time string
    static func formatTime(_ seconds: Float) -> String {
        let mins = Int(seconds) / 60
        let secs = Int(seconds) % 60
        let ms = Int((seconds - Float(Int(seconds))) * 100)
        return String(format: "%d:%02d.%02d", mins, secs, ms)
    }

    /// Convert frequency in Hz to MIDI note number
    /// - Parameter pitchHz: Frequency in Hz
    /// - Returns: MIDI note number (60 = C4), or -1 if invalid
    static func hzToMidi(_ pitchHz: Float) -> Int {
        guard pitchHz > 0 else { return -1 }
        return Int(round(69 + 12 * log2(pitchHz / 440.0)))
    }

    /// Convert MIDI note number to frequency in Hz
    /// - Parameter midiNote: MIDI note number (60 = C4)
    /// - Returns: Frequency in Hz
    static func midiToHz(_ midiNote: Int) -> Float {
        return 440.0 * pow(2.0, Float(midiNote - 69) / 12.0)
    }
}

// MARK: - File Utilities

/// Copy a bundle asset to a writable file location.
/// - Parameters:
///   - name: Name of the resource (without extension)
///   - ext: File extension
/// - Returns: Path to the copied file, or nil if copy failed
func copyAssetToFile(name: String, ext: String) -> String? {
    guard let bundleURL = Bundle.main.url(forResource: name, withExtension: ext) else {
        return nil
    }

    let tempDir = FileManager.default.temporaryDirectory
    let destURL = tempDir.appendingPathComponent("\(name).\(ext)")

    // Remove existing file if present
    try? FileManager.default.removeItem(at: destURL)

    do {
        try FileManager.default.copyItem(at: bundleURL, to: destURL)
        return destURL.path
    } catch {
        Log.e(.general, "Failed to copy asset '\(name).\(ext)'", error: error)
        return nil
    }
}
