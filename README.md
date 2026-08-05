# Alight Motion Editor

## Native C++ OpenGL Motion Graphics Android App

### Tech Stack
- **Language:** Kotlin + Native C++ (NDK)
- **UI:** Jetpack Compose + Material 3
- **Rendering:** OpenGL ES 2.0
- **Codec:** MediaCodec NDK
- **Audio:** OpenSLES
- **Export:** MediaCodec H.264/265 + MediaMuxer
- **No Backend Required** — 100% Local Processing

### Architecture
| Module | Files | Description |
|---|---|---|
| Native C++ | 12 files | GL renderer, 16 effects, particles, media decoder, audio, Figma, SDF |
| Kotlin Engine | 4 files | Keyframe, export, native bridge, models |
| Editor UI | 2 files | Full editor with timeline, properties, effects, Figma, export |
| Import/Export | 2 files | Figma importer, project save/load |

### Features
- 10 Layer Types (Video, Image, Text, Shape, Null, Camera, Adjustment, Group, Figma, Audio)
- 16 GPU-Accelerated Effects (Blur, Glow, Shadow, Chroma Key, Distortion, Noise...)
- 8 Easing Curves (Linear, Ease, Bounce, Elastic, Back)
- FPS 1-240, Resolution 144p-4K
- 25+ Import Formats
- Export MP4 H.264/265

### Build
```
Android Studio → Open → Sync Gradle → Run
```

