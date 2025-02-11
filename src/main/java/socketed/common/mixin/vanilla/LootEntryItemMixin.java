package socketed.common.mixin.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntryItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import socketed.common.util.AddSocketsHelper;

import java.util.Collection;
import java.util.Random;

@Mixin(LootEntryItem.class)
public abstract class LootEntryItemMixin {
    
    @Inject(
            method = "addLoot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z")
    )
    private void socketed_vanillaLootEntryItem_addLoot(Collection<ItemStack> stacks, Random rand, LootContext context, CallbackInfo ci, @Local ItemStack stack){
        //Attempt to add sockets to loot by default without specifying loot functions
        if(context.getLootedEntity() != null && context.getLootedEntity() instanceof EntityLivingBase) {
            //Mob specific loot generation
            AddSocketsHelper.addSockets(stack, AddSocketsHelper.EnumItemCreationContext.MOB_DROP);
        }
        //General loot generation such as chests
        else AddSocketsHelper.addSockets(stack, AddSocketsHelper.EnumItemCreationContext.LOOT);
    }
}