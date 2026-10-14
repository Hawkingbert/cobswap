# 🕸️ CobSwap

**A client-side Fabric mod for Minecraft 1.21.11 that places cobwebs for you mid-fight, with a single left-click and no manual hotbar juggling.**

Hold your sword or axe, left-click a block next to your opponent, and CobSwap quickly switches to your cobweb, places it, and switches back to your weapon. It uses only normal slot switching and block placement, with a configurable delay so the sequence runs at a human-like pace.

> **Client-side only.** Nothing needs to be installed on the server.

---

## ✨ Features

- **One-click cobweb placement.** Left-click a block while holding a sword or axe, and CobSwap does the swap → place → swap-back for you.
- **Combat-aware.** It only triggers when a living entity (player or mob) is within about 3 blocks of the placement spot, and only if that spot is empty. Normal mining is untouched everywhere else.
- **PvP-safe by design.** CobSwap only listens to left-clicks on *blocks*. Left-clicking an *entity* (a normal attack) never goes through the mod.
- **Adjustable timing.** The delay between each step is configurable in ticks (default **2 ticks ≈ 100 ms**), and you can change it live in-game.
- **On/off toggle.** A keybind enables or disables the mod, so it won't interfere when you mine with a sword and have cobwebs in your inventory.
- **Rebindable keys.** Every keybind appears under **Options → Controls → Key Binds → CobSwap**.
- **Action-bar feedback.** Toggling and delay changes show a short message above your hotbar instead of spamming chat.

---

## 🎮 How it works

When the mod is **ON**, the following conditions must all be true:

1. You're holding a **sword or axe** in your main hand.
2. You **left-click a block**, not an entity.
3. The empty space next to the clicked face is free for a cobweb.
4. A **living entity** (not you) is within ~3 blocks of that spot.
5. You have a **cobweb in your hotbar** (slots 1–9).

If all five are true, CobSwap:

1. Cancels the normal mining action for that click.
2. Switches to your cobweb slot.
3. Waits the configured delay.
4. Places the cobweb on the clicked face.
5. Waits the configured delay again.
6. Switches back to your original slot.

If any condition fails, the click behaves exactly as vanilla Minecraft would.

Slot switching sends the same packet that pressing a number key does, and placement goes through the game's normal block-interaction path.

---

## ⌨️ Default keybinds

| Action | Default key | Notes |
|---|---|---|
| Toggle CobSwap on/off | `G` | Starts **OFF** each time the game launches |
| Increase delay (+1 tick) | `]` | Saved automatically |
| Decrease delay (−1 tick) | `[` | Saved automatically |

Rebind any of them in **Options → Controls → Key Binds → CobSwap**.

---

## ⚙️ Configuration

The delay is stored in `config/cobswap.json` inside your Minecraft instance folder. It's created automatically on first launch:

```json
{
  "delayTicks": 2
}
```

| Setting | Default | Range | Description |
|---|---|---|---|
| `delayTicks` | `2` | `0` – `10` | Ticks to wait between switching to the cobweb, placing it, and switching back. 1 tick = 50 ms. |

You can edit the file directly or adjust the value live with the `[` / `]` keys. A delay of `0` runs each step on consecutive ticks with no extra wait.

**Tuning tip:** Some servers' anti-cheat systems flag unusually fast inventory actions. If you get kicked or flagged, press `]` to increase the delay.

---

## 📦 Installation

### Requirements

