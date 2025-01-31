package socketed.common.config;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.util.text.TextFormatting;
import socketed.common.attributes.Attributes;
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
        //Gem Types
        registerDefaultGemType("obsidian_damage", new GemType("socketed.tooltip.default.obsidian_damage", 3, TextFormatting.DARK_PURPLE,
                                               Arrays.asList(new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(3, false), 0),
                                                             new AttributeGemEffect(SocketedSlotTypes.BODY, Attributes.DURABILITY.getName(), new RandomValueRange(0.02F, false), 1)),
                                               Collections.singletonList(new OreEntry("obsidian"))));
        registerDefaultGemType("stone_damage", new GemType("socketed.tooltip.default.stone_damage", 1, TextFormatting.GRAY,
                                            Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(1, false), 0)),
                                            Arrays.asList(new OreEntry("stone"),
                                                          new OreEntry("cobblestone"))));
        registerDefaultGemType("sticky", new GemType("socketed.tooltip.default.sticky", 0, TextFormatting.DARK_GREEN,
                                      Collections.singletonList(new PotionGemEffect(SocketedSlotTypes.BODY, SocketedActivationTypes.PASSIVE_SELF, MobEffects.SLOWNESS.getRegistryName().toString(), 0, 60)),
                                      Collections.singletonList(new OreEntry("slimeball"))));
        registerDefaultGemType("lucky", new GemType("socketed.tooltip.default.lucky", 2, TextFormatting.GREEN,
                                     Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.ALL, SharedMonsterAttributes.LUCK.getName(), new RandomValueRange(2, false), 0)),
                                     Collections.singletonList(new OreEntry("gemEmerald"))));
        registerDefaultGemType("streamlined", new GemType("socketed.tooltip.default.streamlined", 1, TextFormatting.AQUA,
                                           Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.BODY, EntityLivingBase.SWIM_SPEED.getName(), new RandomValueRange(0.1F, false), 1)),
                                           Collections.singletonList(new OreEntry("gemPrismarine"))));
        registerDefaultGemType("diamond", new GemType("socketed.tooltip.default.diamond", 2, TextFormatting.AQUA,
                                       Arrays.asList(
                                               new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(2, 3, true), 0),
                                               new AttributeGemEffect(SocketedSlotTypes.BODY, SharedMonsterAttributes.ARMOR.getName(), new RandomValueRange(2, false), 0)),
                                       Collections.singletonList(new OreEntry("gemDiamond"))));
        registerDefaultGemType("redstone", new GemType("socketed.tooltip.default.redstone", 2, TextFormatting.RED,
                                        Arrays.asList(
                                                new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_SPEED.getName(), new RandomValueRange(0.05F, false), 1),
                                                new AttributeGemEffect(SocketedSlotTypes.BODY, SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new RandomValueRange(0.05F, false), 1)),
                                        Collections.singletonList(new OreEntry("dustRedstone"))));
        //TODO: Add enchanting attribute/add attribute to SME
        registerDefaultGemType("lapis", new GemType("socketed.tooltip.default.lapis", 2, TextFormatting.BLUE,
                                     Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.ALL, "socketed.attributes.enchantingpower", new RandomValueRange(2, false), 0)),
                                     Collections.singletonList(new OreEntry("gemLapis", true))));
        registerDefaultGemType("glowstone", new GemType("socketed.tooltip.default.glowstone", 1, TextFormatting.YELLOW,
                                         Collections.singletonList(new PotionGemEffect(SocketedSlotTypes.HEAD, SocketedActivationTypes.PASSIVE_FAR, MobEffects.GLOWING.getRegistryName().toString(), 0, 60)),
                                         Collections.singletonList(new OreEntry("dustGlowstone"))));
        
        //Gem Combination Types
        registerDefaultGemCombinationType("three_diamonds", new GemCombinationType(
                "socketed.tooltip.default.three_diamonds", TextFormatting.DARK_BLUE, false,false, true,
                Arrays.asList("diamond", "diamond", "diamond"),
                Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(9, false), 0))));
        registerDefaultGemCombinationType("rgb", new GemCombinationType(
                "socketed.tooltip.default.rgb", TextFormatting.GOLD, true,false, false,
                Arrays.asList("redstone", "lucky", "lapis"),
                Arrays.asList(new PotionGemEffect(SocketedSlotTypes.ALL, SocketedActivationTypes.PASSIVE_SELF, MobEffects.GLOWING.getRegistryName().toString(), 0, 60),
                              new PotionGemEffect(SocketedSlotTypes.ALL, SocketedActivationTypes.PASSIVE_NEARBY, MobEffects.GLOWING.getRegistryName().toString(), 0, 60))));
    }
}