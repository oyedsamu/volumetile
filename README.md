# VolumeTile

Two minimal Android Quick Settings tiles — a **Volume** slider and a **Screenshot** button.

## Tiles

### 🔊 Volume
Tapping the tile collapses the Quick Settings shade and shows a floating slider overlay. Drag to adjust media volume. The overlay auto-dismisses after 3 seconds of inactivity, or tap the background to close it instantly.

The volume icon updates dynamically:

| Icon | State |
|------|-------|
| 🔇 | Muted (0) |
| 🔉 | Low (below 50%) |
| 🔊 | Medium / high |

### 📸 Screenshot
Tapping the tile takes a screenshot instantly using Android's built-in screenshot action.

**First-time setup:** the tile will open Accessibility Settings so you can enable the *VolumeTile Screenshot* accessibility service once. After that, one tap = one screenshot.

## Setup

1. Clone and open the project in Android Studio.
2. Build and install on your device (**Run → Run 'app'**).
3. Add tiles: swipe down twice → tap the pencil/edit icon → find **Volume** and/or **Screenshot** → drag them into your active tiles.
4. For the Screenshot tile, enable the accessibility service when prompted.

## Tech details

- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 36
- **Zero external dependencies** — pure Android SDK only
- 4 source files, no view binding, no Jetpack, no Compose

## Permissions

| Permission | Why |
|---|---|
| `MODIFY_AUDIO_SETTINGS` | Set media stream volume |
| `BIND_ACCESSIBILITY_SERVICE` | Take screenshots via `GLOBAL_ACTION_TAKE_SCREENSHOT` (API 28+) |
