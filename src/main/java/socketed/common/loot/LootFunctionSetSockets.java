package socketed.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.capabilities.socketable.ICapabilitySocketable;
import socketed.common.socket.TieredSocket;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Random;

public class LootFunctionSetSockets extends LootFunction {

    private final ArrayList<Integer> socketTiers;

    public LootFunctionSetSockets(LootCondition[] conditions, ArrayList<Integer> socketTiers) {
        super(conditions);
        this.socketTiers = socketTiers;
    }

    @Override
    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        ICapabilitySocketable cap = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
        if (cap != null) {
            for (int tier : socketTiers) {
                cap.addSocket(new TieredSocket(tier));
            }
        }
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<LootFunctionSetSockets> {

        public Serializer() {
            super(new ResourceLocation("set_sockets"), LootFunctionSetSockets.class);
        }

        @Override
        public void serialize(JsonObject object, LootFunctionSetSockets functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.add("socketTiers", serializationContext.serialize(functionClazz.socketTiers));
        }

        @Override
        @Nonnull
        public LootFunctionSetSockets deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new LootFunctionSetSockets(conditionsIn, deserializationContext.deserialize(object, ArrayList.class));
        }
    }
}