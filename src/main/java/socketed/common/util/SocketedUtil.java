package socketed.common.util;

import socketed.common.config.CustomConfig;
import socketed.common.data.entry.effect.GenericGemEffect;
import socketed.common.data.entry.effect.activatable.IActivationType;
import socketed.common.data.entry.filter.FilterEntry;

public abstract class SocketedUtil {

    public static void registerFilterDeserializer(String ENTRY_FILTER, Class<? extends FilterEntry> typeClass) {
        CustomConfig.filterDeserializerMap.put(ENTRY_FILTER, typeClass);
    }

    public static void registerEffectDeserializer(String ENTRY_FILTER, Class<? extends GenericGemEffect> typeClass) {
        CustomConfig.gemEffectDeserializerMap.put(ENTRY_FILTER, typeClass);
    }

    public static void registerActivationTypeDeserializer(String MODID, Class<? extends IActivationType> typeClass) {
        CustomConfig.activationDeserializerMap.put(MODID, typeClass);
    }
}