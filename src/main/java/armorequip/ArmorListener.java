package armorequip;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnah
 * @since Jul 30, 2015
 */
public class ArmorListener implements Listener{

	private final List<String> blockedMaterials = new ArrayList<String>(){{
		add("FURNACE");
		add("CHEST");
		add("TRAPPED_CHEST");
		add("BEACON");
		add("DISPENSER");
		add("DROPPER");
		add("HOPPER");
		add("WORKBENCH");
		add("ENCHANTMENT_TABLE");
		add("ENDER_CHEST");
		add("ANVIL");
		add("BED_BLOCK");
		add("FENCE_GATE");
		add("SPRUCE_FENCE_GATE");
		add("BIRCH_FENCE_GATE");
		add("ACACIA_FENCE_GATE");
		add("JUNGLE_FENCE_GATE");
		add("DARK_OAK_FENCE_GATE");
		add("IRON_DOOR_BLOCK");
		add("WOODEN_DOOR");
		add("SPRUCE_DOOR");
		add("BIRCH_DOOR");
		add("JUNGLE_DOOR");
		add("ACACIA_DOOR");
		add("DARK_OAK_DOOR");
		add("WOOD_BUTTON");
		add("STONE_BUTTON");
		add("TRAP_DOOR");
		add("IRON_TRAPDOOR");
		add("DIODE_BLOCK_OFF");
		add("DIODE_BLOCK_ON");
		add("REDSTONE_COMPARATOR_OFF");
		add("REDSTONE_COMPARATOR_ON");
		add("FENCE");
		add("SPRUCE_FENCE");
		add("BIRCH_FENCE");
		add("JUNGLE_FENCE");
		add("DARK_OAK_FENCE");
		add("ACACIA_FENCE");
		add("NETHER_FENCE");
		add("BREWING_STAND");
		add("CAULDRON");
		add("LEGACY_SIGN_POST");
		add("LEGACY_WALL_SIGN");
		add("LEGACY_SIGN");
		add("ACACIA_SIGN");
		add("ACACIA_WALL_SIGN");
		add("BIRCH_SIGN");
		add("BIRCH_WALL_SIGN");
		add("DARK_OAK_SIGN");
		add("DARK_OAK_WALL_SIGN");
		add("JUNGLE_SIGN");
		add("JUNGLE_WALL_SIGN");
		add("OAK_SIGN");
		add("OAK_WALL_SIGN");
		add("SPRUCE_SIGN");
		add("SPRUCE_WALL_SIGN");
		add("LEVER");
		add("BLACK_SHULKER_BOX");
		add("BLUE_SHULKER_BOX");
		add("BROWN_SHULKER_BOX");
		add("CYAN_SHULKER_BOX");
		add("GRAY_SHULKER_BOX");
		add("GREEN_SHULKER_BOX");
		add("LIGHT_BLUE_SHULKER_BOX");
		add("LIME_SHULKER_BOX");
		add("MAGENTA_SHULKER_BOX");
		add("ORANGE_SHULKER_BOX");
		add("PINK_SHULKER_BOX");
		add("PURPLE_SHULKER_BOX");
		add("RED_SHULKER_BOX");
		add("SILVER_SHULKER_BOX");
		add("WHITE_SHULKER_BOX");
		add("YELLOW_SHULKER_BOX");
		add("DAYLIGHT_DETECTOR_INVERTED");
		add("DAYLIGHT_DETECTOR");
		add("BARREL");
		add("BLAST_FURNACE");
		add("SMOKER");
		add("CARTOGRAPHY_TABLE");
		add("COMPOSTER");
		add("GRINDSTONE");
		add("LECTERN");
		add("LOOM");
		add("STONECUTTER");
		add("BELL");
	}};

