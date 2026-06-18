# TinyBlox

A lightweight pseudo-3D sandbox survival game written in Java using LibGDX framework for rendering, input handling, and audio playback.

TinyBlox focuses on atmospheric exploration, topology-aware terrain, modular architecture, procedural generation, and lightweight sandbox gameplay systems.

---

<div align="center">

![theme](/assets/textures/background/menu_background.png)

</div>

---

## Current Focus

TinyBlox is currently focused on:

* Sandbox interaction systems
* Terrain/topology experimentation
* Atmospheric pseudo-3D rendering
* Procedural terrain generation
* Lightweight gameplay architecture
* Exploration and progression systems

---

## What Makes TinyBlox Different?

TinyBlox is built around layered terrain instead of voxels.

The world is composed of TileStacks that create pseudo-3D traversal,
allowing caves, cliffs, ladders, elevation changes, and terrain-driven
exploration without using a traditional voxel engine.

The focus is on:

- Exploration
- Traversal
- Atmosphere
- Terrain interaction
- Lightweight architecture

---

## Features

* Layered pseudo-3D terrain system
* Chunk-based world architecture
* Procedural terrain generation
* Terrain depth/topology rendering
* Procedural tree generation
* Player and enemy entities
* Health and stamina systems
* Inventory and item systems
* Item drops and pickups
* Tool progression system
* Basic crafting system
* Ore generation
* Camera follow system
* Keyboard and mouse input handling
* Audio playback system
* Block placing and destroying
* Directional selector system
* Day/night cycle
* Atmospheric ambient/local lighting
* Modular engine structure

---

## Screenshots

<div align="center">

### Terrain Exploration
![Terrain Exploration](/screenshots/tinyblox_showcase.png)

### Building and Crafting
![Building and Crafting](/screenshots/building_showcase.png)

### Combat
![Combat](/screenshots/combat_showcase.png)

</div>

---

## Architecture

TinyBlox is structured into modular systems including:

* `Engine`
* `Renderer`
* `Camera`
* `Terrain`
* `Chunk`
* `Tile`
* `TileStack`
* `Controller`
* `Entity`
* `MobEntity`
* `Player`
* `Enemy`
* `Inventory`

TinyBlox is an actively developed sandbox survival game focused on exploration, traversal, atmosphere, and terrain interaction.

The world uses lightweight layered terrain instead of voxel rendering, focusing on topology, traversal, silhouettes, and atmospheric exploration.

---

> [!IMPORTANT]
>
> TinyBlox is currently in active development.
>
> This is NOT a finished game.
>
> Systems, mechanics, rendering, terrain generation, saves,
> and architecture may change frequently between versions.

---

## Game Mechanics

* [x] Player movement
* [x] Terrain system
* [x] Chunk system
* [x] Layered TileStack terrain
* [x] Procedural terrain generation
* [x] Procedural tree generation
* [x] Inventory system
* [x] Item entities and pickups
* [x] Tool progression
* [x] Basic crafting
* [x] Ore generation
* [x] Block placing
* [x] Block destroying
* [x] Basic enemy AI
* [x] Combat system
* [x] Menu system
* [x] Sound system
* [x] Camera follow
* [x] Entity system
* [x] Day/night cycle
* [x] Ladder (partial)
* [x] Lighting (simple)
* [x] Cave generation (Prototype)
* [ ] RNG improvements
* [ ] Stats system
* [ ] Dialogues
* [ ] Helper NPCs
* [ ] Biomes
* [ ] World saving/loading
* [ ] Boss fights

---

## Requirements

* Java 17+
* Gradle 9.3.1+
* Desktop OS (Windows/Linux/macOS)

---

## Setup Instructions

### Clone Repository

```bash
git clone https://github.com/Kyrixen/TinyBloxJava.git
cd TinyBloxJava
```

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

| Key                   | Action                     |
| --------------------  | -------------------------- |
| `W`                   | Move Up                    |
| `A`                   | Move Left                  |
| `S`                   | Move Down                  |
| `D`                   | Move Right                 |
| `I`                   | Show / Hide Inventory      |
| `C`                   | Toggle Crafting Menu       |
| `Q`                   | Drop Item                  |
| `Left Control`        | Sprint                     |
| `Space` + `WASD`      | Step Up                    |
| `Left Shift` + `WASD` | Step Down                  |
| `Space`               | Climb Up                   |
| `Left Shift`          | Climb Down                 |
| `Left Mouse Button`   | Destroy Block / Hit Entity |
| `Right Mouse Button`  | Place Block                |
| `Scroll Up`           | Previous Inventory Slot    |
| `Scroll Down`         | Next Inventory Slot        |

---

## Notes

* The world uses layered TileStacks for pseudo-3D topology rendering.
* Textures currently use a 16x16 pixel-art style.
* Systems are still experimental and may evolve significantly.
* Performance optimization is ongoing.
* Terrain/world generation is under active development.
* Some systems currently use temporary or debug implementations.

---

## Future Improvements

* Cave expansion
* Underground progression
* More structures
* More enemies
* Save/load system
* World persistence
* Biome expansion
* Better UI

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

Created by Kyrixen

Built using:

* LibGDX
* FastNoiseLite
* Java
* Gradle

---

Copyright (c) 2026 Kyrixen
