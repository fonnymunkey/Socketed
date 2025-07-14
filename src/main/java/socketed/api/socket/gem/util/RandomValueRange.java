package socketed.api.socket.gem.util;

import com.google.gson.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
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

    public boolean getIsInteger() {
        return this.isInteger;
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

    @SideOnly(Side.CLIENT)
    public String getTooltip(int operation) {
        DecimalFormat format = this.getIsInteger() ? new DecimalFormat("#") : ItemStack.DECIMALFORMAT;
        if(this.getMax() == this.getMin()) {
            double amount = this.getMin() * (operation == 0 ? 1.0D : 100.0D);
            if(amount > 0.0D) return I18n.format("attribute.modifier.plus." + operation, format.format(amount), "");
            else if(amount < 0.0D) return I18n.format("attribute.modifier.take." + operation, format.format(-amount), "");
            else return "";
        } else {
            double min = this.getMin() * (operation == 0 ? 1.0D : 100.0D);
            double max = this.getMax() * (operation == 0 ? 1.0D : 100.0D);
            if(min >= 0.0D) return I18n.format("socketed.modifier.plus.plus." + operation, format.format(min), format.format(max), "");
            else if(min < 0.0D && max >=0.0D) return I18n.format("socketed.modifier.take.plus." + operation, format.format(-min), format.format(max), "");
            else if(min < 0.0D && max < 0.0D) return I18n.format("socketed.modifier.take.take." + operation, format.format(-min), format.format(-max), "");
            return "";
        }
    }
}