package socketed.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.capabilities.socketable.ICapabilitySocketable;

import javax.annotation.Nonnull;
import java.util.Random;

public class LootFunctionAddSocketsRandomly extends LootFunction {

    private final int maxSockets;
    private final int rollCount;
    private final float rollChance;

    public LootFunctionAddSocketsRandomly(LootCondition[] conditions, int maxSockets, int rollCount, float rollChance) {
        super(conditions);
        this.maxSockets = maxSockets;
        this.rollCount = rollCount;
        this.rollChance = rollChance;
    }

    @Override
    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        ICapabilitySocketable cap = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if(cap != null)
            DefaultSocketsGenerator.addSocketsRandomly(stack, maxSockets, rollCount, rollChance);
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<LootFunctionAddSocketsRandomly> {
        
        public Serializer() {
            super(new ResourceLocation("socket_randomly"), LootFunctionAddSocketsRandomly.class);
        }

        @Override
        public void serialize(JsonObject object, LootFunctionAddSocketsRandomly functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("maxSockets", functionClazz.maxSockets);
            object.addProperty("rollCount", functionClazz.rollCount);
            object.addProperty("rollChance", functionClazz.rollChance);
        }

        @Override
        @Nonnull
        public LootFunctionAddSocketsRandomly deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            int maxSockets = JsonUtils.getInt(object, "maxSockets", 0);
            int rollCount = JsonUtils.getInt(object, "rollCount", 0);
            float rollChance = JsonUtils.getFloat(object, "rollChance", 0F);
            return new LootFunctionAddSocketsRandomly(conditionsIn, maxSockets, rollCount, rollChance);
        }
    }
}