	//Event Priority is highest because other plugins might cancel the events before we check.
	@EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
	public void inventoryClick(InventoryClickEvent e){
		if(e.getAction() == InventoryAction.NOTHING) return;// Why does this get called if nothing happens??
		if(e.getSlotType() != SlotType.ARMOR && e.getSlotType() != SlotType.QUICKBAR && e.getSlotType() != SlotType.CONTAINER) return;
		if(e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
		if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
		if(!(e.getWhoClicked() instanceof Player)) return;

		boolean shift = e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT);
		boolean numberKey = e.getClick().equals(ClickType.NUMBER_KEY);

		ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());
		if(!shift && newArmorType != null && e.getRawSlot() != newArmorType.getRawSlot()){
			// Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots slot.
			return;
		}
		if(shift){
			newArmorType = ArmorType.matchType(e.getCurrentItem());
			if(newArmorType != null){
				boolean equipping = true;
				if(e.getRawSlot() == newArmorType.getRawSlot()){
					equipping = false;
				}
				if(newArmorType.equals(ArmorType.HELMET) && (equipping == isAirOrNull(e.getWhoClicked().getInventory().getHelmet())) || newArmorType.equals(ArmorType.CHESTPLATE) && (equipping == isAirOrNull(e.getWhoClicked().getInventory().getChestplate())) || newArmorType.equals(ArmorType.LEGGINGS) && (equipping ? isAirOrNull(e.getWhoClicked().getInventory().getLeggings()) : !isAirOrNull(e.getWhoClicked().getInventory().getLeggings())) || newArmorType.equals(ArmorType.BOOTS) && (equipping ? isAirOrNull(e.getWhoClicked().getInventory().getBoots()) : !isAirOrNull(e.getWhoClicked().getInventory().getBoots()))){
					ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType, equipping ? null : e.getCurrentItem(), equipping ? e.getCurrentItem() : null);
					Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
					if(armorEquipEvent.isCancelled()){
						e.setCancelled(true);
					}
				}
			}
		}else{
			ItemStack newArmorPiece = e.getCursor();
			ItemStack oldArmorPiece = e.getCurrentItem();
			if(numberKey){
				if(e.getClickedInventory().getType().equals(InventoryType.PLAYER)){// Prevents shit in the 2by2 crafting
					// e.getClickedInventory() == The players inventory
					// e.getHotBarButton() == key people are pressing to equip or unequip the item to or from.
					// e.getRawSlot() == The slot the item is going to.
					// e.getSlot() == Armor slot, can't use e.getRawSlot() as that gives a hotbar slot ;-;
					ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
					if(!isAirOrNull(hotbarItem)){// Equipping
						newArmorType = ArmorType.matchType(hotbarItem);
						newArmorPiece = hotbarItem;
						oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
					}else{// Unequipping
						newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
					}
				}
			}else{
				if(isAirOrNull(e.getCursor()) && !isAirOrNull(e.getCurrentItem())){// unequip with no new item going into the slot.
					newArmorType = ArmorType.matchType(e.getCurrentItem());
				}
				// e.getCurrentItem() == Unequip
				// e.getCursor() == Equip
				// newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
			}
			if(newArmorType != null && e.getRawSlot() == newArmorType.getRawSlot()){
				ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.PICK_DROP;
				if(e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberKey) method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
				ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), method, newArmorType, oldArmorPiece, newArmorPiece);
				Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
				if(armorEquipEvent.isCancelled()){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority =  EventPriority.HIGHEST)
	public void playerInteractEvent(PlayerInteractEvent e){
		if(e.useItemInHand().equals(Result.DENY))return;
		if(e.getAction() == Action.PHYSICAL) return;

		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			Player player = e.getPlayer();
			if(!e.useInteractedBlock().equals(Result.DENY)){
				if(e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK || player.isSneaking()){// Having both of these checks is useless, might as well do it though.
					// Some blocks have actions when you right click them which stops the client from equipping the armor in hand.
					Material mat = e.getClickedBlock().getType();
					for(String s : blockedMaterials){
						if(mat.name().equalsIgnoreCase(s)) return;
					}
				}
			}
			ArmorType newArmorType = ArmorType.matchType(e.getItem());
			if(newArmorType != null){
				if(newArmorType.equals(ArmorType.HELMET) && isAirOrNull(e.getPlayer().getInventory().getHelmet()) && !(e.getItem().getType().name().endsWith("_SKULL") || e.getItem().getType().name().endsWith("_HEAD"))
						|| newArmorType.equals(ArmorType.CHESTPLATE) && isAirOrNull(e.getPlayer().getInventory().getChestplate())
						|| newArmorType.equals(ArmorType.LEGGINGS) && isAirOrNull(e.getPlayer().getInventory().getLeggings())
						|| newArmorType.equals(ArmorType.BOOTS) && isAirOrNull(e.getPlayer().getInventory().getBoots())){
					ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR, ArmorType.matchType(e.getItem()), null, e.getItem());
					Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
					if(armorEquipEvent.isCancelled()){
						e.setCancelled(true);
						player.updateInventory();
					}
				}
			}
		}
	}
	
	@EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
	public void inventoryDrag(InventoryDragEvent event){
		// getType() seems to always be even.
		// Old Cursor gives the item you are equipping
		// Raw slot is the ArmorType slot
		// Can't replace armor using this method making getCursor() useless.
		ArmorType type = ArmorType.matchType(event.getOldCursor());
		if(event.getRawSlots().isEmpty()) return;// Idk if this will ever happen
		if(type != null && type.getRawSlot() == event.getRawSlots().stream().findFirst().orElse(0)){
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, type, null, event.getOldCursor());
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
			if(armorEquipEvent.isCancelled()){
				event.setResult(Result.DENY);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void itemBreakEvent(PlayerItemBreakEvent e){
		ArmorType type = ArmorType.matchType(e.getBrokenItem());
		if(type != null){
			Player p = e.getPlayer();
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type, e.getBrokenItem(), null);
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
			if(armorEquipEvent.isCancelled()){
				ItemStack i = e.getBrokenItem().clone();
				i.setAmount(1);
				i.setDurability((short) (i.getDurability() - 1));
				if(type.equals(ArmorType.HELMET)){
					p.getInventory().setHelmet(i);
				}else if(type.equals(ArmorType.CHESTPLATE)){
					p.getInventory().setChestplate(i);
				}else if(type.equals(ArmorType.LEGGINGS)){
					p.getInventory().setLeggings(i);
				}else if(type.equals(ArmorType.BOOTS)){
					p.getInventory().setBoots(i);
				}
			}
		}
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent e){
		Player p = e.getEntity();
		if(e.getKeepInventory()) return;
		for(ItemStack i : p.getInventory().getArmorContents()){
			if(!isAirOrNull(i)){
				Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i), i, null));
				// No way to cancel a death event.
			}
		}
	}

	/**
	 * A utility method to support versions that use null or air ItemStacks.
	 */
	public static boolean isAirOrNull(ItemStack item){
		return item == null || item.getType().equals(Material.AIR);
	}
}
