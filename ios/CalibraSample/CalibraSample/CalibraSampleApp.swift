import SwiftUI

// Timing: capture when this file is first loaded
let appLoadTime = Date()

// Helper to print timestamps with high precision
func logTiming(_ message: String) {
    let elapsed = Date().timeIntervalSince(appLoadTime)
    print("⏱️ [\(String(format: "%.3f", elapsed))s] \(message)")
}

@main
struct CalibraSampleApp: App {
    init() {
        logTiming("CalibraSampleApp.init() START")
        // Note: Kotlin/Native framework initialization happens when Swift first
        // accesses any type from the calibra module. This typically occurs during
        // SwiftUI view body evaluation when section views are instantiated.
        logTiming("CalibraSampleApp.init() END")
    }

    var body: some Scene {
        logTiming("CalibraSampleApp.body START")
        return WindowGroup {
            ContentView()
                .onAppear {
                    logTiming("ContentView.onAppear (UI is now visible)")
                }
        }
    }
}
