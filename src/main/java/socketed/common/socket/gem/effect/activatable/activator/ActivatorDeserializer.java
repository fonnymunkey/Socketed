package socketed.common.socket.gem.effect.activatable.activator;

import com.google.gson.*;
import socketed.common.config.JsonConfig;
import socketed.common.socket.gem.effect.activatable.condition.ConditionDeserializer;
import socketed.common.socket.gem.effect.activatable.condition.GenericCondition;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ActivatorDeserializer implements JsonDeserializer<GenericActivator> {
    
    private final Gson gson;
    private final Map<String, Class<? extends GenericActivator>> typeReg;

    public ActivatorDeserializer() {
        ConditionDeserializer conditionDeserializer = new ConditionDeserializer();
        for(Map.Entry<String, Class<? extends GenericCondition>> entry : JsonConfig.conditionDeserializerMap.entrySet()) {
            conditionDeserializer.registerType(entry.getKey(), entry.getValue());
        }
        
        this.gson = new GsonBuilder()
                .registerTypeAdapter(GenericCondition.class, conditionDeserializer)
                .create();
        this.typeReg = new HashMap<>();
    }

    public void registerType(String typeName, Class<? extends GenericActivator> typeClass) {
        typeReg.put(typeName, typeClass);
    }

    @Override
    public GenericActivator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Class<? extends GenericActivator> typeClass = typeReg.get(obj.get(GenericActivator.TYPE_FIELD).getAsString());
        return this.gson.fromJson(obj, typeClass);
    }
}