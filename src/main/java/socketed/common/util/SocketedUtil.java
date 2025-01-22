package socketed.common.util;

import socketed.common.config.JsonConfig;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.effect.activatable.IActivationType;
import socketed.common.jsondata.entry.filter.FilterEntry;

public abstract class SocketedUtil {

    public static void registerFilterDeserializer(String ENTRY_FILTER, Class<? extends FilterEntry> typeClass) {
        JsonConfig.filterDeserializerMap.put(ENTRY_FILTER, typeClass);
    }

    public static void registerEffectDeserializer(String ENTRY_FILTER, Class<? extends GenericGemEffect> typeClass) {
        JsonConfig.gemEffectDeserializerMap.put(ENTRY_FILTER, typeClass);
    }

    public static void registerActivationTypeDeserializer(String MODID, Class<? extends IActivationType> typeClass) {
        JsonConfig.activationDeserializerMap.put(MODID, typeClass);
    }
}