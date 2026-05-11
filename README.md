# TinyBlox

A lightweight 2D sandbox/survival engine and experimental game written in Java using the LibGDX framework for rendering, input handling, and audio playback.

TinyBlox focuses on modular architecture, terrain systems, entities, world interaction, and experimental gameplay mechanics.

---

<div align="center">

![theme](/assets/textures/background/menu_background.png)

</div>

---

## Features

- 2D tile-based terrain
- Stacked terrain system
- Player and enemy entities
- Health and stamina systems
- Camera follow system
- Keyboard and mouse input handling
- Audio playback system
- Chunk-based tile rendering
- Block placing and destroying
- Directional selector system
- Chunk-based terrain architecture
- Modular engine structure

---

## Architecture

TinyBlox is structured into modular systems including:

- `Engine`
- `Renderer`
- `Textures`
- `Camera`
- `Terrain`
- `Chunk`
- `Tile`
- `Controller`
- `Entity`
- `Player`
- `Enemy`
- `Utils`

The project is designed for experimentation, learning, and gradual expansion into a larger sandbox/survival game.

---

>[!IMPORTANT]
>
>TinyBlox is currently in active development.
>
>This is NOT a finished game.
>
>Systems, mechanics, rendering, terrain generation, saves,
>and architecture may change frequently between versions.
>
>Also this README is created by AI(ChatGPT) because creators of TinyBlox are NOT good writers

---

## Game Mechanics

- [x] Player movement
- [x] Terrain system
- [x] Chunk system
- [x] Block placing
- [x] Block destroying
- [x] Basic enemy AI
- [x] Menu system
- [x] Sound system
- [x] Camera follow
- [x] Entity system
- [ ] RNG improvements
- [ ] Combat mechanics
- [ ] Inventory system
- [ ] Stats system
- [ ] Dialogues
- [ ] Helper NPCs
- [ ] Biomes
- [ ] World saving/loading
- [ ] Boss fights
- [ ] Multiplayer
- [ ] Web support
- [ ] Mobile support
- [ ] 32-bit support

---

## Requirements

- Java 8+
- Gradle 9.3.1+
- Desktop OS (Windows/Linux/macOS)

---

## Setup Instructions

### Clone Repository

```bash
git clone https://github.com/Kyrixen/TinyEngineJava.git
cd TinyEngineJava
````

### Build Project

```bash
./gradlew build
```

### Run Project

```bash
./gradlew run
```

---

## Controls

| Key                  | Action                     |
| -------------------- | -------------------------- |
| `W`                  | Move Up                    |
| `A`                  | Move Left                  |
| `S`                  | Move Down                  |
| `D`                  | Move Right                 |
| `Left Shift`         | Sprint                     |
| `Left Mouse Button`  | Destroy Block / Hit Entity |
| `Right Mouse Button` | Place Block                |

---

## Notes

* Terrain and entities are rendered relative to the camera.
* Textures currently use a 16x16 pixel style.
* Systems are still experimental and may be rewritten.
* Performance optimization is ongoing.
* Terrain/world generation is under active development.
* Some systems may be placeholder implementations.

---

## Future Improvements

* Better terrain generation
* Biome system
* Lighting/shadows
* Animated textures
* Improved AI
* Save/load system
* World streaming optimization
* Multiplayer experiments
* Configurable controls
* Zoomable camera
* Better UI
* Inventory system
* Crafting mechanics

---

## Contributing

Contributions, suggestions, bug reports, and experiments are welcome.

You may:

* Fork the repository
* Modify the source code
* Submit pull requests
* Create experimental branches
* Improve systems and architecture

Please keep contributions clean, documented,
and compatible with the existing project structure when possible.

---

## License

TinyBlox uses a custom non-commercial source-available license.

### Source Code

The source code of this project is licensed under the **TinyBlox Source License**.

You are allowed to:

* View and study the source code
* Modify the project
* Create forks and derivative works
* Redistribute modified or unmodified versions

You are NOT allowed to:

* Use the project commercially
* Sell the project or derivative works
* Monetize forks or distributions
* Use TinyBlox code inside commercial products or services

All redistributed versions must include the original license and copyright notices.

See the `LICENSE` file for full terms.

---

### Assets

All assets inside the `assets/` directory are proprietary unless stated otherwise.

This includes, but is not limited to:

* Textures
* Sprites
* Audio
* Music
* UI elements
* Logos
* Artwork

Assets may NOT be:

* Reused in other projects
* Redistributed separately
* Sold or monetized
* Claimed as original work

without explicit permission from the author.

See `ASSET_LICENSE.md` for full terms.

---

### Contributions

By contributing code or assets to TinyBlox, you agree that your contributions may be used, modified, and distributed as part of the project under its licensing terms.

---

## Credits

Created by Kyrixen Tirev

Built using:

* LibGDX
* Java
* Gradle

---

Copyright (c) 2026 Kyrixen Tirev
