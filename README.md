# CobSwap

A client-side Fabric mod for Minecraft that automatically switches to a cobweb in your hotbar and places it, so you don't have to manually swap slots and click.

## Features
- Toggle on/off with a keybind (default: `G`)
- Configurable delay (in ticks) between the slot switch and the placement, adjustable in-game with `[` / `]`
- Only triggers on legitimate block-target placements — never touches entity/combat interactions
- Delay setting is saved to `config/cobswap.json` and persists between sessions

## Tech Stack
- Java 21
- Fabric Loader + Fabric API
- Official Mojang Mappings
- Gradle (Fabric Loom)

## Requirements
- Minecraft 1.21.11
- Fabric Loader 0.19.3+
- Fabric API 0.141.5+1.21.11

## Building
```
./gradlew build
```
The playable jar is output to `build/libs/cobswap-<version>.jar` (the plain, no-suffix one — not the `-dev` or `-sources` jars).

## Installing
Drop the built (or released) jar into `.minecraft/mods/`, alongside matching Fabric Loader and Fabric API versions.

## Controls
Found under Options → Controls → Key Binds → CobSwap:
| Action | Default key |
|---|---|
| Toggle CobSwap | `G` |
| Increase delay | `]` |
| Decrease delay | `[` |

## How it was built
I designed and directed this project — the feature idea, the toggle/delay behavior, and testing every stage in real gameplay — but I went into it with close to no prior knowledge of Fabric mod development. I worked through it with AI assistance (Claude) to learn the Fabric/Minecraft APIs, work through mapping issues (Yarn vs. Mojang mappings), and structure the code, while I made the calls on what the mod should actually do and verified each build myself before moving to the next step.

## Notes
- This mod is intended for single-player / private use.
- Built and tested incrementally: detection and validation logic were verified in a non-destructive (log-only) mode before the actual placement action was wired in, to avoid breaking normal mining/placement if something was misconfigured.
