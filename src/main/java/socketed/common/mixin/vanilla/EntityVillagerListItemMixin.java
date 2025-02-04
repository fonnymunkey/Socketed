package socketed.common.mixin.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipeList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import socketed.common.util.AddSocketsHelper;

import java.util.Random;

@Mixin(EntityVillager.ListItemForEmeralds.class)
public class EntityVillagerListItemMixin {
    @Inject(
            method = "addMerchantRecipe",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/village/MerchantRecipeList;add(Ljava/lang/Object;)Z"),
            remap = false
    )
    private void socketed_entityVillagerListItem_addSockets(IMerchant merchant, MerchantRecipeList recipeList, Random random, CallbackInfo ci, @Local(ordinal = 1) ItemStack stack){
        AddSocketsHelper.addSockets(stack, AddSocketsHelper.EnumItemCreationContext.MERCHANT);
    }
}
