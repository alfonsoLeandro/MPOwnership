package com.github.alfonsoleandro.mpownership.listener;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.manager.OwnershipManager;
import com.github.alfonsoleandro.mpownership.manager.Settings;
import com.github.alfonsoleandro.mpownership.utils.Message;
import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.github.alfonsoleandro.mputils.sound.SoundUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author alfonsoLeandro
 */
public class PlayerHitListener implements Listener {

    private final OwnershipManager ownershipManager;
    private final MessageSender<Message> messageSender;
    private final Settings settings;

    public PlayerHitListener(Ownership plugin) {
        this.ownershipManager = plugin.getOwnershipManager();
        this.messageSender = plugin.getMessageSender();
        this.settings = plugin.getSettings();
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!event.getDamager().getType().equals(EntityType.PLAYER)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (player.hasPermission("ownership.bypass")) {
            return;
        }
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        String owner = this.ownershipManager.checkOwner(itemInHand);
        if (owner != null && !owner.equals(player.getName())) {
            event.setCancelled(true);
            this.messageSender.send(player, Message.CANNOT_HIT_WITH_ITEM, "%owner%", owner);
            if (this.settings.isForbiddenWeaponSoundEnabled()) {
                SoundUtils.playSound(player, this.settings.getForbiddenWeaponSound());
            }
        }
    }

}
