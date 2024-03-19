package com.github.alfonsoleandro.mpownership.listener;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.manager.OwnershipManager;
import com.github.alfonsoleandro.mpownership.manager.Settings;
import com.github.alfonsoleandro.mpownership.utils.Message;
import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.github.alfonsoleandro.mputils.sound.SoundUtils;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * @author alfonsoLeandro
 */
public class ShopKeeperTradeListener implements Listener {

    private final MessageSender<Message> messageSender;
    private final OwnershipManager ownershipManager;
    private final Settings settings;

    public ShopKeeperTradeListener(Ownership plugin) {
        this.ownershipManager = plugin.getOwnershipManager();
        this.messageSender = plugin.getMessageSender();
        this.settings = plugin.getSettings();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
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
                playForbiddenTradeSound(player);
                return;
            }
        }

        if (owner1 != null && !owner1.equals(player.getName())) {
            event.setCancelled(true);
            this.messageSender.send(player, Message.CANNOT_TRADE, "%owner%", owner1);
            playForbiddenTradeSound(player);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSuccessfulTrade(ShopkeeperTradeEvent event) {
        Player player = event.getPlayer();
        UnmodifiableItemStack result = event.getResultItem();

        if (result == null) {
            return;
        }

        ItemStack resultItem = result.copy();

        if (!this.ownershipManager.isReadyToOwn(resultItem)) {
            return;
        }

        if (!player.hasPermission("ownership.autoOwn")) {
            event.setCancelled(true);
            this.messageSender.send(player, Message.CANNOT_AUTO_OWN);
            return;
        }

        this.ownershipManager.setOwner(resultItem, player.getName());
        event.setResultItem(UnmodifiableItemStack.of(resultItem));

        this.messageSender.send(player, Message.YOU_NOW_OWN);
        playNewlyOwnedSound(player);
    }

    private void playForbiddenTradeSound(Player player) {
        if (this.settings.isForbiddenTradeSoundEnabled()) {
            SoundUtils.playSound(player, this.settings.getForbiddenTradeSound());
        }
    }

    private void playNewlyOwnedSound(Player player) {
        if (this.settings.isNewlyOwnedSoundEnabled()) {
            SoundUtils.playSound(player, this.settings.getNewlyOwnedSound());
        }
    }
}
