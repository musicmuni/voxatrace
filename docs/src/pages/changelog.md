---
title: Changelog
---

# Changelog

All notable changes to VoxaTrace will be documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/),
and this project adheres to [Semantic Versioning](https://semver.org/).

## Latest: 2.0.0 (2026-05-22)

Major release. VoxaTrace 2.0.0 introduces four new public namespaces:
`tona` (pitch detection / processing / analysis), `tessera` (voice
metrics), `accura` (intonation scoring), and `common.MusicTheory`.

Part of this is reorganization: calibra functionality that was never about
singing evaluation moved to its permanent home (pitch to `tona`, voice
metrics to `tessera`, music theory to `common`), and the legacy 1.x
calibra facades (`CalibraPitch`, `CalibraBreath`, `CalibraVocalRange`,
`VocalRangeSession`, `CalibraSpeakingPitch`, `CalibraMusic`) are removed.

But most of the new surface is genuinely new functionality that did not
exist in 1.x in any form: the entire `Accura` facade, the entire
`PitchAnalysis` facade, `TesseraAgility`, the multi-metric
`Tessera.analyze` / `TesseraSession`, the batch-shape
`TesseraRange.computeVocalRange` / `computeSearchVector` / `computeMatch`,
and `MusicTheory.deriveUserShruti`.

Calibra retains the four singing-evaluation facades (`CalibraLiveEval`,
`CalibraMelodyEval`, `CalibraNoteEval`, `CalibraVAD`). See the full entry,
breaking-change list, and migration table in the canonical changelog.

---

*Full changelog (all versions, with migration notes): [CHANGELOG.md](https://github.com/musicmuni/voxatrace/blob/main/CHANGELOG.md)*
