package net.milkbowl.vault;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.milkbowl.vault.placeholder.VaultPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Vault extends JavaPlugin {

    private VaultPlaceholder placeholder;

    @Override
    public void onEnable() {
        // we will register placeholder on first server tick
        // to get the providers right
        Bukkit.getScheduler().runTask(this, () -> {
            if (
                super
                    .getServer()
                    .getPluginManager()
                    .getPlugin("PlaceholderAPI") != null
            ) {
                this.placeholder = new VaultPlaceholder(
                    this.getProvider(Economy.class),
                    this.getProvider(Permission.class),
                    this.getProvider(Chat.class)
                );
                this.placeholder.register();
            }
        });
    }

    @Override
    public void onDisable() {
        // check for null, bc the plugin can be disabled faster
        // than the registration task will run
        if (this.placeholder != null) {
            // runtime reload?
            try {
                this.placeholder.unregister();
            } catch (Throwable ignored) {
                // ignore if PlaceholderAPI is already disabled
            }
        }
    }

    private <T> T getProvider(Class<T> providerClass) {
        final ServicesManager servicesManager = super
            .getServer()
            .getServicesManager();
        final ServicesManager sm = Bukkit.getServicesManager();
        final String name = System.getProperty(
            "vault." + providerClass.getSimpleName(),
            null
        );
        final RegisteredServiceProvider<T> provider =
            name == null
                ? sm.getRegistration(providerClass)
                : sm
                      .getRegistrations(providerClass)
                      .stream()
                      .filter(r ->
                          r.getPlugin().getName().equalsIgnoreCase(name)
                      )
                      .findFirst()
                      .orElse(null);

        if (provider != null) {
            super
                .getLogger()
                .info(
                    providerClass.getSimpleName() +
                        " provider loaded by " +
                        provider.getPlugin().getName() +
                        " (" +
                        provider.getProvider().getClass().getCanonicalName() +
                        ")"
                );
            return provider.getProvider();
        } else {
            super
                .getLogger()
                .warning(
                    providerClass.getSimpleName() + " provider not found!"
                );
            return null;
        }
    }
}
