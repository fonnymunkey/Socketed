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
import socketed.common.socket.gem.effect.activatable.activator.*;
import socketed.common.socket.gem.effect.activatable.condition.*;
import socketed.common.socket.gem.effect.activatable.target.*;
import socketed.common.socket.gem.effect.slot.SocketedSlotTypes;
import socketed.common.socket.gem.filter.ItemFilter;
import socketed.common.socket.gem.filter.OreFilter;
import socketed.common.socket.gem.util.RandomValueRange;

import java.util.*;

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

    private static List<GenericTarget> ONLYSELF = Collections.singletonList(new SelfTarget(null));
    private static List<GenericTarget> ONLYOTHER = Collections.singletonList(new OtherTarget(null));

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

        registerDefaultGemType("gunpowder", new GemType(
                "socketed.tooltip.default.gunpowder",
                0,
                TextFormatting.RED,
                Collections.singletonList(new ExplosionGemEffect(
                        SocketedSlotTypes.HAND,
                        new AttackingActivator(new ChanceCondition(0.1F), true),
                        Collections.singletonList(new OtherTarget(null)),
                        1, false)),
                Collections.singletonList(new OreFilter("gunpowder"))));

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
                        ONLYSELF,
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
                        ONLYSELF,
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
                        ONLYSELF,
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
                        ONLYOTHER,
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
                        ONLYOTHER,
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
                TextFormatting.YELLOW,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.ALL,
                        new PassiveActivator(null, 400),
                        ONLYSELF,
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
                        ONLYSELF,
                        MobEffects.WATER_BREATHING.getRegistryName().toString(),
                        0,
                        10)),
                Collections.singletonList(new ItemFilter("minecraft:sponge", 0, true))));

        registerDefaultGemType("witherskull", new GemType(
                "socketed.tooltip.default.witherskull",
                2,
                TextFormatting.DARK_GRAY,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.HAND,
                        new AttackingActivator(new MultiCondition(MultiCondition.ConditionLogicType.AND,Arrays.asList(new ChanceCondition(0.25F), new HealthPercentCondition(0.25F, ComparingCondition.ConditionComparisonType.LESS_EQUAL, false))), true),
                        Arrays.asList(new OtherTarget(null), new OtherAOETarget(null,3)),
                        MobEffects.WITHER.getRegistryName().toString(),
                        0,
                        200)),
                Collections.singletonList(new ItemFilter("minecraft:skull", 1, true))));

        registerDefaultGemType("anvil", new GemType(
                "socketed.tooltip.default.anvil",
                2,
                TextFormatting.GRAY,
                Arrays.asList(
                        new KnockbackGemEffect(
                            SocketedSlotTypes.HAND,
                            new AttackingActivator(null, true),
                            ONLYOTHER,
                            4.0F,
                            false),
                        new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(2, false), 0)),
                Collections.singletonList(new ItemFilter("minecraft:anvil", 0, true))));

        registerDefaultGemType("rabbitfoot", new GemType(
                "socketed.tooltip.default.rabbitfoot",
                2,
                TextFormatting.YELLOW,
                Arrays.asList(
                        new PotionGemEffect(
                                SocketedSlotTypes.FEET,
                                new PassiveActivator(null, 9),
                                ONLYSELF,
                                MobEffects.JUMP_BOOST.getRegistryName().toString(),
                                0,
                                10),
                        new AttributeGemEffect(SocketedSlotTypes.FEET, SharedMonsterAttributes.LUCK.getName(), new RandomValueRange(1, false), 0)),
                Collections.singletonList(new ItemFilter("minecraft:rabbit_foot", 0, false))));

        registerDefaultGemType("pufferfish", new GemType(
                "socketed.tooltip.default.pufferfish",
                2,
                TextFormatting.DARK_GRAY,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.HAND,
                        new AttackingActivator(new MultiCondition(MultiCondition.ConditionLogicType.AND,Arrays.asList(new ChanceCondition(0.25F), new HealthPercentCondition(0.25F, ComparingCondition.ConditionComparisonType.LESS_EQUAL, false))), true),
                        Arrays.asList(new OtherTarget(null), new OtherAOETarget(null,3)),
                        MobEffects.POISON.getRegistryName().toString(),
                        0,
                        200)),
                Collections.singletonList(new ItemFilter("minecraft:fish", 3, true))));

        registerDefaultGemType("sealantern", new GemType(
                "socketed.tooltip.default.sealantern",
                2,
                TextFormatting.AQUA,
                Collections.singletonList(new PotionGemEffect(
                        SocketedSlotTypes.HAND,
                        new AttackingActivator(new MultiCondition(MultiCondition.ConditionLogicType.AND,Arrays.asList(new ChanceCondition(0.25F), new HealthPercentCondition(0.25F, ComparingCondition.ConditionComparisonType.LESS_EQUAL, false))), true),
                        Arrays.asList(new OtherTarget(null), new OtherAOETarget(null,3)),
                        MobEffects.BLINDNESS.getRegistryName().toString(),
                        0,
                        200)),
                Collections.singletonList(new ItemFilter("minecraft:sea_lantern", 0, false))));

        registerDefaultGemType("tropicalfish", new GemType(
                "socketed.tooltip.default.tropicalfish",
                2,
                TextFormatting.AQUA,
                Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.FEET, EntityLivingBase.SWIM_SPEED.getName(), new RandomValueRange(0.3F, false), 1)),
                Collections.singletonList(new ItemFilter("minecraft:fish", 2, true))));

        registerDefaultGemType("obsidian", new GemType(
                "socketed.tooltip.default.obsidian",
                2,
                TextFormatting.DARK_GRAY,
                Arrays.asList(
                        new PotionGemEffect(
                                SocketedSlotTypes.HAND,
                                new AttackingActivator(new ChanceCondition(0.2F), true),
                                ONLYSELF,
                                MobEffects.STRENGTH.getRegistryName().toString(),
                                0,
                                100),
                        new AttributeGemEffect(SocketedSlotTypes.BODY, SocketedAttributes.DURABILITY.getName(), new RandomValueRange(0.1F, false), 1)),
                Collections.singletonList(new OreFilter("obisidian"))));

        registerDefaultGemType("tnt", new GemType(
                "socketed.tooltip.default.tnt",
                2,
                TextFormatting.RED,
                Collections.singletonList(new ExplosionGemEffect(
                        SocketedSlotTypes.HAND,
                        new AttackingActivator(new ChanceCondition(0.2F), true),
                        Collections.singletonList(new OtherTarget(null)),
                        2, false)),
                Collections.singletonList(new ItemFilter("minecraft:tnt", 0, false))));

        //Tier 3

        registerDefaultGemType("ghasttear", new GemType(
                "socketed.tooltip.default.ghasttear",
                3,
                TextFormatting.WHITE,
                Collections.singletonList(new MultiEffectGemEffect(
                        SocketedSlotTypes.CHEST,
                        new PassiveActivator(null, 9),
                        ONLYSELF,
                        Arrays.asList(
                                new PotionGemEffect(
                                        SocketedSlotTypes.ALL,
                                        new MultiEffectActivator(null),
                                        ONLYSELF,
                                        MobEffects.REGENERATION.getRegistryName().toString(),
                                        0,
                                        10),
                                new PotionGemEffect(
                                        SocketedSlotTypes.ALL,
                                        new MultiEffectActivator(null),
                                        ONLYSELF,
                                        MobEffects.HEALTH_BOOST.getRegistryName().toString(),
                                        0,
                                        10)
                        )
                )),
                Collections.singletonList(new ItemFilter("minecraft:ghast_tear", 0, false))));

        registerDefaultGemType("totem", new GemType(
                "socketed.tooltip.default.totem",
                3,
                TextFormatting.YELLOW,
                Collections.singletonList(new MultiEffectGemEffect(
                            SocketedSlotTypes.OFFHAND,
                            new DeathTotemCheckActivator(new ChanceCondition(0.2F)),
                            ONLYSELF,
                            Arrays.asList(
                                    new UndyingTotemGemEffect(SocketedSlotTypes.ALL, new MultiEffectActivator(null), ONLYSELF),
                                    new CancelEventGemEffect(SocketedSlotTypes.ALL, new MultiEffectActivator(null), ONLYSELF)
                            )
                )),
                Collections.singletonList(new ItemFilter("minecraft:totem_of_undying", 0, false))));

        registerDefaultGemType("endcrystal", new GemType(
                "socketed.tooltip.default.endcrystal",
                3,
                TextFormatting.LIGHT_PURPLE,
                Collections.singletonList(new UndyingTotemGemEffect(
                        SocketedSlotTypes.HAND,
                        new AttackingActivator(new MultiCondition(MultiCondition.ConditionLogicType.AND, Arrays.asList(new ChanceCondition(0.05F), new InvertedCondition(new IsBossCondition()))), true),
                        ONLYOTHER
                )),
                Collections.singletonList(new ItemFilter("minecraft:end_crystal", 0, false))));

        registerDefaultGemType("beacon", new GemType(
                "socketed.tooltip.default.beacon",
                3,
                TextFormatting.WHITE,
                Arrays.asList(
                        new MultiEffectGemEffect(SocketedSlotTypes.HEAD, new PassiveActivator(new LightLevelCondition(15, ComparingCondition.ConditionComparisonType.EQUAL, true), 9), Collections.singletonList(new SelfTarget(null)), Arrays.asList(
                                new PotionGemEffect(SocketedSlotTypes.ALL, new MultiEffectActivator(null), Collections.singletonList(new SelfAOETarget(null, 32)), MobEffects.INVISIBILITY.getName(), 0, 10),
                                new PotionGemEffect(SocketedSlotTypes.ALL, new MultiEffectActivator(null), Collections.singletonList(new SelfAOETarget(null, 32)), MobEffects.GLOWING.getName(), 0, 10)
                        )),
                        new PotionGemEffect(SocketedSlotTypes.HEAD, new PassiveActivator(new LightLevelCondition(7, ComparingCondition.ConditionComparisonType.LESS_EQUAL, true), 9), ONLYSELF, MobEffects.INVISIBILITY.getName(), 0, 10)
                ),
                Collections.singletonList(new ItemFilter("minecraft:beacon", 0, false))));

        registerDefaultGemType("godapple", new GemType(
                "socketed.tooltip.default.godapple",
                3,
                TextFormatting.GOLD,
                Arrays.asList(
                        new PotionGemEffect(
                            SocketedSlotTypes.ALL,
                            new PassiveActivator(null, 400),
                            ONLYSELF,
                            MobEffects.ABSORPTION.getRegistryName().toString(),
                            1,
                            200),
                        new PotionGemEffect(
                            SocketedSlotTypes.ALL,
                            new PassiveActivator(new HealthPercentCondition(0.25F, ComparingCondition.ConditionComparisonType.LESS_EQUAL, true), 400),
                            ONLYSELF,
                            MobEffects.FIRE_RESISTANCE.getRegistryName().toString(),
                            0,
                            400)),
                Collections.singletonList(new ItemFilter("minecraft:golden_apple", 1, true))));

        registerDefaultGemType("netherstar", new GemType(
                "socketed.tooltip.default.netherstar",
                3,
                TextFormatting.WHITE,
                Arrays.asList(
                        new PotionGemEffect(
                                SocketedSlotTypes.BODY,
                                new AttackedActivator(null, false),
                                Arrays.asList(new OtherTarget(null), new OtherAOETarget(null, 3)),
                                MobEffects.WITHER.getRegistryName().toString(),
                                0,
                                200),
                        new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(5, false), 0)),
                Collections.singletonList(new ItemFilter("minecraft:nether_star", 0, false))));
    }
}