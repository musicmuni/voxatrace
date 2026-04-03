import Foundation
import VoxaTrace

/// ViewModel for Pitch Analysis - histogram and tonal segment labeling.
///
/// ## VoxaTrace Integration
/// ```swift
/// // 1. Decode audio and extract contour
/// let audioData = SonixDecoder.decode(path: filePath)
/// let extractor = PitchDetection.createContourExtractor()
/// let contour = extractor.extract(audio: samples, sampleRate: sampleRate)
/// extractor.close()
///
/// // 2. Compute histogram
/// let histogram = PitchAnalysis.computeHistogram(contour: contour, tonicHz: 261.63)
///
/// // 3. Label tonal segments
/// let segments = PitchAnalysis.labelByMeanPitch(contour: contour, tonicHz: 261.63,
///     intervals: MusicTheory.eqTemperedIntervalsCents.map { Float($0) })
/// ```
@MainActor
final class PitchAnalysisViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var isAnalyzing = false
    @Published private(set) var histogramBinCenters: [Float] = []
    @Published private(set) var histogramValues: [Float] = []
    @Published private(set) var tonalSegments: [TonalSegmentUi] = []

    // MARK: - Types

    struct TonalSegmentUi: Identifiable {
        let id = UUID()
        let label: String
        let startTime: Float
        let endTime: Float

        var duration: Float { endTime - startTime }
    }

    // MARK: - Actions

    func analyzeOffline() {
        isAnalyzing = true
        histogramBinCenters = []
        histogramValues = []
        tonalSegments = []

        Task {
            guard let audioURL = Bundle.main.url(forResource: "Alankaar 01_voice", withExtension: "m4a"),
                  let audioData = SonixDecoder.decode(path: audioURL.path) else {
                await MainActor.run { isAnalyzing = false }
                return
            }

            // Extract pitch contour
            let extractor = PitchDetection.createContourExtractor()
            let contour = extractor.extract(audio: audioData.samples, sampleRate: audioData.sampleRate)
            extractor.close()

            let tonicHz: Float = 261.63

            // Compute histogram
            let histogram = PitchAnalysis.computeHistogram(contour: contour, tonicHz: tonicHz)

            // Label tonal segments
            let intervals = MusicTheory.eqTemperedIntervalsCents.map { Float($0) }
            let segments = PitchAnalysis.labelByMeanPitch(
                contour: contour,
                tonicHz: tonicHz,
                intervals: intervals
            )

            var bins: [Float] = []
            var vals: [Float] = []
            var segs: [TonalSegmentUi] = []

            if let histogram = histogram {
                bins = Array(histogram.binCenters)
                vals = Array(histogram.values)
            }

            if let segments = segments {
                segs = segments.map { seg in
                    TonalSegmentUi(
                        label: seg.label,
                        startTime: seg.startTime,
                        endTime: seg.endTime
                    )
                }
            }

            await MainActor.run {
                histogramBinCenters = bins
                histogramValues = vals
                tonalSegments = segs
                isAnalyzing = false
            }
        }
    }
}
