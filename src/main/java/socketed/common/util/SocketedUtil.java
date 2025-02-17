package socketed.common.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.config.*;
import socketed.common.loot.DefaultSocketsGenerator;
import socketed.common.loot.IItemCreationContext;
import socketed.common.socket.gem.GemCombinationType;
import socketed.common.socket.gem.GemType;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.effect.activatable.target.GenericTarget;
import socketed.common.socket.gem.effect.activatable.condition.GenericCondition;
import socketed.common.socket.gem.filter.GenericFilter;
import socketed.common.socket.gem.effect.slot.ISlotType;
import socketed.common.socket.GenericSocket;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class SocketedUtil {
    
    private static final List<ISlotType> registeredSlots = new ArrayList<>();
    
    //
    // Registrations
    //
    
    public static void registerFilterType(String typeName, Class<? extends GenericFilter> typeClass, String modid) {
        Socketed.LOGGER.log(Level.INFO, "Registering Filter " + typeName + " from " + modid);
        JsonConfig.filterDeserializerMap.put(typeName, typeClass);
    }

    public static void registerEffectType(String typeName, Class<? extends GenericGemEffect> typeClass, String modid) {
        Socketed.LOGGER.log(Level.INFO, "Registering Effect " + typeName + " from " + modid);
        JsonConfig.gemEffectDeserializerMap.put(typeName, typeClass);
    }
    
    public static void registerActivator(String typeName, Class<? extends GenericActivator> typeClass, String modid) {
        Socketed.LOGGER.log(Level.INFO, "Registering Activator " + typeName + " from " + modid);
        JsonConfig.activatorDeserializerMap.put(typeName, typeClass);
    }
    
    public static void registerTarget(String typeName, Class<? extends GenericTarget> typeClass, String modid) {
        Socketed.LOGGER.log(Level.INFO, "Registering Target " + typeName + " from " + modid);
        JsonConfig.targetDeserializerMap.put(typeName, typeClass);
    }
    
    public static void registerCondition(String typeName, Class<? extends GenericCondition> typeClass, String modid) {
        Socketed.LOGGER.log(Level.INFO, "Registering Condition " + typeName + " from " + modid);
        JsonConfig.conditionDeserializerMap.put(typeName, typeClass);
    }

    public static <T extends Enum<T> & ISlotType> void registerSlotTypes(Class<T> typeClass, String modid) {
        for(T type : typeClass.getEnumConstants()) {
            Socketed.LOGGER.log(Level.INFO, "Registering Slot Type " + type.name() + " from " + modid);
            registeredSlots.add(type);
            JsonConfig.slotTypeDeserializerMap.put(type.name(), typeClass);
        }
    }
    
    public static void registerSocket(String typeName, Function<NBTTagCompound, ? extends GenericSocket> fromNBTFunction, String modid) {
        Socketed.LOGGER.log(Level.INFO, "Registering Socket " + typeName + " from " + modid);
        JsonConfig.socketNBTDeserializerMap.put(typeName, fromNBTFunction);
    }
    
    /**
     * Used to allow addons to define their own item type definitions and rolls for socketing and loot
     */
    public static void registerForcedItemType(String name, Predicate<Item> canSocket, int rolls) {
        Socketed.LOGGER.log(Level.INFO, "Registering Forced Socketable Item Type: " + name);
        SocketableConfig.forcedItemTypes.put(name, new SocketableConfig.SocketableType(canSocket));
        AddSocketsConfig.forcedItemTypeRolls.put(name, rolls);
        ForgeConfig.reset();
    }
    
    /**
     * Used to allow addons to define their own item type definitions and rolls for socketing and loot
     */
    public static void registerForcedItemType(String name, String regex, int rolls) {
        Socketed.LOGGER.log(Level.INFO, "Registering Forced Socketable Item Type: " + name);
        SocketableConfig.forcedItemTypes.put(name, new SocketableConfig.SocketableType(regex));
        AddSocketsConfig.forcedItemTypeRolls.put(name, rolls);
        ForgeConfig.reset();
    }
    
    public static void registerDefaultGemType(String name, GemType gemType) {
        DefaultJsonConfig.registerDefaultGemType(name, gemType);
    }
    
    public static void registerDefaultGemCombinationType(String name, GemCombinationType gemCombinationType) {
        DefaultJsonConfig.registerDefaultGemCombinationType(name, gemCombinationType);
    }
    
    //
    // Utility
    //
    
    /**
     * @return if the given ItemStack would be a valid gem
     */
    public static boolean stackIsGem(ItemStack stack) {
        if(stack.isEmpty()) return false;
        if(!SocketedUtil.hasCompletedLoading()) return false;
        return SocketedUtil.getGemTypeFromItemStack(stack) != null;
    }
    
    @Nullable
    public static GemType getGemTypeFromItemStack(ItemStack itemStack) {
        if(itemStack.isEmpty()) return null;
        if(!SocketedUtil.hasCompletedLoading()) return null;
        for(GemType gemType : JsonConfig.getSortedGemDataList()) {
            if(gemType.matches(itemStack)) return gemType;
        }
        return null;
    }
    
    @Nullable
    public static GemType getGemTypeFromName(String gemTypeName) {
        if(gemTypeName == null || gemTypeName.isEmpty()) return null;
        if(!SocketedUtil.hasCompletedLoading()) return null;
        return JsonConfig.getGemData().get(gemTypeName);
    }
    
    @Nullable
    public static GemCombinationType getGemCombinationTypeFromName(String gemCombinationTypeName) {
        if(gemCombinationTypeName == null || gemCombinationTypeName.isEmpty()) return null;
        if(!SocketedUtil.hasCompletedLoading()) return null;
        return JsonConfig.getGemCombinationData().get(gemCombinationTypeName);
    }
    
    /**
     * Note: Use this if possible over ISlotType::isStackValid to allow for config override compatibility
     * @return true if the given ItemStack is valid for the given ISlotType
     */
    //TODO: allow for config override?
    public static boolean isStackValidForSlot(ItemStack stack, ISlotType slotType) {
        if(stack.isEmpty()) return false;
        return slotType.isStackValid(stack);
    }
    
    public static List<ISlotType> getAllSlotTypes() {
        return registeredSlots;
    }
    
    /**
     * @return true if the provided ISlotTypes match each other (Equals or inherits)
     */
    public static boolean doSlotsMatch(ISlotType slotType1, ISlotType slotType2) {
        return slotType1.isSlotValid(slotType2) && slotType2.isSlotValid(slotType1);
    }
    
    /**
     * @return the translated tooltip for the given ISlotType
     */
    @SideOnly(Side.CLIENT)
    public static String getSlotTooltip(ISlotType slotType) {
        return I18n.format("socketed.tooltip.slottype." + slotType.getTooltipKey());
    }
    
    public static void addSocketsToStack(ItemStack stack, IItemCreationContext context) {
        if(SocketedUtil.hasCompletedLoading()) {
            DefaultSocketsGenerator.addSockets(stack, context);
        }
    }
    
    public static void addSocketsToStackRandomly(ItemStack stack, int maxSockets, int rollAmount, float rollChance) {
        if(SocketedUtil.hasCompletedLoading()) {
            DefaultSocketsGenerator.addSocketsRandomly(stack, maxSockets, rollAmount, rollChance);
        }
    }
    
    public static boolean canStackHaveSockets(ItemStack stack) {
        if(SocketedUtil.hasCompletedLoading()) {
            return ForgeConfig.SOCKETABLES.canSocket(stack);
        }
        return false;
    }
    
    //Some ItemStacks/tooltips are created during loading causing data to be read before loading is finished
    //These ItemStacks would not involve sockets anyways so ignore them if accessed during loading
    public static boolean hasCompletedLoading() {
        return JsonConfig.completedLoading;
    }
}