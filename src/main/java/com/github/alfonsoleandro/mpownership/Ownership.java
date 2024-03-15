package com.github.alfonsoleandro.mpownership;

import armorequip.ArmorListener;
import armorequip.DispenserArmorListener;
import com.github.alfonsoleandro.mpownership.command.MainCommand;
import com.github.alfonsoleandro.mpownership.listener.BlockBreakListener;
import com.github.alfonsoleandro.mpownership.listener.PlayerHitListener;
import com.github.alfonsoleandro.mpownership.listener.BowShootListener;
import com.github.alfonsoleandro.mpownership.manager.OwnershipManager;
import com.github.alfonsoleandro.mpownership.utils.Message;
import com.github.alfonsoleandro.mputils.commands.MPTabCompleter;
import com.github.alfonsoleandro.mputils.files.YamlFile;
import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.github.alfonsoleandro.mputils.reloadable.ReloaderPlugin;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;

//TODO: shopkeepers integration
public final class Ownership extends ReloaderPlugin {

    private final PluginDescriptionFile pdfFile = getDescription();
    private final String version = this.pdfFile.getVersion();
    private OwnershipManager ownershipManager;
    private MessageSender<Message> messageSender;
    private YamlFile messagesYaml;

    @Override
    public void onEnable() {
        registerFiles();
        this.ownershipManager = new OwnershipManager(this);
        this.messageSender = new MessageSender<>(this, Message.values(), this.messagesYaml, "prefix");
        registerEvents();
        registerCommands();
        //TODO: enable messages
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //TODO: disable messages
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockBreakListener(this), this);
        pm.registerEvents(new PlayerHitListener(this), this);
        pm.registerEvents(new BowShootListener(this), this);
        //ArmorEquipEvent events
        pm.registerEvents(new ArmorListener(), this);
        pm.registerEvents(new DispenserArmorListener(), this);
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
        this.messagesYaml = new YamlFile(this, "messages.yml");
    }


    public OwnershipManager getOwnershipManager() {
        return this.ownershipManager;
    }

    public MessageSender<Message> getMessageSender() {
        return this.messageSender;
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
