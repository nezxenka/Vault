package net.milkbowl.vault.placeholder.children;

import com.google.common.primitives.Ints;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PermissionHook {

    private final Permission permission;
    private final Chat chat;

    public PermissionHook(Permission permission, Chat chat) {
        this.permission = permission;
        this.chat = chat;
    }

    @Nullable
    public String onRequest(
        @Nullable OfflinePlayer player,
        @NotNull String params
    ) {
        if (player == null) return "";

        // this will be the most frequently requested,
        // so I wrote it separately here
        if (params.equalsIgnoreCase("prefix")) {
            return this.getPlayerMeta(player, true);
        } else if (params.equalsIgnoreCase("suffix")) {
            return this.getPlayerMeta(player, false);
        }

        if (
            params.startsWith("rankprefix_") ||
            params.startsWith("groupprefix_")
        ) {
            return this.getGroupMeta(player, params, true);
        }

        if (
            params.startsWith("ranksuffix_") ||
            params.startsWith("groupsuffix_")
        ) {
            return this.getGroupMeta(player, params, false);
        }

        if (params.startsWith("hasgroup_")) {
            final String group = params.substring(9);
            return this.bool(
                this.permission.playerInGroup(null, player, group)
            );
        }

        if (params.startsWith("inprimarygroup_")) {
            final String group = params.substring(15);
            final String primaryGroup = this.getPrimaryGroup(player);
            return this.bool(primaryGroup.equals(group));
        }

        return switch (params) {
            case "group", "rank" -> this.getPrimaryGroup(player);
            case "group_capital", "rank_capital" -> {
                final String primaryGroup = this.getPrimaryGroup(player);
                yield this.capitalize(primaryGroup);
            }
            case "groups", "ranks" -> String.join(
                ", ",
                this.getPlayerGroups(player)
            );
            case "groups_capital", "ranks_capital" -> {
                final String[] playerGroups = this.getPlayerGroups(player);
                if (playerGroups.length == 0) yield "";
                final StringBuilder groups = new StringBuilder();
                for (final String group : playerGroups) {
                    groups.append(group).append(',').append(' ');
                }
                groups.setLength(groups.length() - 2);
                yield groups.toString();
            }
            case "groupprefix", "rankprefix" -> this.getPrimaryGroupMeta(
                player,
                true
            );
            case "groupsuffix", "ranksuffix" -> this.getPrimaryGroupMeta(
                player,
                false
            );
            default -> "";
        };
    }

    @NotNull
    private String getPlayerMeta(
        @NotNull OfflinePlayer player,
        boolean isPrefix
    ) {
        final String result = isPrefix
            ? this.chat.getPlayerPrefix(null, player)
            : this.chat.getPlayerSuffix(null, player);
        return result != null ? result : "";
    }

    @NotNull
    private String getGroupMeta(
        @NotNull OfflinePlayer player,
        String params,
        boolean isPrefix
    ) {
        final String number = params.substring(params.lastIndexOf("_") + 1);
        final Integer index = Ints.tryParse(number);
        if (index == null || index < 0) {
            return "Invalid number " + number;
        }

        final String[] groups = this.getPlayerGroups(player);

        if (index > groups.length) return "";

        for (int i = index - 1; i < groups.length; i++) {
            final String meta = this.getGroupMeta(groups[i], isPrefix);
            if (!meta.isEmpty()) return meta;
        }

        return "";
    }

    @NotNull
    private String bool(boolean value) {
        return value
            ? PlaceholderAPIPlugin.booleanTrue()
            : PlaceholderAPIPlugin.booleanFalse();
    }

    @NotNull
    private String getPrimaryGroup(@NotNull OfflinePlayer player) {
        final String primaryGroup = this.permission.getPrimaryGroup(
            null,
            player
        );
        return primaryGroup != null ? primaryGroup : "";
    }

    @NotNull
    private String capitalize(@NotNull String str) {
        if (str.isEmpty()) return str;
        return (
            Character.toUpperCase(str.charAt(0)) +
            str.substring(1).toLowerCase()
        );
    }

    @NotNull
    private String[] getPlayerGroups(@NotNull OfflinePlayer player) {
        final String[] groups = permission.getPlayerGroups(null, player);
        return groups == null ? new String[0] : groups;
    }

    @NotNull
    private String getPrimaryGroupMeta(
        @NotNull OfflinePlayer player,
        boolean isPrefix
    ) {
        final String primaryGroup = this.getPrimaryGroup(player);
        return this.getGroupMeta(primaryGroup, isPrefix);
    }

    @NotNull
    private String getGroupMeta(@NotNull String group, boolean isPrefix) {
        if (group.isEmpty()) return group;
        final String result = isPrefix
            ? this.chat.getGroupPrefix((World) null, group)
            : this.chat.getGroupSuffix((World) null, group);
        return result != null ? result : "";
    }
}
