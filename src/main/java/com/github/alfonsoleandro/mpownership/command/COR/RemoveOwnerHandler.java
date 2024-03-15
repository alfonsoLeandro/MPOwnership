package com.github.alfonsoleandro.mpownership.command.COR;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.manager.OwnershipManager;
import com.github.alfonsoleandro.mpownership.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveOwnerHandler extends AbstractHandler {

    private final OwnershipManager ownershipManager;

    public RemoveOwnerHandler(Ownership plugin, AbstractHandler successor) {
        super(plugin, successor);
        this.ownershipManager = plugin.getOwnershipManager();
    }

    @Override
    protected boolean meetsCondition(CommandSender sender, String label, String[] args) {
        return args.length > 0 && args[0].equalsIgnoreCase("remove");
    }

    @Override
    protected void internalHandle(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            this.messageSender.send(sender, Message.CANNOT_SEND_CONSOLE);
            return;
        }
        if (!sender.hasPermission("ownership.remove")) {
            this.messageSender.send(sender, Message.NO_PERMISSION);
            return;
        }

        Player player = (Player) sender;

        this.ownershipManager.removeOwner(player.getInventory().getItemInMainHand());

        this.messageSender.send(sender, Message.OWNER_REMOVED);
    }

}
