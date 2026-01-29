import Foundation
import os.log

/// Centralized logging for VoxaTrace Demo app.
///
/// Uses os_log for proper system integration:
/// - Logs appear in Console.app with "VoxaTraceDemo" subsystem
/// - Debug logs filtered in release builds
/// - Proper log levels for filtering
enum Log {
    private static let subsystem = "com.musicmuni.VoxaTraceDemo"

    // Category-specific loggers
    private static let playback = OSLog(subsystem: subsystem, category: "Playback")
    private static let recording = OSLog(subsystem: subsystem, category: "Recording")
    private static let mixer = OSLog(subsystem: subsystem, category: "Mixer")
    private static let session = OSLog(subsystem: subsystem, category: "Session")
    private static let general = OSLog(subsystem: subsystem, category: "General")

    /// Log debug info (filtered in release builds)
    static func d(_ category: Category, _ message: String) {
        os_log(.debug, log: category.osLog, "%{public}s", message)
    }

    /// Log informational message
    static func i(_ category: Category, _ message: String) {
        os_log(.info, log: category.osLog, "%{public}s", message)
    }

    /// Log warning
    static func w(_ category: Category, _ message: String) {
        os_log(.default, log: category.osLog, "[WARN] %{public}s", message)
    }

    /// Log error with optional error object
    static func e(_ category: Category, _ message: String, error: Error? = nil) {
        if let error = error {
            os_log(.error, log: category.osLog, "%{public}s: %{public}s", message, error.localizedDescription)
        } else {
            os_log(.error, log: category.osLog, "%{public}s", message)
        }
    }

    enum Category {
        case playback
        case recording
        case mixer
        case session
        case general

        var osLog: OSLog {
            switch self {
            case .playback: return Log.playback
            case .recording: return Log.recording
            case .mixer: return Log.mixer
            case .session: return Log.session
            case .general: return Log.general
            }
        }
    }
}
