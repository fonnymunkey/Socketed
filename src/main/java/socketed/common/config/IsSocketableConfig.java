package socketed.common.config;

import com.google.common.base.Predicate;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import socketed.Socketed;

import java.util.HashMap;
import java.util.Map;

public class IsSocketableConfig {
	@Config.Comment("There are some predefined item types shown in default config. Pattern for any additional types: Name to use;Regex to exactly match item ids.")
	@Config.Name("Item Types that can get sockets")
	public String[] itemTypesConfig = {
			"HELMET",
			"CHESTPLATE",
			"LEGGINGS",
			"BOOTS",
			"SWORD",
			"FISHING_ROD",
			"BOW",
			"AXE",
			"PICKAXE",
			"HOE",
			"SHOVEL",
			"SHIELD",
			//"BATTLEAXE;(mujmajnkraftsbettersurvival\\:item.*battleaxe)|(spartan(defiled|fire|weaponry)\\:battleaxe.*)",
			//"BS_WEAPON;mujmajnkraftsbettersurvival\\:item.*(dagger|nunchaku|hammer|battleaxe)",
			//"SW_CROSSBOW;spartan(defiled|fire|weaponry)\\:crossbow.*"
	};

	private final Map<String, SocketableType> enabledItemTypes = new HashMap<>();
	//This is for other mods adding additional item types
	public void addEnabledItemType(String name, Predicate<ItemStack> canSocket){
		enabledItemTypes.put(name, new SocketableType(canSocket));
	}
	public void addEnabledItemType(String name, String regex){
		enabledItemTypes.put(name, new SocketableType(regex));
	}

    public IsSocketableConfig() {
        HashMap<String, SocketableType> itemTypes = new HashMap<>();
        itemTypes.put("HELMET", new SocketableType(v -> v.getItem() instanceof ItemArmor && ((ItemArmor) v.getItem()).getEquipmentSlot().equals(EntityEquipmentSlot.HEAD)));
		itemTypes.put("CHESTPLATE", new SocketableType(v -> v.getItem() instanceof ItemArmor && ((ItemArmor) v.getItem()).getEquipmentSlot().equals(EntityEquipmentSlot.CHEST)));
		itemTypes.put("LEGGINGS", new SocketableType(v -> v.getItem() instanceof ItemArmor && ((ItemArmor) v.getItem()).getEquipmentSlot().equals(EntityEquipmentSlot.LEGS)));
		itemTypes.put("BOOTS", new SocketableType(v -> v.getItem() instanceof ItemArmor && ((ItemArmor) v.getItem()).getEquipmentSlot().equals(EntityEquipmentSlot.FEET)));
		itemTypes.put("SWORD", new SocketableType(v -> v.getItem() instanceof ItemSword));
		itemTypes.put("BOW", new SocketableType(v -> v.getItem() instanceof ItemBow));
		itemTypes.put("AXE", new SocketableType(v -> v.getItem() instanceof ItemAxe));
		itemTypes.put("PICKAXE", new SocketableType(v -> v.getItem() instanceof ItemPickaxe));
		itemTypes.put("HOE", new SocketableType(v -> v.getItem() instanceof ItemHoe));
		itemTypes.put("SHOVEL", new SocketableType(v -> v.getItem() instanceof ItemSpade));
		itemTypes.put("SHIELD", new SocketableType(v -> v.getItem() instanceof ItemShield));
		itemTypes.put("FISHING_ROD", new SocketableType(v -> v.getItem() instanceof ItemFishingRod));

		for (String s : itemTypesConfig) {
			String[] split = s.split(";");
			if(split.length == 1)
				if(itemTypes.containsKey(s))
					enabledItemTypes.put(s, itemTypes.get(s));
			if (split.length == 2)
				enabledItemTypes.put(split[0], new SocketableType(split[1]));
		}
	}

	public String getSocketableType(ItemStack stack){
		for(HashMap.Entry<String,SocketableType> entry : enabledItemTypes.entrySet())
			if(entry.getValue().canSocket(stack)) return entry.getKey();
		return null;
	}

	private static class SocketableType {
		Predicate<ItemStack> canSocket;

		public SocketableType(Predicate<ItemStack> canSocket){
			this.canSocket = canSocket;
		}

		public SocketableType(String regex){
			this.canSocket = stack -> {
				if (stack == null || stack.isEmpty()) return false;
				ResourceLocation location = stack.getItem().getRegistryName();
				return location != null && location.toString().matches(regex);
			};
		}

		public boolean canSocket(ItemStack stack){
			return canSocket.apply(stack);
		}
	}

	public boolean canSocket(ItemStack stack){
		for(SocketableType type : enabledItemTypes.values())
			if(type.canSocket(stack))
				return true;
		return false;
	}
}