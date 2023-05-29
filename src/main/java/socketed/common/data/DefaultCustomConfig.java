package socketed.common.data;

import net.minecraft.util.text.TextFormatting;
import socketed.common.data.entry.EffectEntry;
import socketed.common.data.entry.ItemEntry;
import socketed.common.data.entry.OreEntry;

import java.util.*;

public abstract class DefaultCustomConfig {

    public static Map<String, RecipientGroup> getDefaultRecipients() {
        Map<String, RecipientGroup> map = new HashMap<>();

        map.put("diamond_armor", new RecipientGroup("diamond_armor", 3, Arrays.asList(
                new ItemEntry("minecraft:diamond_boots"),
                new ItemEntry("minecraft:diamond_leggings"),
                new ItemEntry("minecraft:diamond_chestplate"),
                new ItemEntry("minecraft:diamond_helmet"))));
        map.put("iron_armor", new RecipientGroup("iron_armor", 2, Arrays.asList(
                new ItemEntry("minecraft:iron_boots"),
                new ItemEntry("minecraft:iron_leggings"),
                new ItemEntry("minecraft:iron_chestplate"),
                new ItemEntry("minecraft:iron_helmet"))));
        map.put("leather_armor", new RecipientGroup("leather_armor", 1, Arrays.asList(
                new ItemEntry("minecraft:leather_boots"),
                new ItemEntry("minecraft:leather_leggings"),
                new ItemEntry("minecraft:leather_chestplate"),
                new ItemEntry("minecraft:leather_helmet"))));

        map.put("swords", new RecipientGroup("swords", 1, Arrays.asList(
                new ItemEntry("minecraft:stone_sword"),
                new ItemEntry("minecraft:iron_sword"),
                new ItemEntry("minecraft:gold_sword"),
                new ItemEntry("minecraft:diamond_sword"))));

        map.put("pickaxe", new RecipientGroup("pickaxe", 2, Arrays.asList(
                new ItemEntry("minecraft:stone_pickaxe"),
                new ItemEntry("minecraft:iron_pickaxe"),
                new ItemEntry("minecraft:gold_pickaxe"),
                new ItemEntry("minecraft:diamond_pickaxe"))));

        map.put("wood", new RecipientGroup("wood", 1, Collections.emptyList(), Arrays.asList(
                new OreEntry("plankWood"),
                new OreEntry("logWood"))));

        return map;
    }

    public static Map<String, EffectGroup> getDefaultEffects() {
        Map<String, EffectGroup> map = new HashMap<>();

        map.put("damage", new EffectGroup("damage", "tooltip.damage.1", TextFormatting.BLUE,
                Arrays.asList("swords", "pickaxe"),
                Collections.singletonList(new EffectEntry("attackDamage", "generic.Damage", 0.1, 1)),
                Collections.singletonList(new ItemEntry("minecraft:diamond"))));

        map.put("defense", new EffectGroup("defense", "tooltip.defense.1", TextFormatting.RED,
                Arrays.asList("diamond_armor", "iron_armor", "leather_armor"),
                Collections.singletonList(new EffectEntry("playerDefense", "generic.Defense", 5, 0)),
                Collections.singletonList(new ItemEntry("minecraft:emerald"))));

        return map;
    }
}