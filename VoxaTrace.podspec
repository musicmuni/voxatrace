Pod::Spec.new do |spec|
  spec.name         = "VoxaTrace"
  spec.version      = "3.0.1"
  spec.summary      = "Acoustic intelligence for voice apps: real-time pitch, intonation analysis, voice metrics, singing evaluation. On-device, cross-platform."
  spec.description  = <<-DESC
    Acoustic intelligence for voice apps: real-time pitch, intonation analysis, voice metrics, singing evaluation. On-device, cross-platform.
  DESC

  spec.homepage     = "https://github.com/musicmuni/voxatrace"
  spec.license      = { :type => "Commercial License", :file => "LICENSE" }
  spec.author       = { "MusicMuni" => "support@musicmuni.com" }

  spec.ios.deployment_target = "15.0"
  spec.swift_versions = ["5.9"]

  spec.source = {
    :http => "https://github.com/musicmuni/voxatrace/releases/download/voxatrace-v3.0.1/voxatrace.xcframework.zip"
  }

  spec.vendored_frameworks = "VoxaTrace.xcframework"
  spec.frameworks = "AVFoundation", "AudioToolbox", "CoreAudio", "Accelerate"
end
