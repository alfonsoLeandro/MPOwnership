package armorequip;

import org.bukkit.inventory.ItemStack;

/**
 * @author Arnah
 * @since Jul 30, 2015
 */
public enum ArmorType{
	HELMET(5, 39), CHESTPLATE(6, 38), LEGGINGS(7, 37), BOOTS(8, 36);

	private final int rawSlot;
	private final int playerInventorySlot;

	ArmorType(int rawSlot, int playerInventorySlot){
		this.rawSlot = rawSlot;
		this.playerInventorySlot = playerInventorySlot;
	}

	/**
	 * Attempts to match the ArmorType for the specified ItemStack.
	 *
	 * @param itemStack The ItemStack to parse the type of.
	 * @return The parsed ArmorType, or null if not found.
	 */
	public static ArmorType matchType(final ItemStack itemStack){
		if(ArmorListener.isAirOrNull(itemStack)) return null;
		String type = itemStack.getType().name();
		if(type.endsWith("_HELMET") || type.endsWith("_SKULL") || type.endsWith("_HEAD")) return HELMET;
		else if(type.endsWith("_CHESTPLATE") || type.equals("ELYTRA")) return CHESTPLATE;
		else if(type.endsWith("_LEGGINGS")) return LEGGINGS;
		else if(type.endsWith("_BOOTS")) return BOOTS;
		else return null;
	}

	public int getRawSlot(){
		return rawSlot;
	}

	public int getPlayerInventorySlot(){
		return playerInventorySlot;
	}
}