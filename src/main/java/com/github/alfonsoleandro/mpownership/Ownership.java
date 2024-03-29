package com.github.alfonsoleandro.mpownership;

import armorequip.ArmorListener;
import armorequip.DispenserArmorListener;
import com.github.alfonsoleandro.mpownership.command.MainCommand;
import com.github.alfonsoleandro.mpownership.listener.*;
import com.github.alfonsoleandro.mpownership.manager.OwnershipManager;
import com.github.alfonsoleandro.mpownership.manager.Settings;
import com.github.alfonsoleandro.mpownership.utils.Message;
import com.github.alfonsoleandro.mputils.commands.MPTabCompleter;
import com.github.alfonsoleandro.mputils.files.YamlFile;
import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.github.alfonsoleandro.mputils.reloadable.ReloaderPlugin;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;

// TODO: prevenir crafteos de items con dueño
// TODO: prevenir que se pueda poner un item con dueño en un horno
// TODO: prevenir tridentes
// TODO: hacer que tradeos setee owner
public final class Ownership extends ReloaderPlugin {

    private final PluginDescriptionFile pdfFile = getDescription();
    private final String version = this.pdfFile.getVersion();
    private MessageSender<Message> messageSender;
    private Settings settings;
    private YamlFile configYaml;
    private YamlFile messagesYaml;
    private OwnershipManager ownershipManager;

    @Override
    public void onEnable() {
        registerFiles();
        this.ownershipManager = new OwnershipManager(this);
        this.messageSender = new MessageSender<>(this, Message.values(), this.messagesYaml, "prefix");
        this.messageSender.send("&aEnabled&f. Version: &e" + this.version);
        this.messageSender.send("&fThank you for using my plugin! &c" + this.pdfFile.getName() + "&f By " + this.pdfFile.getAuthors().get(0));
        this.messageSender.send("&fJoin my discord server at &chttps://bit.ly/MPDiscordSv");
        this.messageSender.send("Please consider subscribing to my yt channel: &c" + this.pdfFile.getWebsite());
        this.settings = new Settings(this);
        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        this.messageSender.send("&cDisabled&f. Version: &e" + this.version);
        this.messageSender.send("&fThank you for using my plugin! &c" + this.pdfFile.getName() + "&f By " + this.pdfFile.getAuthors().get(0));
        this.messageSender.send("&fJoin my discord server at &chttps://bit.ly/MPDiscordSv");
        this.messageSender.send("Please consider subscribing to my yt channel: &c" + this.pdfFile.getWebsite());
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockBreakListener(this), this);
        pm.registerEvents(new PlayerHitListener(this), this);
        pm.registerEvents(new BowShootListener(this), this);
        pm.registerEvents(new ArmorEquipListener(this), this);
        // ArmorEquipEvent events
        pm.registerEvents(new ArmorListener(), this);
        pm.registerEvents(new DispenserArmorListener(), this);
        // Shopkeepers integration
        if (pm.isPluginEnabled("Shopkeepers")) {
            this.messageSender.send("&aShopkeepers integration enabled!");
            pm.registerEvents(new ShopKeeperTradeListener(this), this);
        } else {
            this.messageSender.send("&cShopkeepers integration disabled. Shopkeepers plugin not found.");
        }
    }

    private void registerCommands() {
        PluginCommand mainCommand = getCommand("ownership");

        if (mainCommand == null) {
            this.messageSender.send("&cThe main command has not been registered properly. Disabling Ownership");
            this.setEnabled(false);
            return;
        }
        mainCommand.setExecutor(new MainCommand(this));
        mainCommand.setTabCompleter(new MPTabCompleter(Arrays.asList("help", "version", "reload", "set {PLAYERS}", "remove", "check")));
    }

    private void registerFiles() {
        this.configYaml = new YamlFile(this, "config.yml");
        this.messagesYaml = new YamlFile(this, "messages.yml");
    }


    public OwnershipManager getOwnershipManager() {
        return this.ownershipManager;
    }

    public MessageSender<Message> getMessageSender() {
        return this.messageSender;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public YamlFile getConfigYaml() {
        return this.configYaml;
    }


    /**
     * Gets the plugin's current version.
     *
     * @return The current version.
     */
    public String getVersion() {
        return this.version;
    }

}
