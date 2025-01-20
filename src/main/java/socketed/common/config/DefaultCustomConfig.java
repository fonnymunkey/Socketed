package socketed.common.config;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.text.TextFormatting;
import socketed.common.jsondata.GemType;
import socketed.common.jsondata.entry.effect.AttributeGemEffect;
import socketed.common.jsondata.entry.effect.activatable.EnumActivationType;
import socketed.common.jsondata.entry.effect.activatable.PotionGemEffect;
import socketed.common.jsondata.entry.filter.OreEntry;

import java.util.*;

public abstract class DefaultCustomConfig {

    public static final List<EntityEquipmentSlot> body = Arrays.asList(
            EntityEquipmentSlot.fromString("head"),
            EntityEquipmentSlot.fromString("chest"),
            EntityEquipmentSlot.fromString("legs"),
            EntityEquipmentSlot.fromString("feet"));
    public static final List<EntityEquipmentSlot> hands = Arrays.asList(
            EntityEquipmentSlot.fromString("mainhand"),
            EntityEquipmentSlot.fromString("offhand"));
    public static final List<EntityEquipmentSlot> allSlots = Arrays.asList(
            EntityEquipmentSlot.fromString("mainhand"),
            EntityEquipmentSlot.fromString("offhand"),
            EntityEquipmentSlot.fromString("head"),
            EntityEquipmentSlot.fromString("chest"),
            EntityEquipmentSlot.fromString("legs"),
            EntityEquipmentSlot.fromString("feet"));

    public static Map<String, GemType> getDefaultGemTypes() {
        Map<String, GemType> map = new HashMap<>();

        map.put("obsidian_damage", new GemType("obsidian_damage", "socketed.tooltip.default.obsidian_damage", TextFormatting.DARK_PURPLE,
                Collections.singletonList(new AttributeGemEffect(hands, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), 3, 0)),
                Collections.singletonList(new OreEntry("obsidian")),3));

        map.put("stone_damage", new GemType("stone_damage", "socketed.tooltip.default.stone_damage", TextFormatting.GRAY,
                Collections.singletonList(new AttributeGemEffect(hands, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), 1, 0)),
                Arrays.asList(
                        new OreEntry("stone"),
                        new OreEntry("cobblestone")),1));

        map.put("sticky", new GemType("sticky", "socketed.tooltip.default.sticky", TextFormatting.DARK_GREEN,
                Collections.singletonList(new PotionGemEffect(body,
                        MobEffects.SLOWNESS.getRegistryName().toString(), 0, 60, EnumActivationType.PASSIVE)),
                Collections.singletonList(new OreEntry("slimeball")),0));

        map.put("lucky", new GemType("lucky", "socketed.tooltip.default.lucky", TextFormatting.GREEN,
                Collections.singletonList(new AttributeGemEffect(allSlots, SharedMonsterAttributes.LUCK.getName(), 2, 0)),
                Collections.singletonList(new OreEntry("gemEmerald")),2));

        map.put("streamlined", new GemType("streamlined", "socketed.tooltip.default.streamlined", TextFormatting.AQUA,
                Collections.singletonList(new AttributeGemEffect(body, EntityLivingBase.SWIM_SPEED.getName(), 0.1, 1)),
                Collections.singletonList(new OreEntry("gemPrismarine")),1));

        map.put("diamond", new GemType("diamond", "socketed.tooltip.default.diamond", TextFormatting.AQUA,
                Arrays.asList(
                        new AttributeGemEffect(hands,/*"diamond_damage",*/ SharedMonsterAttributes.ATTACK_DAMAGE.getName(), 2, 0),
                        new AttributeGemEffect(body,/*"diamond_defense",*/ SharedMonsterAttributes.ARMOR.getName(), 2, 0)),
                Collections.singletonList(new OreEntry("gemDiamond")),2));

        return map;
    }
}