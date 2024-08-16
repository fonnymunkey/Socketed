package socketed.common.data.entry.effect;

import com.google.gson.*;
import socketed.common.config.CustomConfig;
import socketed.common.data.entry.effect.activatable.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EffectDeserializer implements JsonDeserializer<EffectEntry> {
    private final Gson gson;
    private final Map<String, Class<? extends EffectEntry>> typeReg;

    public EffectDeserializer() {
        ActivationTypeDeserializer activationDeserializer = new ActivationTypeDeserializer();
        for(Map.Entry<String, Class<? extends IActivationType>> entry : CustomConfig.activationDeserializerMap.entrySet()) {
            activationDeserializer.registerType(entry.getKey(), entry.getValue());
        }
        this.gson = new GsonBuilder()
                .registerTypeAdapter(IActivationType.class, activationDeserializer)
                .create();
        this.typeReg = new HashMap<>();
    }

    public void registerType(String typeName, Class<? extends EffectEntry> typeClass) {
        typeReg.put(typeName, typeClass);
    }

    @Override
    public EffectEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Class<? extends EffectEntry> typeClass = typeReg.get(obj.get(EffectEntry.FILTER_NAME).getAsString());
        return this.gson.fromJson(obj, typeClass);
    }
}