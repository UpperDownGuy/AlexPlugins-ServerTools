# 🧰 AlexPlugins – ServerTools

**ServerTools** is a modular utility plugin for **Paper Minecraft servers** designed to provide small, focused server-side tools that integrate cleanly with modern plugins like **PlaceholderAPI** and **TAB**.

The project is intentionally structured as a **framework-style plugin**, allowing features to be enabled, disabled, and expanded over time without bloating the server or forcing unnecessary dependencies.

⚠️ **ServerTools is actively developed and considered WIP.**  
Features and structure may change between releases.

---

## ✨ Core Features

### 🕒 Player Timezone System
A fully local, per-player timezone system using **real IANA timezone identifiers** (no EST/PST/GMT guessing).

- Per-player timezone storage
- Server default timezone fallback
- Automatic DST handling
- Join reminders for players without a timezone set
- Integrated PlaceholderAPI support
- Works seamlessly with TAB

Example output:
6:18 PM [EST]

---

### 🧩 PlaceholderAPI Integration (Bundled)
ServerTools registers its own **internal PlaceholderAPI expansion** at runtime.

No eCloud downloads required.

Available placeholder:
%servertools_time%

---

### 📋 Modular Configuration
All main configuration is stored in **`servertools.yml`** (not `config.yml`) to support modular expansion.

Each module can be enabled or disabled independently.

---

### 🛠️ Utility Commands
ServerTools includes a growing set of server utilities, including:
- `/disconnect`
- `/stopserver`
- `/servertools`
- `/xray`
- `/tz`

(Exact availability depends on version.)

---

## 📦 Installation

1. Download the latest ServerTools release from GitHub.
2. Place the JAR into your server’s `plugins/` directory.
3. Install **PlaceholderAPI**.
4. Restart the server.
5. Configure `servertools.yml`.

---

## ⚙ Configuration

### `servertools.yml`

```yaml
plugin:
  enabled: true

disconnect:
  enabled: true

timezones:
  enabled: true
  default: "America/New_York"
  requestonjoin: true
```

### README WRITTEN WITH CHATGPT
