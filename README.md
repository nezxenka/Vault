# Vault - Streamlined Vault for Modern Minecraft Servers

**Vault** is a lightweight fork of the classic Vault plugin, optimized for modern Minecraft servers. It maintains full compatibility with the original Vault while removing outdated implementations that waste server resources.

## 🚀 What makes Vault different?

Modern Minecraft servers use **LuckPerms** as the standard for permissions and chat, but original Vault still tries to detect and load implementations for 35+ dead plugins like PermissionsEx, bPermissions, GroupManager, mChat, iChat, and many others that nobody uses anymore.

### What Vault removes:
- ❌ **Update checker** - No more network calls to dead APIs
- ❌ **35+ legacy plugin implementations** - No startup time wasted detecting dead plugins
- ❌ **Useless metrics collection** - No tracking of unused permission/chat implementations

### What Vault adds:
- ✅ **Built-in PlaceholderAPI integration** - Economy placeholders work out of the box
- ✅ **Faster startup** - No time wasted scanning for dead plugins
- ✅ **Same identity** - Uses `provides: [Vault]` so all plugins see it as original Vault

## 📦 Installation

**Simple replacement:**
1. Stop your server
2. Remove original `Vault.jar` from plugins folder
3. Add `Vault.jar` to plugins folder
4. Start your server

All your existing plugins will work exactly the same!

## 🔧 For Developers

Vault is **100% API compatible** with original Vault:

```java
// These work exactly as before:
Plugin vault = getServer().getPluginManager().getPlugin("Vault");
Plugin Vault = getServer().getPluginManager().getPlugin("Vault"); 

// Economy API unchanged:
RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
if (rsp != null) {
    Economy economy = rsp.getProvider();
    // All methods work the same
}
```

**No code changes needed** - Vault keeps the same package structure (`net.milkbowl.vault`) and class names as original Vault.

## 🎯 Supported Economy Plugins

Vault works with all economy plugins that support Vault API:
- EssentialsX Economy
- CMI Economy
- TNE (The New Economy)
- И все остальные!

## 📋 PlaceholderAPI Integration

Vault includes built-in economy placeholders:
- `%vault_eco_balance%` - Player's balance
- `%vault_eco_balance_fixed%` - Balance with fixed decimal places
- `%vault_eco_balance_formatted%` - Formatted balance with currency symbol

No separate expansion needed! See [complete placeholder list](https://wiki.placeholderapi.com/users/placeholder-list/#vault) for all available options.

## 🏗️ Building

```bash
git clone https://github.com/nezxenka/Vault.git
cd Vault  
mvn clean package
```

## 📜 License

Vault inherits the same license as original Vault.

## ⭐ Why choose Vault?

- **Perfect drop-in replacement** - Works with all existing plugins
- **Modern server focused** - No resources wasted on dead plugins
- **Faster startup** - No scanning for 35+ legacy plugins
- **PlaceholderAPI included** - No need for separate expansion downloads
- **Same plugin identity** - All plugins recognize it as "Vault"

---
*Vault - Because sometimes less is more.*
