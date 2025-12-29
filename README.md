# TinyEngine

A lightweight 2D game engine in Java using LWJGL for rendering, input handling, and audio playback. This engine supports entities, terrain, player movement, camera follow, and basic sounds.

---

## Features

- 2D tile-based terrain
- Player and enemy entities with health and stamina
- Camera that follows the player
- Keyboard input handling
- Audio playback (walking, hit, power-up sounds)
- Grid-based rendering
- Selector system for directional interaction
- Modular architecture: `Engine`, `Renderer`, `Textures`, `Camera`, `Terrain`, `Controller`, `Entity`, `Player`, `Enemy`, `Sound`

---

## Project Structure

```

org.kyrixen
│
├─ Engine.java      # Main engine loop and initialization
├─ Renderer.java    # Handles OpenGL drawing
├─ Textures.java    # Texture loading and rendering
├─ Camera.java      # Camera logic
├─ Terrain.java     # Tile-based terrain system
├─ Controller.java  # Keyboard input handling
├─ Entity.java      # Base entity class
├─ Player.java      # Player entity
├─ Enemy.java       # Enemy entity
├─ Sound.java       # Audio system
├─ Stats.java       # Health and Stamina interfaces
├─ Chunk.java       # Chunk managment
├─ Terrain.java     # Terrain generation
├─ FPSCounter.java  # FPS 
├─ Main.java        # Main loop
├─ Constants.java   # Constants for game
└─ Selector.java    # Inactive for now

````

---

## Requirements

- Java 17+
- LWJGL 3.3.6
- OpenGL compatible system
- Optional: IDE like IntelliJ or VSCode

---

## Setup Instructions

1. Clone the repository:

```bash
git clone https://github.com/yourusername/TinyEngine.git
cd TinyEngine
````

2. Build the project using Gradle:

```bash
./gradlew build
```

3. Run the project:

```bash
./gradlew run
```

---

## Controls

| Key          | Action              |
| ------------ | ------------------- |
| `W`          | Move Up             |
| `A`          | Move Left           |
| `S`          | Move Down           |
| `D`          | Move Right          |
| `Left Shift` | Sprint              |
---

## Notes

* Player is spawned in the middle of the map using pixel-based coordinates.
* All entities and terrain tiles are drawn via `Textures`, so offsets from the camera are handled there.
* Tile map size is defined in pixels (`Constants.MAP_WIDTH` and `Constants.MAP_HEIGHT`).
* Sounds are handled via `javax.sound.sampled.Clip`.
* Health and stamina systems include cooldowns and limits.

---

## Future Improvements

* Zoomable camera
* Tile-based collision for entities
* Enemy AI improvementss
* Animated textures
* Configurable key bindings

---

## License

This project is MIT licensed.
