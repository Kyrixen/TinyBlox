# TinyBlox

<div align="center">

![Showcase](https://raw.githubusercontent.com/Kyrixen/TinyBlox/main/assets/textures/background/menu_background.png)

</div>

---

A lightweight pseudo-3D sandbox survival game written in Java using LibGDX framework as backend.

---

## What Makes TinyBlox Different?

TinyBlox is built around layered terrain instead of voxels.

The world is composed of layers of tiles which creates pseudo-3D traversal,
allowing caves, cliffs, ladders, elevation changes, and terrain-driven
exploration without using 3d graphics.

The focus is on:

- Exploration
- Traversal
- Atmosphere
- Terrain interaction
- Lightweight architecture

---

## Screenshots

<div align="center">

### Terrain Exploration
![Terrain Exploration](https://raw.githubusercontent.com/Kyrixen/TinyBlox/main/screenshots/tinyblox_showcase.png)

### Building and Crafting
![Building and Crafting](https://raw.githubusercontent.com/Kyrixen/TinyBlox/main/screenshots/building_showcase.png)

### Combat
![Combat](https://raw.githubusercontent.com/Kyrixen/TinyBlox/main/screenshots/combat_showcase.png)

</div>

---

## Game Checklist

- [x] Player interaction
- [x] World generation system
- [x] Inventory system
- [x] Tool progression
- [x] Crafting
- [x] Enemy AI
- [x] Menu
- [x] Sound
- [x] Entity system
- [x] Day/night cycle
- [x] Lighting (simple)
- [x] Cave generation (Prototype)
- [ ] HUD
- [ ] Biomes
- [ ] World saving/loading
- [ ] Boss fights
- [ ] Dungeons

---

## Controls

| Key | Action |
| --- | --- |
| `W` | Move Up |
| `A` | Move Left |
| `S` | Move Down |
| `D` | Move Right |
| `I` | Show / Hide Inventory |
| `C` | Toggle Crafting Menu |
| `Q` | Drop Item |
| `Left Control` | Sprint |
| `Space` + `WASD` | Step Up |
| `Left Shift` + `WASD` | Step Down |
| `Space` | Climb Up |
| `Left Shift` | Climb Down |
| `Left Mouse Button` | Destroy Block / Hit Entity |
| `Right Mouse Button` | Place Block |
| `Scroll Up` | Previous Inventory Slot |
| `Scroll Down` | Next Inventory Slot |

---

## Download TinyBlox

### Latest Release

Download the latest TinyBlox release and start playing immediately.
***[Releases](https://github.com/Kyrixen/TinyBlox/releases/latest)***

> Requires Java 17+.

![Thank You](https://raw.githubusercontent.com/Kyrixen/TinyBlox/main/screenshots/thank_you.png)

---

## Notes

- The world uses layered TileStacks for pseudo-3D topology rendering.
- Textures use a 16x16 pixel-art style.
- Systems are still experimental and may evolve significantly.
- Performance optimization is ongoing.
- Some systems currently use temporary or debug implementations.

> [!IMPORTANT]
>
> TinyBlox is currently in active development.
> This is NOT a finished game.
> Systems, mechanics, rendering, terrain generation, saves,
> and architecture may change frequently between versions.

---

## License

The source code is licensed under the **TinyBlox Source License** (non-commercial, source-available).

You are allowed to:

- View and study the source code
- Modify the project
- Create forks and derivative works
- Redistribute modified or unmodified versions

You are NOT allowed to:

- Use the project commercially
- Sell the project or derivative works
- Monetize forks or distributions
- Use TinyBlox code inside commercial products or services

All assets in the `assets/` directory are proprietary. See `LICENSE` and `ASSET_LICENSE.md` for full terms.

By contributing, you agree your contributions may be used, modified, and distributed under the project's licensing terms.

---

## Credits

Created by Kyrixen

Built using LibGDX, FastNoiseLite, Java, Gradle

Copyright (c) 2026 Kyrixen
