package socketed.common.config;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.util.text.TextFormatting;
import socketed.common.jsondata.GemCombinationType;
import socketed.common.jsondata.GemType;
import socketed.common.jsondata.entry.RandomValueRange;
import socketed.common.jsondata.entry.effect.AttributeGemEffect;
import socketed.common.jsondata.entry.effect.activatable.SocketedActivationTypes;
import socketed.common.jsondata.entry.effect.activatable.PotionGemEffect;
import socketed.common.jsondata.entry.filter.OreEntry;
import socketed.common.jsondata.entry.effect.slot.SocketedSlotTypes;

import java.util.*;

public abstract class DefaultJsonConfig {

    public static Map<String, GemType> getDefaultGemTypes() {
        Map<String, GemType> map = new HashMap<>();

        map.put("obsidian_damage", new GemType("socketed.tooltip.default.obsidian_damage", TextFormatting.DARK_PURPLE,
                Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(3, false), 0)),
                Collections.singletonList(new OreEntry("obsidian")),3));

        map.put("stone_damage", new GemType("socketed.tooltip.default.stone_damage", TextFormatting.GRAY,
                Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(1, false), 0)),
                Arrays.asList(
                        new OreEntry("stone"),
                        new OreEntry("cobblestone")),1));

        map.put("sticky", new GemType("socketed.tooltip.default.sticky", TextFormatting.DARK_GREEN,
                Collections.singletonList(new PotionGemEffect(SocketedSlotTypes.BODY,
                                                              MobEffects.SLOWNESS.getRegistryName().toString(), 0, 60, SocketedActivationTypes.PASSIVE_SELF)),
                Collections.singletonList(new OreEntry("slimeball")),0));

        map.put("lucky", new GemType("socketed.tooltip.default.lucky", TextFormatting.GREEN,
                Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.ALL, SharedMonsterAttributes.LUCK.getName(), new RandomValueRange(2, false), 0)),
                Collections.singletonList(new OreEntry("gemEmerald")),2));

        map.put("streamlined", new GemType("socketed.tooltip.default.streamlined", TextFormatting.AQUA,
                Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.BODY, EntityLivingBase.SWIM_SPEED.getName(), new RandomValueRange(0.1F, false), 1)),
                Collections.singletonList(new OreEntry("gemPrismarine")),1));

        map.put("diamond", new GemType("socketed.tooltip.default.diamond", TextFormatting.AQUA,
                Arrays.asList(
                        new AttributeGemEffect(SocketedSlotTypes.HAND,/*"diamond_damage",*/ SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(2, 3, true), 0),
                        new AttributeGemEffect(SocketedSlotTypes.BODY,/*"diamond_defense",*/ SharedMonsterAttributes.ARMOR.getName(), new RandomValueRange(2, false), 0)),
                Collections.singletonList(new OreEntry("gemDiamond")),2));

        map.put("redstone", new GemType("socketed.tooltip.default.redstone", TextFormatting.RED,
                Arrays.asList(
                        new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_SPEED.getName(), new RandomValueRange(0.05F, false), 1),
                        new AttributeGemEffect(SocketedSlotTypes.BODY, SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new RandomValueRange(0.05F, false), 1)),
                Collections.singletonList(new OreEntry("dustRedstone")),2));

        map.put("lapis", new GemType("socketed.tooltip.default.lapis", TextFormatting.BLUE,
                Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.ALL, "socketed.attributes.enchantingpower", new RandomValueRange(2, false), 0)),
                Collections.singletonList(new OreEntry("gemLapis")),2));

        map.put("glowstone", new GemType("socketed.tooltip.default.glowstone", TextFormatting.YELLOW,
                Collections.singletonList(new PotionGemEffect(SocketedSlotTypes.HEAD, MobEffects.GLOWING.getRegistryName().toString(), 0, 60, SocketedActivationTypes.PASSIVE_FAR)),
                Collections.singletonList(new OreEntry("dustGlowstone")),1));

        return map;
    }

    public static Map<String, GemCombinationType> getDefaultGemCombinations() {
        Map<String, GemCombinationType> map = new HashMap<>();

        map.put("three_diamonds", new GemCombinationType("socketed.tooltip.default.three_diamonds", TextFormatting.DARK_BLUE,
                Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(8, false), 0)),
                false,false, true,
                Arrays.asList("diamond", "diamond", "diamond")));

        map.put("rgb", new GemCombinationType("socketed.tooltip.default.rgb", TextFormatting.GOLD,
                Arrays.asList(
                        new PotionGemEffect(SocketedSlotTypes.ALL, MobEffects.GLOWING.getRegistryName().toString(), 0, 60, SocketedActivationTypes.PASSIVE_SELF),
                        new PotionGemEffect(SocketedSlotTypes.ALL, MobEffects.GLOWING.getRegistryName().toString(), 0, 60, SocketedActivationTypes.PASSIVE_NEARBY)),
                true,true, false,
                Arrays.asList("redstone", "lucky", "lapis")));

        return map;
    }
}