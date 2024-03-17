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
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author alfonsoLeandro
 */
public class BowShootListener implements Listener {

    private final OwnershipManager ownershipManager;
    private final MessageSender<Message> messageSender;
    private final Settings settings;

    public BowShootListener(Ownership plugin) {
        this.ownershipManager = plugin.getOwnershipManager();
        this.messageSender = plugin.getMessageSender();
        this.settings = plugin.getSettings();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectileLaunch(EntityShootBowEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (player.hasPermission("ownership.bypass")) {
            return;
        }
        ItemStack bow = event.getBow();
        if (bow == null) {
            return;
        }

        String owner = this.ownershipManager.checkOwner(bow);
        if (owner != null && !owner.equals(player.getName())) {
            event.setCancelled(true);
            this.messageSender.send(player, Message.CANNOT_SHOOT_WITH_BOW, "%owner%", owner);
            if (this.settings.isForbiddenBowSoundEnabled()) {
                SoundUtils.playSound(player, this.settings.getForbiddenBowSound());
            }
        }
    }
}
