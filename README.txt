================================================================================
================================================================================
MUSICAR2 v1.0 - NEW VERSION WITH MANUAL SELECTION
================================================================================

🎯 MAIN GOAL:
Allow selecting songs ONE BY ONE from your library,
edit their display names,
keep them in an exact order,
and export them to an "Ordered" folder so they play in the desired order
from a USB or SD card in your car.

================================================================================
NEW INTERFACE - 3 PANELS:
================================================================================

LEFT PANEL - MUSIC LIBRARY
─────────────────────────────────────────
• "Browse Folder" button
  → Opens a dialog to select your music folder
  → Automatically loads MP3, WMA, WAV, FLAC, M4A files
  → Displays a list of available songs

CENTER PANEL - CONTROL BUTTONS
──────────────────────────────────
• >> Add
  → Select a song from the left library
  → Adds it to the selected list (right panel)
  → Assigns a sequential number (001, 002, 003...)

RIGHT PANEL - SELECTED SONGS
──────────────────────────────────────
• Shows all songs in playback order
• Format: "001 - Song Name.mp3"
• Select a song to edit its display name

ORDER BUTTONS:
• ↑ Up   → Move the song up in the order
• ↓ Down → Move the song down in the order
• Remove → Remove the song from the list

NAME EDITOR:
• Text field to edit the display name
• "Update Name" button → Saves the change
• The field is populated automatically when a song is selected

PRIMARY BUTTON:
• ✓ Export to "Ordered" Folder
  → Copies all files to: C:\Users\[YourUser]\Music\Ordered
  → Names files sequentially: 001 - name.mp3, 002 - name.mp3...
  → Preserves original file extensions
  → Creates the folder automatically if it doesn't exist

================================================================================
WORKFLOW - STEP BY STEP:
================================================================================

STEP 1: LOAD LIBRARY
├─ Start the application: java -cp "build\classes;src" musicar2.FramePrincipal
├─ Click "Browse Folder"
├─ Navigate to your music folder
└─ See the available songs listed in the left panel

STEP 2: ADD SONGS (ONE BY ONE)
├─ Select a song in the left panel
├─ Click ">> Add"
├─ The song appears in the right list with a number (001 - ...)
├─ Repeat for each song you want
└─ Numbers are assigned automatically in order

STEP 3: ADJUST ORDER (OPTIONAL)
├─ To change order:
├─ Select a song in the right panel
├─ Use "↑ Up" or "↓ Down" to reposition it
├─ Numbers update automatically
└─ Buttons re-number correctly as well

STEP 4: EDIT NAMES (OPTIONAL)
├─ Select a song in the right panel
├─ The name appears in the text field
├─ Modify the name as desired (e.g., "My Favorite Song.mp3")
├─ Click "Update Name"
└─ The change is applied immediately

STEP 5: REMOVE SONGS (IF NEEDED)
├─ Select a song in the right panel
├─ Click the red "Remove" button
├─ It will be removed from the list
├─ Numbers will be re-ordered automatically
└─ The original file is NOT deleted, it is only removed from the selection

STEP 6: EXPORT TO "ORDERED" FOLDER
├─ When you are satisfied with the order and names:
├─ Click the large green button: "✓ Export to 'Ordered' Folder"
├─ The files are copied to: C:\Users\[YourUser]\Music\Ordered
├─ Files will be named: "001 - name.mp3", "002 - name.mp3", etc.
├─ A summary of copied files will be shown
└─ Ready to use on your USB or SD card!

STEP 7: USE IN THE CAR
└─ Copy the "Ordered" folder to your USB or SD card
   Songs will play in the EXACT order you set

================================================================================
MAIN FEATURES:
================================================================================

✓ Manual selection of songs one-by-one
✓ Edit display names directly in the UI
✓ Visual reordering with up/down buttons
✓ Automatic sequential numbering (001, 002, 003...)
✓ Clear visual playlist of playback order
✓ Intuitive and easy-to-use interface
✓ Support for multiple formats: MP3, WMA, WAV, FLAC, M4A
✓ Automatic creation of the "Ordered" folder
✓ Safe copy operation (originals remain untouched)

