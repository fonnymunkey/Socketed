package socketed.common.jsondata.entry.effect;

import com.google.gson.*;
import socketed.common.config.JsonConfig;
import socketed.common.jsondata.entry.effect.activatable.ActivationTypeDeserializer;
import socketed.common.jsondata.entry.effect.activatable.IActivationType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EffectDeserializer implements JsonDeserializer<GenericGemEffect> {
    private final Gson gson;
    private final Map<String, Class<? extends GenericGemEffect>> typeReg;

    public EffectDeserializer() {
        ActivationTypeDeserializer activationDeserializer = new ActivationTypeDeserializer();
        for(Map.Entry<String, Class<? extends IActivationType>> entry : JsonConfig.activationDeserializerMap.entrySet()) {
            activationDeserializer.registerType(entry.getKey(), entry.getValue());
        }
        this.gson = new GsonBuilder()
                .registerTypeAdapter(IActivationType.class, activationDeserializer)
                .create();
        this.typeReg = new HashMap<>();
    }

    public void registerType(String typeName, Class<? extends GenericGemEffect> typeClass) {
        typeReg.put(typeName, typeClass);
    }

    @Override
    public GenericGemEffect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Class<? extends GenericGemEffect> typeClass = typeReg.get(obj.get(GenericGemEffect.FILTER_NAME).getAsString());
        return this.gson.fromJson(obj, typeClass);
    }
}