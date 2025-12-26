# 🛡️ Airlock

**PacketEvents-based plugin for Netty pipeline injection to terminate unauthorized connections before packet decompression.**
The definitive `iptables` alternative for shared hosting environments to prevent proxy bypass and mitigate native memory exhaustion exploits.

---

## 🚨 The Problem
If you run a **Velocity/BungeeCord** network, you must secure your backend ports.
*   **The Standard Solution:** Configuring `iptables` or `UFW` (System Firewall).
*   **The Issue:** On shared hosting or panels like Pterodactyl, **you often lack root access** to the OS firewall.
*   **Why Plugins Fail:** Conventional security plugins (like *OnlyProxyJoin* or *BungeeGuard*) validate connections **too late**. They act only *after* the packet has been accepted, memory allocated, and data decompressed. By that time, the server is already vulnerable to Netty Crashers and Compression Bombs.

## ⚡ The Solution
**Airlock** injects a strict filter into the Netty pipeline (via PacketEvents) and terminates unauthorized connections during the **TCP Handshake** phase.

| Connection Stage | Standard Plugins / BungeeGuard | **Airlock** |
| :--- | :--- | :--- |
| **TCP Handshake** | ✅ Accepts connection | ❌ **Terminates (if IP is unauthorized)** |
| **Memory Allocation** | ✅ Allocates RAM | ⛔ **Prevented** |
| **Decompression** | ✅ Decompresses (Exploit Risk!) | ⛔ **Prevented** |
| **Packet Parsing** | ✅ Reads Data | ⛔ **Prevented** |
| **Auth Event** | ❌ Kicks Player | — |

**Result:** Malicious packets are never decompressed. The server wastes zero CPU/RAM resources on garbage traffic.

---

## 🛠 Technical Specifications
*   **Core:** Powered by **PacketEvents** (Shaded).
*   **Performance:** O(1) IP lookup efficiency via synchronized `HashSet`. Zero impact on TPS.
*   **Compatibility:** Spigot, Paper, Pufferfish, **Leaf** (1.21+).
*   **Security:** Mitigates "Null Ping", "Netty Compression Exploits", "Invalid NBT" attacks, and direct proxy bypass attempts.

---

## 📥 Installation

1. Download the latest `.jar` from [Releases](https://github.com/purpurcof/Airlock/releases).
2. Place it in the `plugins/` folder of your **Backend Server** (Survival, Anarchy, etc.).
   > ⚠️ **DO NOT** install this plugin on your Proxy (Velocity)!
3. Start the server once to generate the configuration file.
4. Edit `plugins/Airlock/config.yml`.

---

## ⚙️ Configuration

Add **only** the IP addresses of your Proxy (Velocity) to the whitelist.

```yaml
# config.yml

# Any IP not listed here will be terminated at the TCP Handshake level
allowed-ips:
  - "127.0.0.1"
```

5. Restart the server.
6. **Verify:** Attempt to connect to the backend server directly using its specific port (bypassing Velocity). The connection should be refused/closed immediately.

---

## ❗ Disclaimer

Airlock serves as a **software-defined firewall**.
1.  If you have root access to a VDS/Dedicated server, **always prefer `iptables`**. It operates at the kernel level and is superior to any Java-based solution. Use Airlock only if your hosting provider restricts OS-level access.
2.  Ensure you whitelist the correct Proxy IP. Incorrect configuration will lock you out of the server.

---

## License
MIT License. You are free to use, modify, and distribute this software.
