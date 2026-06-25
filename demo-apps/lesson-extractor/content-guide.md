# Preparing lesson content

This guide is for **content creators**: you organize each lesson's files and hand
them to whoever runs the extractor (see [Running the extractor](operations-guide.md)).
No coding required.

## One folder per lesson

Put each lesson in its own folder. **The folder name is the lesson id** (your app
refers to the bundle by this name), so name it clearly. Inside each folder, put
three files: an audio file, a `.csv`, and a `.meta.json`.

```
inputs/
  raag-bhairavi-aroh-avroh/
      raag-bhairavi-aroh-avroh.mp3         # the reference audio
      raag-bhairavi-aroh-avroh.csv         # phrase markers
      raag-bhairavi-aroh-avroh.meta.json   # tonic + raga
  devamanohari-jatiswara/
      ...
```

The file names inside a folder can be anything; the extractor finds them by type
(`.wav`/`.mp3`, `.csv`, `.meta.json`). See `examples/inputs/` for two complete,
working lessons you can copy.

## 1. Audio (`.wav` or `.mp3`)

Any sample rate, mono or stereo, any bit depth. The extractor down-mixes to mono
and resamples to 16 kHz internally, so hand off your natural recordings as-is.

## 2. Phrase markers (`.csv`)

One phrase per line, **four comma-separated fields**:

```
startSeconds,type,durationSeconds,sargam
```

- `startSeconds` / `durationSeconds` — the phrase window, in seconds.
- `type` — `1` for a teacher/reference phrase, `2` for a student-response window
  (see singalong vs singafter below).
- `sargam` — the phrase's svaras. Write them out (use spaces to group as you
  like); `^` marks an upper octave on the preceding svara, `_` a lower octave
  (`^^` / `__` for two octaves). In the finished bundle these render as the
  traditional dot above/below the svara (e.g. `S^` → `Ṡ`).

Example (`raag-bhairavi-aroh-avroh.csv`, a singalong):
```
0.121905,1,8.878730,S r g m P d n S^
9.081905,1,9.589841,S^ n d P m g r S
```

## 3. Singalong vs singafter

- **Singalong**: the student sings *along with* the reference. Every row is a
  teacher phrase (`type` `1`). Set `lessonType` to `singalong`.
  (`raag-bhairavi-aroh-avroh` is a singalong.)
- **Singafter**: call-and-response — the teacher sings, then the student repeats.
  Each teacher phrase (`type` `1`) is followed by a student-response window
  (`type` `2`) with the **same sargam**; the `2` row's window is where the student
  is expected to sing back. Set `lessonType` to `singafter`.
  (`devamanohari-jatiswara` is a singafter.)

Example (`devamanohari-jatiswara.csv`, a singafter — note the paired `1`/`2` rows):
```
2.070000,1,5.940000,S^ R^M^R^ S^NDNP MRMP
9.810000,2,5.940000,S^ R^M^R^ S^NDNP MRMP
```

## 4. Tonic + raga (`.meta.json`)

Set the tonic **per lesson** (lessons can be in different keys). Provide it as a
note label via `shruti`, or directly in Hz via `keyHz`:

```json
{
  "shruti": "A3",
  "lessonType": "singalong",
  "svaraMask": [true, true, false, true, false, true, false, true, true, false, true, false]
}
```

| Field | Required | Notes |
|-------|----------|-------|
| `shruti` | one of shruti/keyHz | Tonic as a note label, e.g. `"A3"`, `"G#3"` (sharps `#`, flats `b`) |
| `keyHz` | one of shruti/keyHz | Tonic directly in Hz, e.g. `220.0` |
| `lessonType` | no | `"singalong"` (default) or `"singafter"` |
| `svaraMask` | no | The raga's svara set — see below |
| `bpm` | no | Tempo, for metered lessons |
| `beatsPerMeasure` | no | For metered lessons |

### `svaraMask` — the raga's svara set

A JSON array of `true`/`false`, one entry per svara position, marking which
svaras the raga uses. When you provide it, the bundle gains per-note **svara
transcription** (each phrase's notes are labelled S, R, G, …); without it,
phrases carry their window and sargam text but no note-level labels.

Use **either** 12 entries (one per semitone) **or** 16 entries (Carnatic
swarasthanas), in this fixed order (index 0 = Sa):

- **12-entry:** `S r R g G m M P d D n N`
- **16-entry:** `S R1 R2 R3 G1 G2 G3 M1 M2 P D1 D2 D3 N1 N2 N3`

Set `true` for each svara the lesson's scale uses, `false` otherwise. The
`raag-bhairavi-aroh-avroh` example uses the 7-svara set `S r g m P d n` (12-entry):

```json
"svaraMask": [true, true, false, true, false, true, false, true, true, false, true, false]
```

(Reading against `S r R g G m M P d D n N`: `S`,`r`,`g`,`m`,`P`,`d`,`n` true.) The
`devamanohari-jatiswara` example uses a 16-entry Carnatic mask. Omit `svaraMask`
entirely if you don't need per-note labels.
