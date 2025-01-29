package socketed.common.jsondata.entry.filter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FilterDeserializer implements JsonDeserializer<FilterEntry> {
    
    private final Gson gson;
    private final Map<String, Class<? extends FilterEntry>> typeReg;

    public FilterDeserializer() {
        this.gson = new Gson();
        this.typeReg = new HashMap<>();
    }

    public void registerType(String typeName, Class<? extends FilterEntry> typeClass) {
        typeReg.put(typeName, typeClass);
    }

    @Override
    public FilterEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Class<? extends FilterEntry> typeClass = typeReg.get(obj.get(FilterEntry.TYPE_FIELD).getAsString());
        return this.gson.fromJson(obj, typeClass);
    }
}