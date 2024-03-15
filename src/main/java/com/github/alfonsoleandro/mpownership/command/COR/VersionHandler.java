package com.github.alfonsoleandro.mpownership.command.COR;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.utils.Message;
import org.bukkit.command.CommandSender;

/**
 * Handles the version check command - shows the plugin's current version.
 */
public class VersionHandler extends AbstractHandler {

    public VersionHandler(Ownership plugin, AbstractHandler successor) {
        super(plugin, successor);
    }

    @Override
    protected boolean meetsCondition(CommandSender sender, String label, String[] args) {
        return args.length > 0 && args[0].equalsIgnoreCase("version");
    }

    @Override
    protected void internalHandle(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("ownership.version")) {
            this.messageSender.send(sender, Message.NO_PERMISSION);
            return;
        }

        this.messageSender.send(sender, "&fVersion: &e" + this.plugin.getVersion());
    }

}
