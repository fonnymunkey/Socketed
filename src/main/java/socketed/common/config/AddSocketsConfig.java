package socketed.common.config;

import net.minecraftforge.common.config.Config;
import socketed.Socketed;

import java.util.HashMap;

public class AddSocketsConfig {
	@Config.Comment("Default roll counts for item types defined in Socketable Items config. Format: ItemType, RollCount")
	@Config.Name("Socket Roll counts per Item Type")
	public String[] socketRollsConfig = {
			"HELMET, 5",
			"CHESTPLATE, 5",
			"LEGGINGS, 5",
			"BOOTS, 5",
			"SWORD, 5",
			"BOW 1",
			"AXE 3",
			"PICKAXE 1",
			"SHOVEL 1",
			"SHIELD 5"
	};

	@Config.Comment("Minecraft uses material enchantability divided by 4 (enchantability tier, 0-7) to roll enchantments. Chance per socket roll = base chance + this value x item material enchantability tier")
	@Config.Name("Socket roll chance per enchantability tier")
	public float socketRollChancePerEnchantabilityTier = 0.08F;

	@Config.Comment("Base chance when rolling sockets")
	@Config.Name("Socket roll base chance")
	public float socketRollBaseChance = 0.2F;

	@Config.Comment("Use this list to override the base material enchantability calc for socket roll chance for certain items. Format: modid:itemid, rollChance")
	@Config.Name("Socket roll chance by item")
	public String[] socketRollChanceByItem = {
			"iceandfire:armor_silver_metal_helmet, 0.2",
			"iceandfire:armor_silver_metal_chestplate, 0.2",
			"iceandfire:armor_silver_metal_leggings, 0.2",
			"iceandfire:armor_silver_metal_boots, 0.2"
	};

	private final HashMap<String, Integer> socketRollCountByItemType = new HashMap<>();
	private final HashMap<String, Float> socketRollChanceByItemId = new HashMap<>();

    public AddSocketsConfig() {
        for (String s : socketRollsConfig) {
			String[] split = s.split(",");
			try {
				if (split.length == 2)
					socketRollCountByItemType.put(split[0].trim(), Integer.parseInt(split[1].trim()));
				else
					Socketed.LOGGER.warn("Socket roll count config doesn't have correct format {}", s);
			} catch (NumberFormatException exception){
				Socketed.LOGGER.warn("Socket roll count config can't parse roll count {}", s);
			}
		}

		for(String s : socketRollChanceByItem){
			String[] split = s.split(",");
			try {
				if(split.length == 2)
					socketRollChanceByItemId.put(split[0].trim(), Float.parseFloat(split[1].trim()));
				else
					Socketed.LOGGER.warn("Socket roll chance config doesn't have correct format {}", s);
			} catch(NumberFormatException exception) {
				Socketed.LOGGER.warn("Socket roll chance config can't parse roll chance {}", s);
			}
		}
	}

	public int getSocketRollCount(String itemTypeString){
		return socketRollCountByItemType.getOrDefault(itemTypeString, 0);
	}

	public float getSocketRollChance(String itemReg){
		return socketRollChanceByItemId.getOrDefault(itemReg, -1F);
	}
}