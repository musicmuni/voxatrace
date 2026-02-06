package com.musicmuni.voxatrace.demo.sections.vad.model

import com.musicmuni.voxatrace.calibra.model.VADBackend

/**
 * VAD threshold constants.
 */
object VADConstants {
    const val noSingingThreshold: Float = 0.3f
    const val partialSingingThreshold: Float = 0.6f
}

/**
 * Sample data for waveform visualization.
 */
data class WaveformSample(
    val amplitude: Float,
    val isVoice: Boolean
)

/**
 * Backend information for display.
 */
data class VADBackendInfo(
    val backend: VADBackend,
    val name: String,
    val description: String,
    val guidance: String,
    val latency: String
) {
    companion object {
        val all = listOf(
            VADBackendInfo(
                backend = VADBackend.SPEECH,
                name = "Speech",
                description = "Silero neural network",
                guidance = "Best for: Conversations, podcasts, voice commands",
                latency = "~32ms"
            ),
            VADBackendInfo(
                backend = VADBackend.GENERAL,
                name = "General",
                description = "RMS-based detection",
                guidance = "Best for: Simple detection, no ML dependency needed",
                latency = "~1ms"
            ),
            VADBackendInfo(
                backend = VADBackend.SINGING_REALTIME,
                name = "Singing RT",
                description = "SwiftF0 pitch-based",
                guidance = "Best for: Karaoke, singing games, low-latency apps",
                latency = "~32ms"
            )
        )

        fun info(backend: VADBackend): VADBackendInfo {
            return all.find { it.backend == backend } ?: all[0]
        }
    }
}
