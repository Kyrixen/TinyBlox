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
- Modular architecture: `Engine`, `Renderer`, `Textures`, `Camera`, `Terrain`, `Controller`, `Entity`, `Player`, `Enemy`, `Sound`, `Utils`

---

>[IMPORTANT]
>
>This is just test of game mechanics not a full game!

---

## Game Mechanics

- [x] Player movement
- [x] Entities
- [x] Enemy AI
- [ ] Helper NPC
- [ ] Menu
- [x] Terrain
- [ ] RNG
- [ ] Combat mechanics
- [ ] Stats
- [ ] Dialogues
- [ ] Teleportation
- [ ] Placing
- [ ] Destroying
- [ ] Sound
- [ ] Boss fight
- [ ] Multiplayer?
- [ ] 32-bit support?
- [ ] Web?




---

## Requirements

- Java 8
- Gradle 4.10.3
- Desktop for now (mobile maybe later)

---

## Setup Instructions

1. Clone the repository:

```bash
git clone https://github.com/Kyrixen/TinyEngineJava.git
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

* Player is spawned in the middle of the map.
* All entities and terrain tiles are drawn via `Textures`, so offsets from the camera are handled there.
* Textures are 50x50 (16x16 soon)
* Sounds are handled via `javax.sound.sampled.Clip`.
* Health and stamina systems include cooldowns and limits.

---

## Future Improvements

* Zoomable camera
* Tile-based collision for entities
* Enemy AI improvements
* Animated textures
* Configurable key bindings

---

## License

This project is under Apache-2.0 license.
