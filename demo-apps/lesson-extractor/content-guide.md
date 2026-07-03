# Preparing lesson content

This guide is for **content creators**: you organize each lesson's files and hand
them to whoever runs the extractor (see [Running the extractor](operations-guide.md)).
No coding required.

## One folder per lesson

Put each lesson in its own folder, with three files inside: an audio file, a
`.csv`, and a `.meta.json`. Name the folder whatever you like; the extractor
doesn't read the name, it just processes every folder that contains those three
files. The output bundle is written to a folder of the same name, so pick
something you'll recognize downstream.

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
startSeconds,type,durationSeconds,label
```

- `startSeconds` / `durationSeconds` — the phrase window, in seconds.
- `type` — `1` for a teacher/reference phrase, `2` for a student-response window
  (see singalong vs singafter below).
- `label` — free text associated with the phrase, for your app to display:
  sargam, Western notation, lyrics, whatever you want. It's stored verbatim.
  As a convenience for sargam, `^` marks an upper octave on the preceding token
  and `_` a lower one (`^^` / `__` for two octaves), rendering as the traditional
  dot above/below in the bundle (e.g. `S^` → `Ṡ`). If you'd rather use your own
  symbols, just type the final text directly and it passes through unchanged.

Example (`raag-bhairavi-aroh-avroh.csv`, a singalong):
```
0.121905,1,8.878730,S r g m P d n S^
9.081905,1,9.589841,S^ n d P m g r S
```

## 3. Singalong vs singafter

`lessonType` in the `.meta.json` (next section) decides how the lesson is
treated. It is the switch — not the row types.

- **Singalong** (`lessonType: "singalong"`, the default): the student sings
  *along with* the reference. Every row is a teacher phrase (`type` `1`).
  (`raag-bhairavi-aroh-avroh` is a singalong.)
- **Singafter** (`lessonType: "singafter"`): call-and-response — the teacher
  sings, then the student repeats. Each teacher phrase (`type` `1`) gets a
  student-response window. You can mark it explicitly with a `type` `2` row
  (same label) right after the teacher row, or omit type-2 rows and let the
  extractor derive each window: the gap after the phrase, or — for the last
  phrase — a window of the phrase's own length. Every derivation is reported in
  the run summary. If a phrase has no room for a window (phrases are back-to-back,
  or a last phrase has no trailing audio) it is skipped with a warning.
  (`devamanohari-jatiswara` is a singafter, with explicit type-2 rows.)

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
  "genre": "hindustani",
  "lessonType": "singalong",
  "svaras": ["S", "r", "g", "m", "P", "d", "n"]
}
```

| Field | Required | Notes |
|-------|----------|-------|
| `shruti` | one of shruti/keyHz | Tonic as a note label, e.g. `"A3"`, `"G#3"` (sharps `#`, flats `b`) |
| `keyHz` | one of shruti/keyHz | Tonic directly in Hz, e.g. `220.0` |
| `genre` | with `svaras` | `"hindustani"` or `"carnatic"` — see below |
| `svaras` | with `genre` | The raga's svaras — see below |
| `lessonType` | no | `"singalong"` (default) or `"singafter"` |
| `soloVoice` | no | `true` (default) for a solo voice recording; `false` if it has accompaniment — see below |
| `bpm` | no | Tempo, for metered lessons |
| `beatsPerMeasure` | no | For metered lessons |

### `genre` + `svaras` — the raga's svara set

To get per-note **svara transcription** in the bundle (each phrase's notes
labelled S, R, G, …), list the raga's svaras by name and declare the genre.
Provide the two together, or omit both (then phrases carry their window and
label text but no note-level labels).

Write the svaras in the genre's notation:

- **`"hindustani"`** — `S r R g G m M P d D n N`. Letter **case matters**: lower
  case is komal (`r` `g` `d` `n`) and `m` is shuddha madhyam; upper case is
  shuddha (`R` `G` `D` `N`) and `M` is tivra madhyam.
- **`"carnatic"`** — `S R1 R2 R3 G1 G2 G3 M1 M2 P D1 D2 D3 N1 N2 N3`.

List only the svaras the raga uses, in any order. The
`raag-bhairavi-aroh-avroh` example (Bhairavi, 7 svaras):

```json
"genre": "hindustani",
"svaras": ["S", "r", "g", "m", "P", "d", "n"]
```

The `devamanohari-jatiswara` example uses `"genre": "carnatic"` with Carnatic
names (`R2`, `M1`, …). An unknown name for the genre is reported as an error, so
typos surface instead of producing wrong labels.

### `soloVoice` — solo or accompanied recording

Declare what is *in* the recording, so the extractor picks the right pitch
tracker:

- **`true` (the default)** — a lone voice (reverb is fine). This is the usual
  case for teacher reference recordings.
- **`false`** — the voice is accompanied (tanpura, tabla, harmonium, a backing
  track, a metronome bell, …).

Getting this wrong shows up as a bad pitch contour: a solo recording processed
as accompanied loses its quieter notes (holes in the contour and missing notes
in the transcription); an accompanied recording processed as solo can track the
accompaniment instead of the voice.
