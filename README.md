# VolumeTile

A minimal Android Quick Settings tile that pops up a media volume slider when tapped.

## How it works

Tapping the **Volume** tile in your Quick Settings panel collapses the shade and immediately shows a floating slider overlay. Drag to adjust media volume. The overlay auto-dismisses after 3 seconds of inactivity, or tap the background to close it instantly.

The volume icon updates dynamically:

| Icon | State |
|------|-------|
| 🔇 | Muted (0) |
| 🔉 | Low (below 50%) |
| 🔊 | Medium / high |

## Setup

1. Clone and open the project in Android Studio.
2. Build and install on your device (Run → Run 'app').
3. Add the tile: swipe down twice to open Quick Settings → tap the pencil/edit icon → find **Volume** → drag it into your active tiles.
4. Tap the tile anytime to bring up the slider.

## Tech details

- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 36
- **Zero external dependencies** — pure Android SDK only
- Two files: `MainActivity.kt` (the overlay) and `VolumeTileService.kt` (the tile)
- No view binding, no Jetpack, no Compose

## Permissions

`MODIFY_AUDIO_SETTINGS` — granted automatically at install, required to change stream volume.
