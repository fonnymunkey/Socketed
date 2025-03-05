package socketed.common.socket.gem.effect.slot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import socketed.api.socket.gem.effect.slot.ISlotType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SlotTypeDeserializer implements JsonDeserializer<ISlotType> {
	
	private final Map<String, Class<? extends ISlotType>> typeReg;
	
	public SlotTypeDeserializer() {
		this.typeReg = new HashMap<>();
	}
	
	public void registerType(String typeName, Class<? extends ISlotType> typeClass) {
		typeReg.put(typeName, typeClass);
	}
	
	@Override
	public ISlotType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		String name = json.getAsString();
		Class<? extends ISlotType> typeClass = typeReg.get(name);
		try {
			return (ISlotType)typeClass.getMethod("valueOf", String.class).invoke(null, name);
		}
		catch(Exception e) {
			throw new JsonParseException(e);
		}
	}
}