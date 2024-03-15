package com.github.alfonsoleandro.mpownership.manager;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mputils.itemstacks.MPItemStacks;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author alfonsoLeandro
 */
public class OwnershipManager {

    private final Ownership plugin;

    public OwnershipManager(Ownership plugin) {
        this.plugin = plugin;
    }

    public void setOwner(ItemStack item, String playerName) {
        if (!item.hasItemMeta()) {
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(new NamespacedKey(this.plugin, "owner"), PersistentDataType.STRING, playerName);
        persistentDataContainer.set(new NamespacedKey(this.plugin, "id"), PersistentDataType.STRING, UUID.randomUUID().toString());
        item.setItemMeta(itemMeta);

        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%owner%", playerName);
        MPItemStacks.replacePlaceholders(item, placeholders);
    }

    public void removeOwner(ItemStack item) {
        if (!item.hasItemMeta()) {
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.remove(new NamespacedKey(this.plugin, "owner"));
        persistentDataContainer.remove(new NamespacedKey(this.plugin, "id"));
        item.setItemMeta(itemMeta);
    }

    public String checkOwner(ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (persistentDataContainer.has(new NamespacedKey(this.plugin, "owner"), PersistentDataType.STRING)) {
            return persistentDataContainer.get(new NamespacedKey(this.plugin, "owner"), PersistentDataType.STRING);
        }
        return null;
    }
}
