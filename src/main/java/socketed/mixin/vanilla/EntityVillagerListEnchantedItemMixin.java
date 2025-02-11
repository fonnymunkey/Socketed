package socketed.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import socketed.common.loot.DefaultSocketsGenerator;
import socketed.common.util.SocketedUtil;

@Mixin(EntityVillager.ListEnchantedItemForEmeralds.class)
public abstract class EntityVillagerListEnchantedItemMixin {
    
    //TODO: Ensure results do not reroll by closing/opening GUI or reloading world
    @ModifyExpressionValue(
            method = "addMerchantRecipe",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;addRandomEnchantment(Ljava/util/Random;Lnet/minecraft/item/ItemStack;IZ)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack socketed_vanillaEntityVillagerListEnchantedItemForEmeralds_addMerchantRecipe(ItemStack original) {
        SocketedUtil.addSocketsToStack(original, DefaultSocketsGenerator.SocketedItemCreationContext.MERCHANT);
        return original;
    }
}