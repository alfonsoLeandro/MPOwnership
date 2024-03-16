package com.github.alfonsoleandro.mpownership.listener;

import armorequip.ArmorEquipEvent;
import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.manager.OwnershipManager;
import com.github.alfonsoleandro.mpownership.utils.Message;
import com.github.alfonsoleandro.mputils.message.MessageSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * @author alfonsoLeandro
 */
public class ArmorEquipListener implements Listener {

    private final OwnershipManager ownershipManager;
    private final MessageSender<Message> messageSender;

    public ArmorEquipListener(Ownership plugin) {
        this.ownershipManager = plugin.getOwnershipManager();
        this.messageSender = plugin.getMessageSender();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArmorEquip(ArmorEquipEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("ownership.bypass")) {
            return;
        }
        ItemStack newArmor = event.getNewArmorPiece();
        if (newArmor == null) {
            return;
        }
        String owner = this.ownershipManager.checkOwner(newArmor);
        if (owner != null && !owner.equals(player.getName())) {
            event.setCancelled(true);
            this.messageSender.send(player, Message.CANNOT_USE_ARMOR, "%owner%", owner);
        }
    }
}
