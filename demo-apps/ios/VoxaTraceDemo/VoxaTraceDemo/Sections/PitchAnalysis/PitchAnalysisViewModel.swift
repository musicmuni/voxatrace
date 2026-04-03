import Foundation
import Combine
import VoxaTrace

private let MAX_SEGMENTS_DISPLAY = 50

/// ViewModel for Pitch Analysis — folded histogram and tonal segment labeling.
@MainActor
final class PitchAnalysisViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var isAnalyzing = false
    @Published private(set) var histogramBins: [Float] = []
    @Published private(set) var histogramValues: [Float] = []
    @Published private(set) var tonalSegments: [TonalSegmentUi] = []

    // MARK: - Types

    struct TonalSegmentUi: Identifiable {
        let id = UUID()
        let label: String
        let startTime: Float
        let endTime: Float
    }

    // MARK: - Actions

    func analyzeOffline() {
        isAnalyzing = true
        histogramBins = []
        histogramValues = []
        tonalSegments = []

        Task {
            guard let audioURL = Bundle.main.url(forResource: "Alankaar 01_voice", withExtension: "m4a"),
                  let audioData = SonixDecoder.decode(path: audioURL.path) else {
                await MainActor.run { isAnalyzing = false }
                return
            }

            let extractor = PitchDetection.createContourExtractor()
            let contour = extractor.extract(audio: audioData.samples, sampleRate: audioData.sampleRate)
            extractor.release()

            let tonicHz: Float = 146.83

            // Folded histogram: 240 bins across one octave (5-cent resolution)
            let config = HistogramConfig.Builder()
                .foldOctaves(true)
                .numBins(240)
                .build()
            let histogram = PitchAnalysis.computeHistogram(contour: contour, tonicHz: tonicHz, config: config)

            // Label by mean pitch, then merge consecutive same-label segments
            let intervals = MusicTheory.eqTemperedIntervalsCents.map { Float($0) }
            let rawSegments = PitchAnalysis.labelByMeanPitch(
                contour: contour,
                tonicHz: tonicHz,
                targetIntervalsCents: intervals
            )

            let bins = histogram.binCenters
            let vals = histogram.values

            // Merge consecutive segments with same label
            var merged: [TonalSegmentUi] = []
            for seg in rawSegments {
                let label = seg.label ?? "?"
                if let last = merged.last, last.label == label {
                    merged[merged.count - 1] = TonalSegmentUi(label: label, startTime: last.startTime, endTime: seg.endSeconds)
                } else {
                    merged.append(TonalSegmentUi(label: label, startTime: seg.startSeconds, endTime: seg.endSeconds))
                }
            }
            let segs = Array(merged.prefix(MAX_SEGMENTS_DISPLAY))

            await MainActor.run {
                histogramBins = bins
                histogramValues = vals
                tonalSegments = segs
                isAnalyzing = false
            }
        }
    }
}
