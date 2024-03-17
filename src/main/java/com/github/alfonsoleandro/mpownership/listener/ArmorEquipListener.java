package com.github.alfonsoleandro.mpownership.listener;

import armorequip.ArmorEquipEvent;
import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.manager.OwnershipManager;
import com.github.alfonsoleandro.mpownership.manager.Settings;
import com.github.alfonsoleandro.mpownership.utils.Message;
import com.github.alfonsoleandro.mputils.itemstacks.InventoryUtils;
import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.github.alfonsoleandro.mputils.sound.SoundUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author alfonsoLeandro
 */
public class ArmorEquipListener implements Listener {

    private final Ownership plugin;
    private final OwnershipManager ownershipManager;
    private final MessageSender<Message> messageSender;
    private final Settings settings;

    public ArmorEquipListener(Ownership plugin) {
        this.plugin = plugin;
        this.ownershipManager = plugin.getOwnershipManager();
        this.messageSender = plugin.getMessageSender();
        this.settings = plugin.getSettings();
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
            returnItem(player,
                    event.getType().getPlayerInventorySlot(),
                    player.getGameMode().equals(GameMode.CREATIVE) && event.getMethod().equals(ArmorEquipEvent.EquipMethod.HOTBAR));
            this.messageSender.send(player, Message.CANNOT_USE_ARMOR, "%owner%", owner);
            if (this.settings.isForbiddenArmorSoundEnabled()) {
                SoundUtils.playSound(player, this.settings.getForbiddenArmorSound());
            }
        }
    }

    private void returnItem(Player player, int slot, boolean preventCreativeDupe) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack item = player.getInventory().getItem(slot);
                if (item == null) {
                    return;
                }
                if (!preventCreativeDupe) {
                    if (InventoryUtils.canAdd(item, player.getInventory())) {
                        player.getInventory().addItem(item);
                    } else {
                        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
                player.getInventory().setItem(slot, null);
            }
        }.runTaskLater(this.plugin, 1);
    }

}
