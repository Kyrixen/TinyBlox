>[!IMPORTANT]
>TinyBlox is back!
Development has resumed, and the source code and releases remain available as a reference and playable project.
> 


# TinyBlox

<div align="center">

![Showcase](https://raw.githubusercontent.com/Kyrixen/TinyBlox/main/assets/tinyblox/textures/background/menu_background.png)

</div>

---

A lightweight pseudo-3D sandbox survival game written in Java using LibGDX framework as backend.

---

## What Makes TinyBlox Different?

TinyBlox is built around layered terrain instead of voxels.

The world is composed of layers of tiles which create pseudo-3D traversal,
allowing caves, cliffs, ladders, elevation changes, and terrain-driven
exploration without using 3D graphics.

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
- [x] World saving/loading
- [ ] HUD
- [ ] Biomes
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
| `I` | Selector Up |
| `J` | Selector Left |
| `K` | Selector Down |
| `L` | Selector Right |
| `E` | Show / Hide Inventory |
| `C` | Toggle Crafting Menu |
| `Q` | Drop Item |
| `Left Control` | Sprint |
| `Space` + `WASD` | Step Up |
| `Left Shift` + `WASD` | Step Down |
| `Space` | Climb Up |
| `Left Shift` | Climb Down |
| `Left Mouse Button /  H` | Destroy Block / Hit Entity |
| `Right Mouse Button / U` | Place Block |
| `Scroll Up / O` | Previous Inventory Slot |
| `Scroll Down / P` | Next Inventory Slot |
| `Scroll Up / O` | Previous Crafting Recipe |
| `Scroll Down / P` | Next Crafting Recipe |

---

## Download TinyBlox

Download the latest release or play on itch.io.

- **GitHub Releases:** [Github](https://github.com/Kyrixen/TinyBlox/releases/latest)
- **itch.io:** [Itch.io](https://kyrixen.itch.io/tinyblox)

> Requires Java 17+.

![Thank You](https://raw.githubusercontent.com/Kyrixen/TinyBlox/main/screenshots/thank_you.png)

---

## Notes

- The world uses layered TileStacks for pseudo-3D topology rendering.
- Textures use a 16x16 pixel-art style.
- Some systems currently use temporary or debug implementations.
- Systems are still experimental and may evolve significantly.
- Performance optimization is ongoing.
- HTML build every release
- Android maybe
- iOS port? That would be cool, but I don't own a Mac series 3000.

> [!IMPORTANT]
>
> TinyBlox is currently in active development.
> This is NOT a finished game.
> Systems, mechanics, rendering, terrain generation, saves,
> and architecture may change frequently between versions.

---

## License

TinyBlox is source-available. You're free to explore the code, learn from it, modify it, and create non-commercial forks.

***The source code and game assets are licensed separately.***


### Source Code

The source code is licensed under the **TinyBlox Source License**.

The source license allows you to:

* View and study the source code
* Modify the project
* Create forks and derivative works
* Redistribute original or modified versions for non-commercial purposes

Commercial use, sale, monetization, and inclusion in commercial products or services are not permitted.

See [`LICENSE`](LICENSE) for the complete terms.


### Assets

The contents of the `assets/` directory are **proprietary** and are licensed separately under the **TinyBlox Asset License**.

The assets may be used for developing, testing, contributing to, or running TinyBlox, but may not be redistributed or used in other projects.

See [`ASSET_LICENSE.md`](ASSET_LICENSE.md) for the complete terms.

The source code license does not grant rights to the assets, and the asset license does not grant rights to the source code.

---

\
Built using LibGDX, FastNoiseLite, Java, Gradle

Created by Kyrixen\
Copyright (c) 2026 Kyrixen
