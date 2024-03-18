package com.github.alfonsoleandro.mpownership.command.COR;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.manager.OwnershipManager;
import com.github.alfonsoleandro.mpownership.utils.Message;
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
        if (!sender.hasPermission("ownership.setReadyToOwn")) {
            this.messageSender.send(sender, Message.NO_PERMISSION);
            return;
        }
        Player player = (Player) sender;

        this.ownershipManager.setReadyToOwn(player.getInventory().getItemInMainHand());

        this.messageSender.send(sender, Message.READY_TO_OWN, "%owner%", args[1]);
    }

}
