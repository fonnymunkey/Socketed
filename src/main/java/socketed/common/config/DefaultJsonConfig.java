package socketed.common.config;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.util.text.TextFormatting;
import socketed.common.attributes.SocketedAttributes;
import socketed.common.socket.gem.GemCombinationType;
import socketed.common.socket.gem.GemType;
import socketed.common.socket.gem.effect.activatable.KnockbackGemEffect;
import socketed.common.socket.gem.effect.activatable.activator.attack.AttackingActivator;
import socketed.common.socket.gem.effect.activatable.activator.passive.PassiveSelfAOEActivator;
import socketed.common.socket.gem.filter.ItemFilter;
import socketed.common.socket.gem.util.RandomValueRange;
import socketed.common.socket.gem.effect.AttributeGemEffect;
import socketed.common.socket.gem.effect.activatable.PotionGemEffect;
import socketed.common.socket.gem.effect.activatable.activator.passive.PassiveSelfActivator;
import socketed.common.socket.gem.filter.OreFilter;
import socketed.common.socket.gem.effect.slot.SocketedSlotTypes;

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
                                                             new AttributeGemEffect(SocketedSlotTypes.BODY, SocketedAttributes.DURABILITY.getName(), new RandomValueRange(0.02F, false), 1)),
                                               Collections.singletonList(new OreFilter("obsidian"))));
        registerDefaultGemType("stone_damage", new GemType("socketed.tooltip.default.stone_damage", 1, TextFormatting.GRAY,
                                            Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(1, false), 0)),
                                            Arrays.asList(new OreFilter("stone"),
                                                          new OreFilter("cobblestone"))));
        registerDefaultGemType("sticky", new GemType("socketed.tooltip.default.sticky", 0, TextFormatting.DARK_GREEN,
                                      Collections.singletonList(new PotionGemEffect(SocketedSlotTypes.BODY, new PassiveSelfActivator(9), MobEffects.SLOWNESS.getRegistryName().toString(), 0, 10)),
                                      Collections.singletonList(new OreFilter("slimeball"))));
        registerDefaultGemType("lucky", new GemType("socketed.tooltip.default.lucky", 2, TextFormatting.GREEN,
                                     Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.ALL, SharedMonsterAttributes.LUCK.getName(), new RandomValueRange(2, false), 0)),
                                     Collections.singletonList(new OreFilter("gemEmerald"))));
        registerDefaultGemType("streamlined", new GemType("socketed.tooltip.default.streamlined", 1, TextFormatting.AQUA,
                                           Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.BODY, EntityLivingBase.SWIM_SPEED.getName(), new RandomValueRange(0.1F, false), 1)),
                                           Collections.singletonList(new OreFilter("gemPrismarine"))));
        registerDefaultGemType("diamond", new GemType("socketed.tooltip.default.diamond", 2, TextFormatting.AQUA,
                                       Arrays.asList(
                                               new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(2, 3, true), 0),
                                               new AttributeGemEffect(SocketedSlotTypes.BODY, SharedMonsterAttributes.ARMOR.getName(), new RandomValueRange(2, false), 0)),
                                       Collections.singletonList(new OreFilter("gemDiamond"))));
        registerDefaultGemType("redstone", new GemType("socketed.tooltip.default.redstone", 2, TextFormatting.RED,
                                        Arrays.asList(
                                                new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_SPEED.getName(), new RandomValueRange(0.05F, false), 1),
                                                new AttributeGemEffect(SocketedSlotTypes.BODY, SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new RandomValueRange(0.05F, false), 1)),
                                        Collections.singletonList(new OreFilter("dustRedstone"))));
        registerDefaultGemType("glowstone", new GemType("socketed.tooltip.default.glowstone", 1, TextFormatting.YELLOW,
                                         Collections.singletonList(new PotionGemEffect(SocketedSlotTypes.HEAD, new PassiveSelfAOEActivator(40, 16, false), MobEffects.GLOWING.getRegistryName().toString(), 0, 41)),
                                         Collections.singletonList(new OreFilter("dustGlowstone"))));
        registerDefaultGemType("piston", new GemType("socketed.tooltip.default.piston", 1, TextFormatting.AQUA,
                                                        Collections.singletonList(new KnockbackGemEffect(SocketedSlotTypes.HAND, new AttackingActivator(false, true, true, false, true), 2.0F, false)),
                                                        Collections.singletonList(new ItemFilter("minecraft:piston", 0, false))));
        registerDefaultGemType("sticky_piston", new GemType("socketed.tooltip.default.sticky_piston", 1, TextFormatting.GREEN,
                                                     Collections.singletonList(new KnockbackGemEffect(SocketedSlotTypes.HAND, new AttackingActivator(false, true, true, false, true), 2.0F, true)),
                                                     Collections.singletonList(new ItemFilter("minecraft:sticky_piston", 0, false))));
        
        //Gem Combination Types
        registerDefaultGemCombinationType("three_diamonds", new GemCombinationType(
                "socketed.tooltip.default.three_diamonds", TextFormatting.DARK_BLUE, false,false, true, true,
                Arrays.asList("diamond", "diamond", "diamond"),
                Collections.singletonList(new AttributeGemEffect(SocketedSlotTypes.HAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new RandomValueRange(9, false), 0))));
        registerDefaultGemCombinationType("rgb", new GemCombinationType(
                "socketed.tooltip.default.rgb", TextFormatting.GOLD, true,false, true, false,
                Arrays.asList("redstone", "lucky", "diamond"),
                Arrays.asList(new PotionGemEffect(SocketedSlotTypes.ALL, new PassiveSelfActivator(9), MobEffects.GLOWING.getRegistryName().toString(), 0, 10),
                              new PotionGemEffect(SocketedSlotTypes.ALL, new PassiveSelfAOEActivator(40, 8, false), MobEffects.GLOWING.getRegistryName().toString(), 0, 41))));
    }
}