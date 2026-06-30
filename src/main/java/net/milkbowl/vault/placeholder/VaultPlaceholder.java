package net.milkbowl.vault.placeholder;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Nullable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Taskable;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.milkbowl.vault.placeholder.children.EconomyHook;
import net.milkbowl.vault.placeholder.children.PermissionHook;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class VaultPlaceholder
    extends PlaceholderExpansion
    implements Configurable, Taskable
{

    private final EconomyHook economyHook;
    private final PermissionHook permissionHook;

    public VaultPlaceholder(Economy economy, Permission permission, Chat chat) {
        this.economyHook =
            economy != null ? new EconomyHook(this, economy) : null;
        this.permissionHook =
            permission != null && chat != null
                ? new PermissionHook(permission, chat)
                : null;
    }

    @NotNull
    public String getIdentifier() {
        return "vault";
    }

    @NotNull
    public String getAuthor() {
        return "GroundbreakingMC";
    }

    @NotNull
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public Map<String, Object> getDefaults() {
        return ImmutableMap.<String, Object>builder()
            .put("formatting.us-number-format", "false")
            .put("formatting.thousands", "k")
            .put("formatting.millions", "M")
            .put("formatting.billions", "B")
            .put("formatting.trillions", "T")
            .put("formatting.quadrillions", "Q")
            .build();
    }

    @Override
    public void start() {
        if (this.economyHook != null) {
            this.economyHook.setup();
        }
    }

    @Override
    public void stop() {}

    @Nullable
    public String onRequest(
        @Nullable OfflinePlayer player,
        @NotNull String params
    ) {
        if (player == null) {
            return "";
        } else if (params.startsWith("eco_")) {
            if (this.economyHook == null) return "economy not found";
            return this.economyHook.onRequest(player, params.substring(4));
        } else {
            if (this.permissionHook == null) return "economy not found";
            return this.permissionHook.onRequest(player, params);
        }
    }
}
