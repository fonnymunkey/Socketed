package socketed.common.socket.gem.effect.activatable.condition;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConditionDeserializer implements JsonDeserializer<GenericCondition> {
	
	private final Gson gson;
	private final Map<String, Class<? extends GenericCondition>> typeReg;
	
	public ConditionDeserializer() {
		this.gson = new GsonBuilder()
				.registerTypeAdapter(GenericCondition.class, this)
				.create();
		this.typeReg = new HashMap<>();
	}
	
	public void registerType(String typeName, Class<? extends GenericCondition> typeClass) {
		typeReg.put(typeName, typeClass);
	}
	
	@Override
	public GenericCondition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		Class<? extends GenericCondition> typeClass = typeReg.get(obj.get(GenericCondition.TYPE_FIELD).getAsString());
		return this.gson.fromJson(obj, typeClass);
	}
}