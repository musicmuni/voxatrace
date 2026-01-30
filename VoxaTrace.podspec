Pod::Spec.new do |spec|
  spec.name         = "VoxaTrace"
  spec.version      = "2025.129.1000"
  spec.summary      = "Cross-platform audio SDK for music applications"
  spec.description  = <<-DESC
    VoxaTrace provides real-time pitch detection, audio playback with pitch shifting,
    recording, multi-track mixing, metronome, and MIDI synthesis.
  DESC

  spec.homepage     = "https://github.com/musicmuni/voxatrace"
  spec.license      = { :type => "Commercial", :file => "LICENSE" }
  spec.author       = { "MusicMuni" => "support@musicmuni.com" }

  spec.ios.deployment_target = "15.0"
  spec.swift_versions = ["5.9"]

  spec.source = {
    :http => "https://github.com/musicmuni/voxatrace/releases/download/voxatrace-v#{spec.version}/voxatrace.xcframework.zip"
  }

  spec.vendored_frameworks = "VoxaTrace.xcframework"
  spec.frameworks = "AVFoundation", "AudioToolbox", "CoreAudio", "Accelerate"
end
