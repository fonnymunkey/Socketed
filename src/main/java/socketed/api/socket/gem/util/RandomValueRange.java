package socketed.api.socket.gem.util;

import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Type;
import java.util.Random;

public class RandomValueRange {
    
    private static final Random RAND = new Random();
    
    private final float min;
    private final float max;
    private final boolean isInteger;

    public RandomValueRange(float minIn, float maxIn, boolean isInteger) {
        //Eagle safe
        this.min = Math.min(minIn,maxIn);
        this.max = Math.max(minIn,maxIn);
        this.isInteger = isInteger;
    }

    public RandomValueRange(float value, boolean isInteger) {
        this.min = value;
        this.max = value;
        this.isInteger = isInteger;
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }
    
    public float generateValue() {
        return this.generateValue(RAND);
    }

    public float generateValue(Random rand) {
        if(this.isInteger) return MathHelper.getInt(rand, MathHelper.floor(this.min), MathHelper.floor(this.max));
        else return MathHelper.nextFloat(rand, this.min, this.max);
    }

    public static class Serializer implements JsonDeserializer<RandomValueRange>, JsonSerializer<RandomValueRange> {
        public RandomValueRange deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (JsonUtils.isNumber(jsonElement))
                return new RandomValueRange(JsonUtils.getFloat(jsonElement, "value"), false);
            else {
                String range = JsonUtils.getString(jsonElement, "value");
                String[] split = range.split("]");
                boolean isInteger = split.length > 1 && split[1].trim().equals("int");
                split = split[0].trim().substring(1).split(":");
                float min = Float.parseFloat(split[0].trim());
                float max = Float.parseFloat(split[1].trim());
                return new RandomValueRange(min, max, isInteger);
            }
        }

        public JsonElement serialize(RandomValueRange randomValueRange, Type type, JsonSerializationContext jsonSerializationContext) {
            if (randomValueRange.getMin() == randomValueRange.getMax())
                return new JsonPrimitive(randomValueRange.getMin());
            else
                return new JsonPrimitive("["+randomValueRange.getMin()+" : "+randomValueRange.getMax()+"] "+ (randomValueRange.isInteger?"int":""));
        }
    }
}