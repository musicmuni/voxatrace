//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.internal.platform](index.md)/[platformFileSystem](platform-file-system.md)

# platformFileSystem

[common]\
expect val [platformFileSystem](platform-file-system.md): FileSystem

Platform-specific FileSystem access.

Okio's FileSystem.SYSTEM is only available in platform actuals (JVM/Native), not in commonMain. This expect/actual bridges that gap.

#### See also

| | |
|---|---|
|  | <a href="https://github.com/square/okio/discussions/1115">Okio Discussion #1115</a> |

[android, ios]\
[android, ios]\
actual val [platformFileSystem](platform-file-system.md): FileSystem
