package socketed.common.jsondata.entry.effect.activatable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

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
        //default mapping is EnumActivationType for key "socketed"
        Class<? extends IActivationType> typeClass = typeReg.get(elem[0]);
        try {
            //Invoke static method (Mod)EnumActivationType.valueOf(elem[1]), return the enum that is mapped to elem[1]
            return (IActivationType) typeClass.getMethod("valueOf", String.class).invoke(null, elem[1]);
        }
        catch(Exception e) { throw new RuntimeException(e); }
    }
}