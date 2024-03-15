package com.github.alfonsoleandro.mpownership.listener;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.manager.OwnershipManager;
import com.github.alfonsoleandro.mpownership.utils.Message;
import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * @author alfonsoLeandro
 */
public class ShopKeeperTradeListener implements Listener {

    private final OwnershipManager ownershipManager;
    private final MessageSender<Message> messageSender;

    public ShopKeeperTradeListener(Ownership plugin) {
        this.ownershipManager = plugin.getOwnershipManager();
        this.messageSender = plugin.getMessageSender();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTrade(ShopkeeperTradeEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("ownership.bypass")) {
            return;
        }
        UnmodifiableItemStack offered1 = event.getOfferedItem1();
        UnmodifiableItemStack offered2 = event.getOfferedItem2();
        String owner1 = this.ownershipManager.checkOwner(offered1.copy());

        if (offered2 != null) {
            String owner2 = this.ownershipManager.checkOwner(offered2.copy());

            if (owner2 != null && !owner2.equals(player.getName())) {
                event.setCancelled(true);
                this.messageSender.send(player, Message.CANNOT_TRADE, "%owner%", owner2);
                return;
            }
        }

        if (owner1 != null && !owner1.equals(player.getName())) {
            event.setCancelled(true);
            this.messageSender.send(player, Message.CANNOT_TRADE, "%owner%", owner1);
        }
    }
}
