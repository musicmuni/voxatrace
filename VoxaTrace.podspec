Pod::Spec.new do |spec|
  spec.name         = "VoxaTrace"
  spec.version      = "0.9.2"
  spec.summary      = "Cross-platform audio SDK for music applications"
  spec.description  = <<-DESC
    Cross-platform audio SDK for music applications
  DESC

  spec.homepage     = "https://github.com/musicmuni/voxatrace"
  spec.license      = { :type => "Commercial License", :file => "LICENSE" }
  spec.author       = { "MusicMuni" => "support@musicmuni.com" }

  spec.ios.deployment_target = "15.0"
  spec.swift_versions = ["5.9"]

  spec.source = {
    :http => "https://github.com/musicmuni/voxatrace/releases/download/voxatrace-v0.9.2/voxatrace.xcframework.zip"
  }

  spec.vendored_frameworks = "VoxaTrace.xcframework"
  spec.frameworks = "AVFoundation", "AudioToolbox", "CoreAudio", "Accelerate"
end
