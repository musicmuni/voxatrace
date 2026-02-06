---
sidebar_position: 3
---

# iOS Quickstart

Build a simple pitch detector in 5 minutes.

## What You'll Build

A minimal SwiftUI app that:
1. Records audio from the microphone
2. Detects pitch in real-time
3. Displays the detected note

## Prerequisites

- Xcode 14 or later
- VoxaTrace installed (see [Installation](./installation))
- Microphone permission configured in Info.plist

## Step 1: Configure Info.plist

Add microphone usage description:

```xml
<key>NSMicrophoneUsageDescription</key>
<string>We need microphone access to detect pitch from your voice.</string>
```

## Step 2: Create the Pitch Detector View

```swift
import SwiftUI
import VoxaTrace

struct PitchDetectorView: View {
    @StateObject private var viewModel = PitchDetectorViewModel()

    var body: some View {
        VStack(spacing: 20) {
            Text(viewModel.note)
                .font(.system(size: 72, weight: .bold))

            Text("\(Int(viewModel.frequency)) Hz")
                .font(.title2)

            ProgressView(value: viewModel.confidence)
                .frame(width: 200)

            Button(viewModel.isRecording ? "Stop" : "Start") {
                if viewModel.isRecording {
                    viewModel.stop()
                } else {
                    Task {
                        await viewModel.start()
                    }
                }
            }
            .buttonStyle(.borderedProminent)
        }
        .padding()
    }
}
```

## Step 3: Create the View Model

```swift
import Foundation
import VoxaTrace
import Combine

@MainActor
class PitchDetectorViewModel: ObservableObject {
    @Published var note: String = "--"
    @Published var frequency: Float = 0
    @Published var confidence: Float = 0
    @Published var isRecording: Bool = false

    private var recorder: SonixRecorder?
    private var detector: CalibraPitch.Detector?
    private var task: Task<Void, Never>?

    func start() async {
        // Create recorder with voice settings
        recorder = SonixRecorder.companion.createTemporary(
            config: SonixRecorderConfig.companion.VOICE
        )

        // Create pitch detector
        detector = CalibraPitch.companion.createDetector()

        // Start recording
        recorder?.start()
        isRecording = true

        // Process audio buffers
        task = Task {
            guard let recorder = recorder else { return }

            for await buffer in recorder.audioBuffersStream() {
                guard !Task.isCancelled else { break }

                let samples = buffer.toFloatArray()
                guard let point = detector?.detect(samples: samples, sampleRate: Int32(buffer.sampleRate)) else {
                    continue
                }

                await MainActor.run {
                    if point.pitch > 0 {
                        self.note = pitchToNote(point.pitch)
                        self.frequency = point.pitch
                        self.confidence = point.confidence
                    } else {
                        self.note = "--"
                        self.frequency = 0
                        self.confidence = 0
                    }
                }
            }
        }
    }

    func stop() {
        task?.cancel()
        task = nil
        recorder?.stop()
        recorder?.release()
        recorder = nil
        detector?.close()
        detector = nil
        isRecording = false
    }

    private func pitchToNote(_ frequency: Float) -> String {
        let noteNames = ["C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"]
        let a4: Float = 440.0
        let semitones = 12 * log2(frequency / a4) + 69
        let noteIndex = (Int(semitones) % 12 + 12) % 12
        let octave = Int(semitones) / 12 - 1
        return "\(noteNames[noteIndex])\(octave)"
    }

    deinit {
        stop()
    }
}
```

## What You'll See

When you sing into the microphone, the console will show:

```text
Note: A4, Frequency: 440 Hz, Confidence: 92%
Note: A4, Frequency: 441 Hz, Confidence: 89%
Note: B4, Frequency: 494 Hz, Confidence: 87%
Note: --, Frequency: 0 Hz, Confidence: 0%   ← breath/silence
Note: C5, Frequency: 523 Hz, Confidence: 91%
```

The app is:

1. Recording audio buffers from the microphone (~50ms chunks)
2. Running pitch detection on each buffer
3. Converting frequency to musical note name
4. Showing confidence (how certain the detection is)

**Troubleshooting:**

