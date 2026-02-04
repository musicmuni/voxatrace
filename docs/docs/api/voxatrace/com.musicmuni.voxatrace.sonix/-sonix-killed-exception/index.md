---
sidebar_label: "SonixKilledException"
---


# SonixKilledException

[common]\
typealias [SonixKilledException](index.md) = [VoxaTraceKilledException](../../com.musicmuni.voxatrace.exceptions/-voxa-trace-killed-exception/index.md)

Exception thrown when API key is invalid or license has been revoked.

This is a type alias for [VoxaTraceKilledException](../../com.musicmuni.voxatrace.exceptions/-voxa-trace-killed-exception/index.md) for backward compatibility.

This exception is thrown by factory methods (SonixPlayer.create(), etc.) when:

- 
   The API key doesn't exist (401 Unauthorized)
- 
   The API key has been revoked (403 Forbidden)

To recover, contact support@musicmuni.com to resolve license issues.
