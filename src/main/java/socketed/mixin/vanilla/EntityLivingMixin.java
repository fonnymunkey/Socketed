package socketed.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import socketed.common.loot.DefaultSocketsGenerator;

@Mixin(EntityLiving.class)
public abstract class EntityLivingMixin {
	
	/**
	 * In at least Vanilla, everytime setEquipmentBasedOnDifficulty is called, setEnchantmentBasedOnDifficulty is called right after
	 * Multiple classes override setEquipmentBasedOnDifficulty, making it more difficult to use
	 * However, setEnchantmentBasedOnDifficulty is only overridden by EntityWitherSkeleton
	 */
	@ModifyExpressionValue(
			method = "setEnchantmentBasedOnDifficulty",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLiving;getHeldItemMainhand()Lnet/minecraft/item/ItemStack;", ordinal = 0)
	)
	private ItemStack socketed_vanillaEntityLiving_setEnchantmentBasedOnDifficulty_getHeldItemMainhand(ItemStack original) {
		DefaultSocketsGenerator.addSockets(original, DefaultSocketsGenerator.SocketedItemCreationContext.MOB_DROP);
		return original;
	}
	
	@ModifyExpressionValue(
			method = "setEnchantmentBasedOnDifficulty",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLiving;getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;")
	)
	private ItemStack socketed_vanillaEntityLiving_setEnchantmentBasedOnDifficulty_getItemStackFromSlot(ItemStack original) {
		DefaultSocketsGenerator.addSockets(original, DefaultSocketsGenerator.SocketedItemCreationContext.MOB_DROP);
		return original;
	}
}