package socketed.common.socket.gem.effect;

import com.google.gson.*;
import socketed.common.config.JsonConfig;
import socketed.common.socket.gem.effect.activatable.target.GenericTarget;
import socketed.common.socket.gem.effect.activatable.target.TargetDeserializer;
import socketed.common.socket.gem.util.RandomValueRange;
import socketed.common.socket.gem.effect.activatable.activator.ActivatorDeserializer;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;
import socketed.common.socket.gem.effect.slot.ISlotType;
import socketed.common.socket.gem.effect.slot.SlotTypeDeserializer;

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
        TargetDeserializer targetDeserializer = new TargetDeserializer();
        for(Map.Entry<String, Class<? extends GenericTarget>> entry : JsonConfig.targetDeserializerMap.entrySet()) {
            targetDeserializer.registerType(entry.getKey(), entry.getValue());
        }
        SlotTypeDeserializer slotTypeDeserializer = new SlotTypeDeserializer();
        for(Map.Entry<String, Class<? extends ISlotType>> entry : JsonConfig.slotTypeDeserializerMap.entrySet()) {
            slotTypeDeserializer.registerType(entry.getKey(), entry.getValue());
        }
        
        this.gson = new GsonBuilder()
                .registerTypeAdapter(GenericActivator.class, activatorDeserializer)
                .registerTypeAdapter(GenericTarget.class, targetDeserializer)
                .registerTypeAdapter(ISlotType.class, slotTypeDeserializer)
                .registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
                .registerTypeAdapter(GenericGemEffect.class, this)
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