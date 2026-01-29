import SwiftUI

/// Navigation controls for segment traversal.
struct NavigationControls: View {
    let canGoPrevious: Bool
    let canGoNext: Bool
    let canRetry: Bool
    let onPrevious: () -> Void
    let onRetry: () -> Void
    let onNext: () -> Void

    var body: some View {
        HStack(spacing: 12) {
            Button(action: onPrevious) {
                Label("Prev", systemImage: "chevron.left")
            }
            .disabled(!canGoPrevious)

            Button(action: onRetry) {
                Label("Retry", systemImage: "arrow.counterclockwise")
            }
            .disabled(!canRetry)

            Button(action: onNext) {
                Label("Next", systemImage: "chevron.right")
            }
            .disabled(!canGoNext)
        }
        .buttonStyle(.bordered)
    }
}

/// Session-level action buttons.
struct SessionControls: View {
    let onReset: () -> Void
    let onFinish: () -> Void

    var body: some View {
        HStack(spacing: 12) {
            Button("Start Over", action: onReset)
                .buttonStyle(.bordered)
                .frame(maxWidth: .infinity)

            Button("Finish", action: onFinish)
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)
        }
    }
}

/// Practice start/stop button.
struct PracticeButton: View {
    let isPracticing: Bool
    let onStart: () -> Void
    let onStop: () -> Void

    var body: some View {
        if isPracticing {
            Button("Stop", action: onStop)
                .buttonStyle(.borderedProminent)
                .tint(.red)
                .frame(maxWidth: .infinity)
        } else {
            Button("Start Practice", action: onStart)
                .buttonStyle(.borderedProminent)
                .frame(maxWidth: .infinity)
        }
    }
}