- Seeing lots of `--` entries? Make sure microphone permission is granted in Settings
- Low confidence values? Sing closer to the device, reduce background noise
- App crashes on launch? Check that `NSMicrophoneUsageDescription` is in Info.plist

## Step 4: Request Microphone Permission

The system will automatically prompt for permission when you start recording. However, you can request it proactively:

```swift
import AVFoundation

func requestMicrophonePermission() async -> Bool {
    await withCheckedContinuation { continuation in
        AVAudioSession.sharedInstance().requestRecordPermission { granted in
            continuation.resume(returning: granted)
        }
    }
}
```

Use it in your view:

```swift
struct PitchDetectorView: View {
    @StateObject private var viewModel = PitchDetectorViewModel()
    @State private var hasPermission = false

    var body: some View {
        Group {
            if hasPermission {
                // ... pitch detector UI
            } else {
                Button("Grant Microphone Access") {
                    Task {
                        hasPermission = await requestMicrophonePermission()
                    }
                }
            }
        }
        .task {
            hasPermission = AVAudioSession.sharedInstance().recordPermission == .granted
        }
    }
}
```

## Complete UIKit Example

If you're using UIKit:

```swift
import UIKit
import VoxaTrace

class PitchDetectorViewController: UIViewController {
    private let noteLabel = UILabel()
    private let frequencyLabel = UILabel()
    private let confidenceBar = UIProgressView()

    private var recorder: SonixRecorder?
    private var detector: CalibraPitch.Detector?
    private var task: Task<Void, Never>?

    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        Task {
            await startPitchDetection()
        }
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        stopPitchDetection()
    }

    private func setupUI() {
        view.backgroundColor = .systemBackground

        noteLabel.font = .systemFont(ofSize: 72, weight: .bold)
        noteLabel.textAlignment = .center
        noteLabel.text = "--"

        frequencyLabel.font = .systemFont(ofSize: 24)
        frequencyLabel.textAlignment = .center
        frequencyLabel.text = "0 Hz"

        confidenceBar.progressViewStyle = .default

        let stack = UIStackView(arrangedSubviews: [noteLabel, frequencyLabel, confidenceBar])
        stack.axis = .vertical
        stack.spacing = 16
        stack.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(stack)
        NSLayoutConstraint.activate([
            stack.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            stack.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            confidenceBar.widthAnchor.constraint(equalToConstant: 200)
        ])
    }

    private func startPitchDetection() async {
        recorder = SonixRecorder.companion.createTemporary(
            config: SonixRecorderConfig.companion.VOICE
        )
        detector = CalibraPitch.companion.createDetector()

        recorder?.start()

        task = Task {
            guard let recorder = recorder else { return }

            for await buffer in recorder.audioBuffersStream() {
                guard !Task.isCancelled else { break }

                let samples = buffer.toFloatArray()
                guard let point = detector?.detect(samples: samples, sampleRate: Int32(buffer.sampleRate)) else {
                    continue
                }

                await MainActor.run {
                    if point.pitch > 0 {
                        self.noteLabel.text = self.pitchToNote(point.pitch)
                        self.frequencyLabel.text = "\(Int(point.pitch)) Hz"
                        self.confidenceBar.progress = point.confidence
                    } else {
                        self.noteLabel.text = "--"
                        self.frequencyLabel.text = "0 Hz"
                        self.confidenceBar.progress = 0
                    }
                }
            }
        }
    }

    private func stopPitchDetection() {
        task?.cancel()
        recorder?.stop()
        recorder?.release()
        detector?.close()
    }

    private func pitchToNote(_ frequency: Float) -> String {
        let noteNames = ["C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"]
        let a4: Float = 440.0
        let semitones = 12 * log2(frequency / a4) + 69
        let noteIndex = (Int(semitones) % 12 + 12) % 12
        let octave = Int(semitones) / 12 - 1
        return "\(noteNames[noteIndex])\(octave)"
    }
}
```

## Next Steps

- [Detecting Pitch Guide](../guides/detecting-pitch) - Deep dive into pitch detection options
- [Recording Audio Guide](../guides/recording-audio) - Learn about recording features
- [Live Evaluation Guide](../guides/live-evaluation) - Score singing against reference
