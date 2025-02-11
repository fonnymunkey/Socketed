package socketed.common.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.config.AddSocketsConfig;
import socketed.common.config.ForgeConfig;
import socketed.common.config.JsonConfig;
import socketed.common.config.SocketableConfig;
import socketed.common.socket.gem.GemType;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.filter.GenericFilter;
import socketed.common.socket.gem.effect.slot.ISlotType;
import socketed.common.socket.GenericSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class SocketedUtil {

    public static final List<ISlotType> registeredSlots = new ArrayList<>();
    
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
    public static void addForcedItemType(String name, Predicate<Item> canSocket, int rolls) {
        Socketed.LOGGER.log(Level.INFO, "Registering Forced Socketable Item Type: " + name);
        SocketableConfig.forcedItemTypes.put(name, new SocketableConfig.SocketableType(canSocket));
        AddSocketsConfig.forcedItemTypeRolls.put(name, rolls);
        ForgeConfig.reset();
    }
    
    /**
     * Used to allow addons to define their own item type definitions and rolls for socketing and loot
     */
    public static void addForcedItemType(String name, String regex, int rolls) {
        Socketed.LOGGER.log(Level.INFO, "Registering Forced Socketable Item Type: " + name);
        SocketableConfig.forcedItemTypes.put(name, new SocketableConfig.SocketableType(regex));
        AddSocketsConfig.forcedItemTypeRolls.put(name, rolls);
        ForgeConfig.reset();
    }
    
    /**
     * @return if the given ItemStack would be a valid gem
     */
    public static boolean stackIsGem(ItemStack stack) {
        if(stack.isEmpty()) return false;
        return GemType.getGemTypeFromItemStack(stack) != null;
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
}