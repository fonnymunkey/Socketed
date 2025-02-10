package socketed.common.jsondata.entry.effect.activatable.activator;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ActivatorDeserializer implements JsonDeserializer<GenericActivator> {
    
    private final Gson gson;
    private final Map<String, Class<? extends GenericActivator>> typeReg;

    public ActivatorDeserializer() {
        this.gson = new GsonBuilder().create();
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