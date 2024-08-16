package socketed.common.config;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.util.text.TextFormatting;
import socketed.common.data.EffectGroup;
import socketed.common.data.RecipientGroup;
import socketed.common.data.entry.effect.AttributeEntry;
import socketed.common.data.entry.effect.activatable.PotionEntry;
import socketed.common.data.entry.effect.activatable.SocketedActivationTypes;
import socketed.common.data.entry.filter.ItemEntry;
import socketed.common.data.entry.filter.OreEntry;

import java.util.*;

public abstract class DefaultCustomConfig {

    public static Map<String, RecipientGroup> getDefaultRecipients() {
        Map<String, RecipientGroup> map = new HashMap<>();

        map.put("diamond_armor", new RecipientGroup("diamond_armor", Arrays.asList(
                new ItemEntry("minecraft:diamond_boots"),
                new ItemEntry("minecraft:diamond_leggings"),
                new ItemEntry("minecraft:diamond_chestplate"),
                new ItemEntry("minecraft:diamond_helmet"))));
        map.put("gold_armor", new RecipientGroup("gold_armor", Arrays.asList(
                new ItemEntry("minecraft:golden_boots"),
                new ItemEntry("minecraft:golden_leggings"),
                new ItemEntry("minecraft:golden_chestplate"),
                new ItemEntry("minecraft:golden_helmet"))));
        map.put("iron_armor", new RecipientGroup("iron_armor", Arrays.asList(
                new ItemEntry("minecraft:iron_boots"),
                new ItemEntry("minecraft:iron_leggings"),
                new ItemEntry("minecraft:iron_chestplate"),
                new ItemEntry("minecraft:iron_helmet"))));
        map.put("leather_armor", new RecipientGroup("leather_armor", Arrays.asList(
                new ItemEntry("minecraft:leather_boots"),
                new ItemEntry("minecraft:leather_leggings"),
                new ItemEntry("minecraft:leather_chestplate"),
                new ItemEntry("minecraft:leather_helmet"))));

        map.put("sword", new RecipientGroup("sword", Arrays.asList(
                new ItemEntry("minecraft:diamond_sword"),
                new ItemEntry("minecraft:golden_sword"),
                new ItemEntry("minecraft:iron_sword"),
                new ItemEntry("minecraft:stone_sword"),
                new ItemEntry("minecraft:wooden_sword"))));

        map.put("metal_sword", new RecipientGroup("metal_sword", Arrays.asList(
                new ItemEntry("minecraft:diamond_sword"),
                new ItemEntry("minecraft:golden_sword"),
                new ItemEntry("minecraft:iron_sword"))));

        map.put("diamond_sword", new RecipientGroup("diamond_sword", Arrays.asList(
                new ItemEntry("minecraft:diamond_sword"))));

        map.put("pickaxe", new RecipientGroup("pickaxe", Arrays.asList(
                new ItemEntry("minecraft:diamond_pickaxe"),
                new ItemEntry("minecraft:golden_pickaxe"),
                new ItemEntry("minecraft:iron_pickaxe"),
                new ItemEntry("minecraft:stone_pickaxe"),
                new ItemEntry("minecraft:wooden_pickaxe"))));

        map.put("metal_pickaxe", new RecipientGroup("metal_pickaxe", Arrays.asList(
                new ItemEntry("minecraft:diamond_pickaxe"),
                new ItemEntry("minecraft:golden_pickaxe"),
                new ItemEntry("minecraft:iron_pickaxe"))));

        map.put("diamond_pickaxe", new RecipientGroup("diamond_pickaxe", Arrays.asList(
                new ItemEntry("minecraft:diamond_pickaxe"))));

        return map;
    }

    public static Map<String, EffectGroup> getDefaultEffects() {
        Map<String, EffectGroup> map = new HashMap<>();

        map.put("obsidian_damage", new EffectGroup("obsidian_damage", "socketed.tooltip.default.obsidian_damage", TextFormatting.DARK_PURPLE,
                Arrays.asList(
                        "diamond_pickaxe",
                        "diamond_sword"),
                Collections.singletonList(new AttributeEntry("obsidian_damage", SharedMonsterAttributes.ATTACK_DAMAGE.getName(), 3, 0)),
                Collections.singletonList(new OreEntry("obsidian"))));

        map.put("diamond_damage", new EffectGroup("diamond_damage", "socketed.tooltip.default.diamond_damage", TextFormatting.AQUA,
                Arrays.asList(
                        "metal_pickaxe",
                        "metal_sword"),
                Collections.singletonList(new AttributeEntry("diamond_damage", SharedMonsterAttributes.ATTACK_DAMAGE.getName(), 2, 0)),
                Collections.singletonList(new OreEntry("gemDiamond"))));

        map.put("stone_damage", new EffectGroup("stone_damage", "socketed.tooltip.default.stone_damage", TextFormatting.GRAY,
                Arrays.asList(
                        "pickaxe",
                        "sword"),
                Collections.singletonList(new AttributeEntry("stone_damage", SharedMonsterAttributes.ATTACK_DAMAGE.getName(), 1, 0)),
                Arrays.asList(
                        new OreEntry("stone"),
                        new OreEntry("cobblestone"))));

        map.put("sticky", new EffectGroup("sticky", "socketed.tooltip.default.sticky", TextFormatting.DARK_GREEN,
                Arrays.asList(
                        "diamond_armor",
                        "iron_armor",
                        "gold_armor",
                        "leather_armor"),
                Collections.singletonList(new PotionEntry(MobEffects.SLOWNESS.getRegistryName().toString(), 0, 60, SocketedActivationTypes.PASSIVE)),
                Collections.singletonList(new OreEntry("slimeball"))));

        map.put("lucky", new EffectGroup("lucky", "socketed.tooltip.default.lucky", TextFormatting.GREEN,
                Arrays.asList(
                        "diamond_armor",
                        "iron_armor",
                        "gold_armor",
                        "leather_armor",
                        "pickaxe",
                        "sword"),
                Collections.singletonList(new AttributeEntry("lucky", SharedMonsterAttributes.LUCK.getName(), 2, 0)),
                Collections.singletonList(new OreEntry("gemEmerald"))));

        map.put("streamlined", new EffectGroup("streamlined", "socketed.tooltip.default.streamlined", TextFormatting.AQUA,
                Arrays.asList(
                        "diamond_armor",
                        "iron_armor",
                        "gold_armor",
                        "leather_armor"),
                Collections.singletonList(new AttributeEntry("streamlined", EntityLivingBase.SWIM_SPEED.getName(), 0.1, 1)),
                Collections.singletonList(new OreEntry("gemPrismarine"))));

        map.put("diamond_defense", new EffectGroup("diamond_defense", "socketed.tooltip.default.diamond_defense", TextFormatting.AQUA,
                Arrays.asList(
                        "diamond_armor",
                        "iron_armor",
                        "gold_armor"),
                Collections.singletonList(new AttributeEntry("diamond_defense", SharedMonsterAttributes.ARMOR.getName(), 2, 0)),
                Collections.singletonList(new OreEntry("gemDiamond"))));

        return map;
    }
}