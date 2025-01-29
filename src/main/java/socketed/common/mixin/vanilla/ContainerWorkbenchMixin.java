package socketed.common.mixin.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import socketed.common.util.AddSocketsOnGeneration;

@Mixin(ContainerWorkbench.class)
public class ContainerWorkbenchMixin {
    @Inject(
            method = "transferStackInSlot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;onCreated(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)V")
    )
    void socketed_containerWorkbench_addSockets(EntityPlayer playerIn, int index, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 1) ItemStack stack){
        //This is only for quick move crafting (shift click) but has to work the same as normal crafting via ItemCraftedEvent
        AddSocketsOnGeneration.addSockets(stack, AddSocketsOnGeneration.EnumItemCreationContext.CRAFTING);
    }
}
