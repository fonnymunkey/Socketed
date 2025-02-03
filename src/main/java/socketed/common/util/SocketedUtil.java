package socketed.common.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.GemType;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.effect.activatable.IActivationType;
import socketed.common.jsondata.entry.filter.FilterEntry;
import socketed.common.jsondata.entry.effect.slot.ISlotType;

import java.util.ArrayList;
import java.util.List;

public abstract class SocketedUtil {

    public static final List<ISlotType> registeredSlots = new ArrayList<>();
    
    public static void registerFilterType(String typeName, Class<? extends FilterEntry> typeClass) {
        Socketed.LOGGER.log(Level.INFO, "Registering Filter Type " + typeName + " from " + typeClass.getSimpleName());
        JsonConfig.filterDeserializerMap.put(typeName, typeClass);
    }

    public static void registerEffectType(String typeName, Class<? extends GenericGemEffect> typeClass) {
        Socketed.LOGGER.log(Level.INFO, "Registering Effect Type " + typeName + " from " + typeClass.getSimpleName());
        JsonConfig.gemEffectDeserializerMap.put(typeName, typeClass);
    }

    public static <T extends Enum<T> & IActivationType> void registerActivationTypes(Class<T> typeClass) {
        for(T type : typeClass.getEnumConstants()) {
            Socketed.LOGGER.log(Level.INFO, "Registering Activation Type " + type.name() + " from " + typeClass.getSimpleName());
            JsonConfig.activationTypeDeserializerMap.put(type.name(), typeClass);
        }
    }
    
    public static <T extends Enum<T> & ISlotType> void registerSlotTypes(Class<T> typeClass) {
        for(T type : typeClass.getEnumConstants()) {
            Socketed.LOGGER.log(Level.INFO, "Registering Slot Type " + type.name() + " from " + typeClass.getSimpleName());
            registeredSlots.add(type);
            JsonConfig.slotTypeDeserializerMap.put(type.name(), typeClass);
        }
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