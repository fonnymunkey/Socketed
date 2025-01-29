package socketed.common.jsondata.entry.effect.activatable;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ActivationTypeDeserializer implements JsonDeserializer<IActivationType> {
    
    private final Map<String, Class<? extends IActivationType>> typeReg;

    public ActivationTypeDeserializer() {
        this.typeReg = new HashMap<>();
    }

    public void registerType(String typeName, Class<? extends IActivationType> typeClass) {
        typeReg.put(typeName, typeClass);
    }

    @Override
    public IActivationType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String name = json.getAsString();
        Class<? extends IActivationType> typeClass = typeReg.get(name);
        try {
            return (IActivationType)typeClass.getMethod("valueOf", String.class).invoke(null, name);
        }
        catch(Exception e) {
            throw new JsonParseException(e);
        }
    }
}