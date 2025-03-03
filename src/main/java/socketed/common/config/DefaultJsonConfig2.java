package socketed.common.config;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.util.text.TextFormatting;
import socketed.common.attributes.SocketedAttributes;
import socketed.common.socket.gem.GemCombinationType;
import socketed.common.socket.gem.GemType;
import socketed.common.socket.gem.effect.AttributeGemEffect;
import socketed.common.socket.gem.effect.activatable.*;
import socketed.common.socket.gem.effect.activatable.activator.AttackingActivator;
import socketed.common.socket.gem.effect.activatable.activator.MultiEffectActivator;
import socketed.common.socket.gem.effect.activatable.activator.PassiveActivator;
import socketed.common.socket.gem.effect.activatable.condition.*;
import socketed.common.socket.gem.effect.activatable.target.OtherTarget;
import socketed.common.socket.gem.effect.activatable.target.SelfAOETarget;
import socketed.common.socket.gem.effect.activatable.target.SelfTarget;
import socketed.common.socket.gem.effect.slot.SocketedSlotTypes;
import socketed.common.socket.gem.filter.ItemFilter;
import socketed.common.socket.gem.filter.OreFilter;
import socketed.common.socket.gem.util.RandomValueRange;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class DefaultJsonConfig2 {
    
    private static final Map<String, GemType> defaultGemTypes = new HashMap<>();
    private static final Map<String, GemCombinationType> defaultGemCombinationTypes = new HashMap<>();

    public static Map<String, GemType> getDefaultGemTypes() {
        return defaultGemTypes;
    }
    
    public static Map<String, GemCombinationType> getDefaultGemCombinationTypes() {
        return defaultGemCombinationTypes;
    }
    
    public static void registerDefaultGemType(String name, GemType gemType) {
        defaultGemTypes.put(name, gemType);
    }
    
    public static void registerDefaultGemCombinationType(String name, GemCombinationType gemCombinationType) {
        defaultGemCombinationTypes.put(name, gemCombinationType);
    }
    
    public static void initializeBuiltinEntries() {
        //Tier 0
        registerDefaultGemType("poison_potato", new GemType(
                "socketed.tooltip.default.poison_potato",
                0,
                TextFormatting.DARK_GREEN,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.HEAD,
                        new PassiveActivator(null, 9),
                        Arrays.asList(new SelfTarget(null), new SelfAOETarget(null, 4)),
                        MobEffects.POISON.getRegistryName().toString(),
                        0,
                        10)),
                Collections.singletonList(new ItemFilter("minecraft:poisonous_potato", 0 , false))));

        registerDefaultGemType("spider_eye", new GemType(
                "socketed.tooltip.default.spider_eye",
                0,
                TextFormatting.DARK_RED,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.CHEST,
                        new PassiveActivator(null, 9),
                        Arrays.asList(new SelfTarget(null), new SelfAOETarget(null, 4)),
                        MobEffects.WEAKNESS.getRegistryName().toString(),
                        0,
                        10)),
                Collections.singletonList(new ItemFilter("minecraft:spider_eye", 0 , false))));

        registerDefaultGemType("slimeball", new GemType(
                "socketed.tooltip.default.slimeball",
                0,
                TextFormatting.GREEN,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.LEGS,
                        new PassiveActivator(null, 9),
                        Arrays.asList(new SelfTarget(null), new SelfAOETarget(null, 4)),
                        MobEffects.SLOWNESS.getRegistryName().toString(),
                        0,
                        10)),
                Collections.singletonList(new OreFilter("slimeball"))));

        registerDefaultGemType("sugar", new GemType(
                "socketed.tooltip.default.sugar",
                0,
                TextFormatting.WHITE,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.FEET,
                        new PassiveActivator(null, 9),
                        Arrays.asList(new SelfTarget(null), new SelfAOETarget(null, 4)),
                        MobEffects.SPEED.getRegistryName().toString(),
                        0,
                        10)),
                Collections.singletonList(new ItemFilter("minecraft:sugar", 0 , false))));

        registerDefaultGemType("dead_bush", new GemType(
                "socketed.tooltip.default.dead_bush",
                0,
                TextFormatting.GOLD,
                Collections.singletonList(new AttributeGemEffect(
                        SocketedSlotTypes.HAND,
                        SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                        new RandomValueRange(0.5F, false),
                        0)),
                Collections.singletonList(new ItemFilter("minecraft:deadbush", 0 , false))));

        registerDefaultGemType("iron", new GemType(
                "socketed.tooltip.default.iron",
                0,
                TextFormatting.GRAY,
                Arrays.asList(
                        new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(1, 1, true), 0),
                        new AttributeGemEffect(SocketedSlotTypes.BODY, SharedMonsterAttributes.ARMOR.getName(), new RandomValueRange(1, false), 0)),
                Collections.singletonList(new OreFilter("blockIron"))));

        registerDefaultGemType("coal", new GemType(
                "socketed.tooltip.default.coal",
                0,
                TextFormatting.DARK_GRAY,
                Collections.singletonList(new IgniteGemEffect(
                        SocketedSlotTypes.HAND,
                        new AttackingActivator(null, true),
                        Collections.singletonList(new OtherTarget(null)),
                        3)),
                Collections.singletonList(new OreFilter("blockCoal"))));

        //Tier 1

        registerDefaultGemType("blazerod", new GemType(
                "socketed.tooltip.default.blazerod",
                1,
                TextFormatting.DARK_RED,
                Collections.singletonList(new IgniteGemEffect(
                        SocketedSlotTypes.HAND,
                        new AttackingActivator(null, true),
                        Collections.singletonList(new OtherTarget(null)),
                        6)),
                Collections.singletonList(new ItemFilter("minecraft:blaze_rod", 0 , false))));

        registerDefaultGemType("glowstone", new GemType(
                "socketed.tooltip.default.glowstone",
                1,
                TextFormatting.YELLOW,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.HEAD,
                        new PassiveActivator(null, 40),
                        Collections.singletonList(new SelfAOETarget(null, 4)),
                        MobEffects.GLOWING.getRegistryName().toString(),
                        0,
                        41)),
                Collections.singletonList(new OreFilter("dustGlowstone"))));

        registerDefaultGemType("glowstone_block", new GemType(
                "socketed.tooltip.default.glowstone_block",
                1,
                TextFormatting.YELLOW,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.HEAD,
                        new PassiveActivator(null, 40),
                        Collections.singletonList(new SelfAOETarget(null, 12)),
                        MobEffects.GLOWING.getRegistryName().toString(),
                        0,
                        41)),
                Collections.singletonList(new OreFilter("glowstone"))));

        registerDefaultGemType("redstone", new GemType(
                "socketed.tooltip.default.redstone",
                1,
                TextFormatting.RED,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.CHEST,
                        new PassiveActivator(null, 40),
                        Collections.singletonList(new SelfTarget(null)),
                        MobEffects.HASTE.getRegistryName().toString(),
                        0,
                        41)),
                Collections.singletonList(new OreFilter("blockRedstone"))));

        registerDefaultGemType("cobweb", new GemType(
                "socketed.tooltip.default.cobweb",
                1,
                TextFormatting.WHITE,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.LEGS,
                        new PassiveActivator(null, 9),
                        Collections.singletonList(new SelfAOETarget(null, 6)),
                        MobEffects.SLOWNESS.getRegistryName().toString(),
                        0,
                        10)),
                Collections.singletonList(new ItemFilter("minecraft:web", 0, false))));

        registerDefaultGemType("emerald", new GemType(
                "socketed.tooltip.default.emerald",
                1,
                TextFormatting.GREEN,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.BODY,
                        new PassiveActivator(null, 9),
                        Collections.singletonList(new SelfTarget(null)),
                        MobEffects.LUCK.getRegistryName().toString(),
                        0,
                        10)),
                Collections.singletonList(new OreFilter("gemEmerald"))));

        registerDefaultGemType("gold_carrot", new GemType(
                "socketed.tooltip.default.gold_carrot",
                1,
                TextFormatting.YELLOW,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.HEAD,
                        new PassiveActivator(
                                new MultiCondition(MultiCondition.ConditionLogicType.AND, Arrays.asList(
                                        new ElevationCondition(60, ComparingCondition.ConditionComparisonType.LESS_EQUAL, true),
                                        new LightLevelCondition(7, ComparingCondition.ConditionComparisonType.LESS, true))),
                                40),
                        Collections.singletonList(new SelfTarget(null)),
                        MobEffects.NIGHT_VISION.getRegistryName().toString(),
                        0,
                        41)),
                Collections.singletonList(new ItemFilter("minecraft:golden_carrot",0,false))));

        registerDefaultGemType("piston", new GemType(
                "socketed.tooltip.default.piston",
                1,
                TextFormatting.AQUA,
                Collections.singletonList(new KnockbackGemEffect(
                        SocketedSlotTypes.HAND,
                        new AttackingActivator(null, true),
                        Collections.singletonList(new OtherTarget(null)),
                        2.0F,
                        false)),
                Collections.singletonList(new ItemFilter("minecraft:piston", 0, false))));

        registerDefaultGemType("sticky_piston", new GemType(
                "socketed.tooltip.default.sticky_piston",
                1,
                TextFormatting.GREEN,
                Collections.singletonList(new KnockbackGemEffect(
                        SocketedSlotTypes.HAND,
                        new AttackingActivator(null, true),
                        Collections.singletonList(new OtherTarget(null)),
                        2.0F,
                        true)),
                Collections.singletonList(new ItemFilter("minecraft:sticky_piston", 0, false))));

        registerDefaultGemType("diamond", new GemType(
                "socketed.tooltip.default.diamond",
                1,
                TextFormatting.AQUA,
                Arrays.asList(
                        new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(2, false), 0),
                        new AttributeGemEffect(SocketedSlotTypes.BODY, SharedMonsterAttributes.ARMOR.getName(), new RandomValueRange(1, false), 0),
                        new AttributeGemEffect(SocketedSlotTypes.BODY, SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new RandomValueRange(1, false), 0)),
                Collections.singletonList(new OreFilter("gemDiamond"))));

        registerDefaultGemType("obsidian_damage", new GemType(
                "socketed.tooltip.default.obsidian_damage",
                1,
                TextFormatting.DARK_PURPLE,
                Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.BODY, SocketedAttributes.DURABILITY.getName(), new RandomValueRange(0.08F, false), 1)),
                Collections.singletonList(new OreFilter("obsidian"))));

        //Tier 2

        registerDefaultGemType("gapple", new GemType(
                "socketed.tooltip.default.gapple",
                2,
                TextFormatting.GOLD,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.HAND,
                        new PassiveActivator(null, 199),
                        Collections.singletonList(new SelfTarget(null)),
                        MobEffects.ABSORPTION.getRegistryName().toString(),
                        0,
                        200)),
                Collections.singletonList(new ItemFilter("minecraft:golden_apple", 0, true))));

        registerDefaultGemType("sponge", new GemType(
                "socketed.tooltip.default.sponge",
                2,
                TextFormatting.AQUA,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.HAND,
                        new PassiveActivator(null, 9),
                        Collections.singletonList(new SelfTarget(null)),
                        MobEffects.WATER_BREATHING.getRegistryName().toString(),
                        0,
                        10)),
                Collections.singletonList(new ItemFilter("minecraft:sponge", 0, true))));



        //Tier 3




        //Gem Types
        registerDefaultGemType("stone_damage", new GemType("socketed.tooltip.default.stone_damage", 1, TextFormatting.GRAY,
                                            Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(1, false), 0)),
                                            Arrays.asList(new OreFilter("stone"),
                                                          new OreFilter("cobblestone"))));
        registerDefaultGemType("lucky", new GemType("socketed.tooltip.default.lucky", 2, TextFormatting.GREEN,
                                     Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.ALL, SharedMonsterAttributes.LUCK.getName(), new RandomValueRange(2, false), 0)),
                                     Collections.singletonList(new OreFilter("gemEmerald"))));
        registerDefaultGemType("streamlined", new GemType("socketed.tooltip.default.streamlined", 1, TextFormatting.AQUA,
                                           Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.BODY, EntityLivingBase.SWIM_SPEED.getName(), new RandomValueRange(0.1F, false), 1)),
                                           Collections.singletonList(new OreFilter("gemPrismarine"))));
        registerDefaultGemType("redstone", new GemType("socketed.tooltip.default.redstone", 2, TextFormatting.RED,
                                        Arrays.asList(
                                                new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_SPEED.getName(), new RandomValueRange(0.05F, false), 1),
                                                new AttributeGemEffect(SocketedSlotTypes.BODY, SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new RandomValueRange(0.05F, false), 1)),
                                        Collections.singletonList(new OreFilter("dustRedstone"))));

    }
}