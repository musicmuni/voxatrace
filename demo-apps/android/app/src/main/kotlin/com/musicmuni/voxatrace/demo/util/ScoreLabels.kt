package com.musicmuni.voxatrace.demo.util

/**
 * Local performance labels derived from a 0..1 score.
 *
 * Score verdicts (`PerformanceLevel`, per-result `feedbackMessage`) were removed
 * from the SDK public API in ADR-023; verdicts are the app's responsibility now.
 * The demo computes its own labels here so all sections stay consistent.
 */
fun performanceLabel(score: Float): String = when {
    score < 0f -> "N/A"
    score >= 0.9f -> "Excellent"
    score >= 0.8f -> "Great"
    score >= 0.7f -> "Good"
    score >= 0.5f -> "Fair"
    else -> "Keep practicing"
}

/** Local feedback message derived from a 0..1 score. */
fun performanceFeedback(score: Float): String = when {
    score < 0f -> "N/A"
    score >= 0.9f -> "Excellent! Perfect performance."
    score >= 0.8f -> "Great job! Almost perfect."
    score >= 0.7f -> "Good job! Keep practicing."
    score >= 0.5f -> "Not bad. Focus on pitch accuracy."
    else -> "Keep practicing! Match each phrase carefully."
}
