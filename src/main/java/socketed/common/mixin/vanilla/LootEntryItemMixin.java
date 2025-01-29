package socketed.common.mixin.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntryItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import socketed.common.util.AddSocketsOnGeneration;

import java.util.Collection;
import java.util.Random;

@Mixin(LootEntryItem.class)
public class LootEntryItemMixin {
    @Inject(
            method = "addLoot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z")
    )
    void socketed_lootEntryItem_addSockets(Collection<ItemStack> stacks, Random rand, LootContext context, CallbackInfo ci, @Local ItemStack stack){
        //Additionally to the LootFunction to also be able to set some default sockets
        //This is not only for chest loot but also mob drops if they have socketable items in their loot table (none in vanilla, as the local difficulty gear is set outside of loot tables)
        AddSocketsOnGeneration.addSockets(stack, AddSocketsOnGeneration.EnumItemCreationContext.LOOT);
    }
}
