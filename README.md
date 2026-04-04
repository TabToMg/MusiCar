# 🎵 MusiCar

**Musicar** is a desktop application designed to organize music libraries for playback in car audio systems, especially older vehicles with limited media capabilities.

---

## 🚗 The Problem

Through my daily use of older cars, I faced a recurring issue:  
managing and organizing music for playback on **CDs, USB drives, and SD cards** was unnecessarily difficult.

Most legacy car audio systems:
- Lack advanced navigation features  
- Depend on strict file structures  
- Sort tracks based on **file creation date** rather than metadata  
- Provide poor user experience when music is not properly organized  

Creating playlists or controlling playback order was often frustrating or simply not possible.

---

## 💡 The Solution

To solve this, I developed **Musicar** — a tool that allows users to **manually or randomly organize their music** and export it in a format optimized for car playback.

The application ensures that songs are played **exactly in the desired order**, regardless of the limitations of the car's media system.

---

## ⚙️ How It Works

### 🔹 Version 1 (Initial Approach)
The first version focused on understanding how car systems determine playback order.

- Discovered that many systems rely on **file creation date**
- Implemented a solution that:
  - Renamed files with numerical prefixes (e.g. `01 - song.mp3`)
  - Modified file creation timestamps using **NirCmd**
- Result: Controlled playback order successfully on limited systems

---

### 🔹 Current Version (Improved Workflow)

Musicar now provides a more intuitive and user-friendly experience:

1. **Select a folder** containing your music files  
2. Add songs to your custom playlist by:
   - Clicking or pressing **"Add"** in your preferred order  
   - Or using **"Shuffle"** to randomize the playlist  
3. Press **"Shuffle"** multiple times until you're satisfied with the order  
4. Use the **preview panel** to verify the final playlist  
5. Click **"Export to Organized Folder"** to generate a ready-to-use folder  

---

## 📂 Export Behavior

- The application creates a fully organized folder ready for playback  
- Songs are renamed and structured according to the selected order  
- If the destination folder already exists:
  - It will be **overwritten** with the new structure and updated playlist  

---

## ✨ Key Features

- Manual playlist creation  
- Random playlist generation (shuffle)  
- Real-time preview of track order  
- File renaming for consistent playback  
- Optimized export for CD / USB / SD usage  
- Designed specifically for compatibility with older car systems  
## 📸 Screenshots

<p align="center">
  <img src="https://i.ibb.co/JY53Mn7/Screenshot1.png" alt="Screenshot 1" width="600"/>
</p>

<p align="center">
  <img src="https://i.ibb.co/nNYqNRKk/Screenshot2.png" alt="Screenshot 2" width="600"/>
</p>

<p align="center">
  <img src="https://i.ibb.co/bMRcqKqf/Screenshot3.png" alt="Screenshot 3" width="600"/>
</p>

---


## 🛠️ Technologies

- Java  
- File system manipulation  
- External tool integration (**NirCmd**)  

---

## 🎯 Purpose

Musicar was built as a practical solution to a real-world problem, combining software development with everyday usability.  
It demonstrates how understanding system limitations can lead to simple yet effective tools.

---

## 📌 Future Improvements

- Drag & drop interface  
- Metadata-based organization (artist, album, genre)  
- Format conversion support  
- Folder size and track limits for specific car systems  

---

## 👨‍💻 Author

Developed by **TabToMg**
*2026 Edition*.  