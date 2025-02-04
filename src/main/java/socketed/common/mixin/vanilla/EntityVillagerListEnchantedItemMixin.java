package socketed.common.mixin.vanilla;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import socketed.common.util.AddSocketsHelper;

import java.util.Random;

@Mixin(EntityVillager.ListEnchantedItemForEmeralds.class)
public class EntityVillagerListEnchantedItemMixin {
    @Redirect(
            method = "addMerchantRecipe",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;addRandomEnchantment(Ljava/util/Random;Lnet/minecraft/item/ItemStack;IZ)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack socketed_entityVillagerListEnchantedItem_addSockets(Random random, ItemStack stack, int level, boolean allowTreasure){
        AddSocketsHelper.addSockets(stack, AddSocketsHelper.EnumItemCreationContext.MERCHANT);
        return EnchantmentHelper.addRandomEnchantment(random, stack, level, allowTreasure);
    }
}
