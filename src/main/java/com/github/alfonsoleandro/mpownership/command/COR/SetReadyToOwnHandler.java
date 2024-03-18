package com.github.alfonsoleandro.mpownership.command.COR;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.manager.OwnershipManager;
import com.github.alfonsoleandro.mpownership.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetReadyToOwnHandler extends AbstractHandler {

    private final OwnershipManager ownershipManager;

    public SetReadyToOwnHandler(Ownership plugin, AbstractHandler successor) {
        super(plugin, successor);
        this.ownershipManager = plugin.getOwnershipManager();
    }

    @Override
    protected boolean meetsCondition(CommandSender sender, String label, String[] args) {
        return args.length > 0 && (args[0].equalsIgnoreCase("setReadyToOwn") || args[0].equalsIgnoreCase("setRTO"));
    }

    @Override
    protected void internalHandle(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            this.messageSender.send(sender, Message.CANNOT_SEND_CONSOLE);
            return;
        }
        if (!sender.hasPermission("ownership.set")) {
            this.messageSender.send(sender, Message.NO_PERMISSION);
            return;
        }
        if (args.length < 2) {
            this.messageSender.send(sender, Message.SET_USE, "%command%", label);
            return;
        }
        Player player = (Player) sender;

        this.ownershipManager.setOwner(player.getInventory().getItemInMainHand(), args[1]);

        this.messageSender.send(sender, Message.OWNER_SET, "%owner%", args[1]);

        Player newOwner = Bukkit.getPlayerExact(args[1]);
        if (newOwner == null || !newOwner.isOnline()) {
            this.messageSender.send(sender, Message.NEW_OWNER_OFFLINE, "%owner%", args[1]);
        }
    }

}