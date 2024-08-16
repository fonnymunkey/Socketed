package socketed.common.data.entry.effect.activatable;

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
        String[] elem = json.getAsString().split(":");
        Class<? extends IActivationType> typeClass = typeReg.get(elem[0]);
        //Stinky
        try { return (IActivationType)typeClass.getMethod("fromValue", String.class).invoke(typeClass, elem[1]); }
        catch(Exception e) { throw new RuntimeException(e); }
    }
}