| Requirement | Version |
|---|---|
| Minecraft | **1.21.11** |
| [Fabric Loader](https://fabricmc.net/use/) | 0.19.3 or newer |
| [Fabric API](https://modrinth.com/mod/fabric-api) | 0.141.5+1.21.11 or a compatible build |
| Java | 21 |

### Steps

1. Install **Fabric Loader** for Minecraft 1.21.11 using the [official installer](https://fabricmc.net/use/).
2. Download **Fabric API** and place the jar in your `mods` folder.
3. Download `cobswap-<version>.jar` from the [Releases](../../releases) page and place it in the same `mods` folder.
   - On Windows, the folder is usually `%appdata%\.minecraft\mods`.
4. Launch Minecraft with the **Fabric** profile.
5. Open **Options → Controls → Key Binds**, find the **CobSwap** category, and check or rebind your keys.

---

## 🕹️ Quick start

1. Put a **cobweb** in your hotbar.
2. Hold a **sword or axe**.
3. Press **`G`** to turn CobSwap on. You'll see `CobSwap: ON` above your hotbar.
4. Left-click a block near your opponent.
5. Adjust the timing with `[` and `]` if needed.

---

## 🛠️ Building from source

```bash
git clone https://github.com/<your-username>/CobSwap.git
cd CobSwap
./gradlew build        # on Windows: gradlew.bat build
```

The finished jar is written to `build/libs/`:

| File | Purpose |
|---|---|
| `cobswap-<version>.jar` | **Use this one.** The remapped, playable mod |
| `cobswap-<version>-dev.jar` | Unmapped development jar, not for playing |
| `cobswap-<version>-sources.jar` | Source code archive |

To test in a development client, run `./gradlew runClient`.

### Tech stack

- Java 21
- Fabric Loader 0.19.3 and Fabric API 0.141.5+1.21.11
- Fabric Loom (plugin ID `net.fabricmc.fabric-loom-remap`)
- Official Mojang mappings
- Split client/common source sets
- Gson for config storage (bundled with Minecraft)

> **Note:** Avoid keeping the project in a cloud-synced folder such as OneDrive, Dropbox, or Google Drive. File locking can corrupt Loom's cached Minecraft files and cause confusing build errors.

---

## 🗂️ Project structure

```
src/
├── main/java/me/adnan/cobswap/
│   └── CobSwap.java                    # Mod entrypoint, MOD_ID, logger
└── client/
    ├── java/me/adnan/cobswap/client/
    │   ├── CobSwapClient.java          # Client entrypoint, wires everything up
    │   ├── KeybindManager.java         # Toggle and delay keybinds
    │   ├── ToggleManager.java          # On/off state
    │   ├── CobSwapConfig.java          # Loads/saves delay to cobswap.json
    │   ├── CobwebAttackHandler.java    # Left-click-on-block event hook
    │   ├── CobwebPlacementValidator.java  # "Is this a valid cobweb spot?"
    │   ├── HotbarScanner.java          # Finds the cobweb in the hotbar
    │   ├── CobwebPlacer.java           # Slot switching and block placement steps
    │   └── CobwebScheduler.java        # Tick-based timing between steps
    └── resources/assets/cobswap/lang/
        └── en_us.json                  # Keybind display names
```

---

## ❓ FAQ

**Does it work on servers?**
Yes. It's client-side only, so it works on any server that allows the mod's behavior. See the disclaimer below.

**Does it work alongside other quick-swap mods (like a wind charge mod)?**
CobSwap only reacts to left-clicks on blocks, so it doesn't compete with mods that act on right-clicks. Two mods that react to different events won't conflict by design.

**Why didn't it place a cobweb?**
Check that CobSwap is toggled ON, you're holding a sword or axe, a cobweb is in your **hotbar** (not the main inventory), a living entity is within ~3 blocks, and the target space is empty. Also, it won't start a new sequence while one is still running.

**Why doesn't it search my main inventory?**
It scans the hotbar only, which avoids moving items around and the desync risk that comes with it.

**Can I make it faster or slower?**
Yes. Use `]` and `[`, or edit `delayTicks` in `config/cobswap.json`.

---

## ⚠️ Disclaimer

CobSwap automates a repetitive inventory action. Many multiplayer servers have rules against automation or macros, and some anti-cheat systems may flag fast inventory actions even when each one is a normal game action. **Check the server's rules before using it.** The author isn't responsible for bans or other consequences from using this mod. Use it at your own risk, and raise the delay if a server is strict.

---

## 📄 License

See the [LICENSE](LICENSE) file for details.

---

## 🙌 Credits

- Built with [Fabric](https://fabricmc.net/) and [Fabric API](https://github.com/FabricMC/fabric).
- Minecraft is a trademark of Mojang AB. This project is not affiliated with Mojang or Microsoft.
