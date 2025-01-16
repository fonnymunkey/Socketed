package socketed.common.socket;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import socketed.common.capabilities.CapabilityHasSockets;

public class SocketLootFunction extends LootFunction
{
    private final RandomValueRange count;

    public SocketLootFunction(LootCondition[] conditions, RandomValueRange countIn)
    {
        super(conditions);
        this.count = countIn;
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context)
    {
        if(stack.hasCapability(CapabilityHasSockets.HAS_SOCKETS,null))
            stack.getCapability(CapabilityHasSockets.HAS_SOCKETS,null).setSocketCount(count.generateInt(rand));
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SocketLootFunction>
    {
        public Serializer()
        {
            super(new ResourceLocation("set_sockets"), SocketLootFunction.class);
        }

        public void serialize(JsonObject object, SocketLootFunction functionClazz, JsonSerializationContext serializationContext)
        {
            object.add("count", serializationContext.serialize(functionClazz.count));
        }

        public SocketLootFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn)
        {
            return new SocketLootFunction(conditionsIn, JsonUtils.deserializeClass(object, "count", deserializationContext, RandomValueRange.class));
        }
    }
}
