# MusiCar2

MusiCar2 is a lightweight Java Swing application to organize and export music files in a precise playback order for USB/SD devices (for example, car players). It lets you browse a folder, pick tracks one-by-one (or shuffle them), edit their display names, reorder the playlist, and export renamed copies into a single folder.

## Key Features

- Browse and list supported audio files: MP3, WMA, WAV, FLAC, M4A
- Add tracks to an ordered playlist with sequential numbering (e.g. `001 - song.mp3`)
- Reorder tracks (move up / move down) and remove entries
- Edit track display names before export
- Shuffle folder contents and rebuild an ordered playlist
- Export renamed copies into `%USERPROFILE%\Music\Ordered`
- Pure Java implementation for file operations (no external tools required)

## Prerequisites

- Java 8 or later (JRE/JDK)
- Optional: Apache Ant or NetBeans for building from source

This project was edited on Windows; the application creates an `Ordered` folder under the current user's Music directory by default.

## Build and Run

You can use the included batch scripts or run the project from your IDE.

Windows (included scripts):

```powershell
compile.bat
run.bat
```

Using Ant:

```powershell
ant -f build.xml
```

From an IDE (NetBeans): open the project and run the main class `musicar2.FramePrincipal`.

If you prefer to run manually, ensure compiled classes are in `build/classes` and resources (Images) are on the classpath.

## Quick Usage

1. Launch the application.
2. Click `Browse Folder` and select the folder that contains your music files.
3. Double-click an item in the left list to add it to the ordered list (or use the `>> Add` button).
4. Reorder items with `↑ Up` and `↓ Down`, or remove them with `Remove`.
5. Edit a display name in the "Edit song name" field and click `Update Name`.
6. Click `Export to "Ordered" Folder` to copy renamed files to `%USERPROFILE%\Music\Ordered`.

Notes:
- The application preserves original file extensions when copying.
- If the edited name already contains an extension, the app removes it before adding the original extension to avoid duplicates.

## Localization and Code Notes

- Visible strings and comments were translated to English in this revision.
- GUI form metadata (`src/musicar2/FramePrincipal.form`) was updated for English labels.
- Identifier names (method/variable names) left mostly unchanged to avoid breaking generated GUI code. Consider refactoring identifiers for full English consistency.

## Contributing

Suggestions for improvement:

- Extract UI strings into resource bundles for proper localization.
- Refactor identifiers to a consistent English naming convention.
- Add unit tests for file-copying and name-handling logic.

Please open an issue or send a pull request with proposed changes.

## License

No license file is included. Add a license if you plan to distribute the software.

---

If you want, I can also:

- Run a quick scan for any remaining Spanish text in other files
- Refactor variable/method names to English (this will be more invasive)
- Prepare a localized resource bundle for future translations

Would you like me to finalize the README in a specific format (e.g., adding screenshots or example runs)?

