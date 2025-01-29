package socketed.common.util;

import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.effect.activatable.IActivationType;
import socketed.common.jsondata.entry.filter.FilterEntry;
import socketed.common.jsondata.entry.effect.slot.ISlotType;

public abstract class SocketedUtil {

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
            JsonConfig.slotTypeDeserializerMap.put(type.name(), typeClass);
        }
    }
}