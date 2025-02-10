package socketed.common.jsondata.entry.effect;

import com.google.gson.*;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.entry.RandomValueRange;
import socketed.common.jsondata.entry.effect.activatable.activator.ActivatorDeserializer;
import socketed.common.jsondata.entry.effect.activatable.activator.GenericActivator;
import socketed.common.jsondata.entry.effect.slot.ISlotType;
import socketed.common.jsondata.entry.effect.slot.SlotTypeDeserializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EffectDeserializer implements JsonDeserializer<GenericGemEffect> {
    
    private final Gson gson;
    private final Map<String, Class<? extends GenericGemEffect>> typeReg;

    public EffectDeserializer() {
        ActivatorDeserializer activatorDeserializer = new ActivatorDeserializer();
        for(Map.Entry<String, Class<? extends GenericActivator>> entry : JsonConfig.activatorDeserializerMap.entrySet()) {
            activatorDeserializer.registerType(entry.getKey(), entry.getValue());
        }
        SlotTypeDeserializer slotTypeDeserializer = new SlotTypeDeserializer();
        for(Map.Entry<String, Class<? extends ISlotType>> entry : JsonConfig.slotTypeDeserializerMap.entrySet()) {
            slotTypeDeserializer.registerType(entry.getKey(), entry.getValue());
        }
        this.gson = new GsonBuilder()
                .registerTypeAdapter(GenericActivator.class, activatorDeserializer)
                .registerTypeAdapter(ISlotType.class, slotTypeDeserializer)
                .registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
                .create();
        this.typeReg = new HashMap<>();
    }

    public void registerType(String typeName, Class<? extends GenericGemEffect> typeClass) {
        typeReg.put(typeName, typeClass);
    }

    @Override
    public GenericGemEffect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Class<? extends GenericGemEffect> typeClass = typeReg.get(obj.get(GenericGemEffect.TYPE_FIELD).getAsString());
        return this.gson.fromJson(obj, typeClass);
    }
}