================================================================================
SUPPORTED FORMATS:
================================================================================

✓ MP3   (.mp3)
✓ WMA   (.wma)
✓ WAV   (.wav)
✓ FLAC  (.flac)
✓ M4A   (.m4a)

You can add more formats by editing the filter line:
    "Music Files", "mp3", "wma", "wav", "flac", "m4a"

================================================================================
OUTPUT LOCATION:
================================================================================

Files are copied to:
C:\Users\[YourUser]\Music\Ordered

Examples:
• C:\Users\Juan\Music\Ordered
• C:\Users\Maria\Music\Ordered
• C:\Users\PC-NOX\Music\Ordered

If the "Ordered" folder does not exist it will be created automatically.
If it already exists, files will be overwritten without prompting.

================================================================================
USAGE EXAMPLES:
================================================================================

EXAMPLE 1: Build a compilation of favorite songs

1. Browse: My Music Library
2. Add in order:
   - 001 - Sweet Dreams (Eurythmics).mp3
   - 002 - Bohemian Rhapsody (Queen).mp3
   - 003 - Imagine (John Lennon).mp3
   - 004 - Levantando Manos (Local Artist).mp3
   - etc...
3. If needed, change order with the up/down buttons
4. Export to the "Ordered" folder
5. Copy the folder to USB
6. Enjoy in your car!

EXAMPLE 2: Organize by artist

1. Browse: C:\Users\[YourUser]\Music\AC/DC
2. Add all songs in the desired order
3. Edit names if needed
4. Export to "Ordered"
5. Copy to SD

EXAMPLE 3: Create a custom mix

1. Browse artist A
2. Add the best songs
3. Switch library (browse artist B)
4. Add songs from other artists
5. Edit all names
6. Reorder to taste
7. Export to "Ordered"
8. Mix ready!

================================================================================
IMPORTANT NOTES:
================================================================================

⚠ Files are COPIED, not MOVED
  → Originals remain in place
  → A copy is created in the "Ordered" folder

⚠ Names can be freely edited
  → Edit only the name; DO NOT include path separators (\) or other special characters
  → Keep file extensions (.mp3, .wma, etc.)

⚠ Order matters
  → Numbers determine playback order
  → Most car players use numeric order (001, 002, 003...)
  → Verify the order before exporting to "Ordered"

⚠ The "Ordered" folder can be cleaned
  → Running the app again will overwrite files
  → Backup if you have important content

================================================================================
TROUBLESHOOTING:
================================================================================

Q: I don't see my MP3 files
A: Check that:
   1. Files are .mp3, .wma, .wav, .flac or .m4a
   2. They are inside the folder you selected
   3. They are not protected or inside a system folder

Q: The "Ordered" folder is not created
A: Check that:
   1. You have write permissions for C:\Users\[YourUser]\Music
   2. There is no existing protected "Ordered" folder
   3. There is enough disk space

Q: Files are not copying
A: Check that:
   1. Original files are not open in another program
   2. You have disk space
   3. Files are not write-protected

Q: My car does not play files in the correct order
A: Remember:
   1. Name format may be required as 001.mp3, 002.mp3, 003.mp3 (numbers only)
   2. Or: 001 - name.mp3, 002 - name.mp3 (MusiCar2 uses this format)
   3. Some older car players only recognize numbers without dashes

================================================================================
SYSTEM REQUIREMENTS:
================================================================================

• Java 8 or newer (run `java -version` to check)
• OS: Windows, macOS, or Linux
• Sufficient disk space for copying files
• Music library: files in supported formats

================================================================================
DEVELOPMENT & SUPPORT:
================================================================================

Developed by: TabToMg
Email: mgtoireland@gmail.com

To report bugs or suggest features, contact by email.

================================================================================
