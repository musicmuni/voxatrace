import Foundation

/// Local performance labels derived from a 0...1 score.
///
/// Score verdicts (`PerformanceLevel`, per-result `feedbackMessage`) were removed
/// from the SDK public API in ADR-023; verdicts are the app's responsibility now.
/// The demo computes its own labels here so all sections stay consistent. Mirrors
/// the Android demo's `util/ScoreLabels.kt`.
enum ScoreLabels {

    /// Short performance label (e.g. for the overall-score header).
    static func performanceLabel(_ score: Float) -> String {
        switch score {
        case ..<0:    return "N/A"
        case 0.9...:  return "Excellent"
        case 0.8...:  return "Great"
        case 0.7...:  return "Good"
        case 0.5...:  return "Fair"
        default:      return "Keep practicing"
        }
    }

    /// Fuller feedback message (e.g. for a per-segment result card).
    static func performanceFeedback(_ score: Float) -> String {
        switch score {
        case ..<0:    return "N/A"
        case 0.9...:  return "Excellent! Perfect performance."
        case 0.8...:  return "Great job! Almost perfect."
        case 0.7...:  return "Good job! Keep practicing."
        case 0.5...:  return "Not bad. Focus on pitch accuracy."
        default:      return "Keep practicing! Match each phrase carefully."
        }
    }
}
