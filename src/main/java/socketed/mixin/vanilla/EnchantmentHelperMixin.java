package socketed.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import socketed.api.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.api.common.capabilities.socketable.ICapabilitySocketable;
import socketed.api.util.SocketedUtil;
import socketed.common.socket.gem.effect.PlusEnchantmentGemEffect;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

	@ModifyReturnValue(
			method = "getEnchantmentLevel",
			at = @At("RETURN")
	)
	private static int modifyEnchantmentLevel(int original, @Local(argsOnly = true) Enchantment enchantment, @Local(argsOnly = true) ItemStack stack){
		if(original <= 0) return original;
		ICapabilitySocketable sockets = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
		if(sockets == null) return original;

		int addedAmount = SocketedUtil.filterForEffectType(sockets.getAllPossibleEffects(), PlusEnchantmentGemEffect.class)
				.filter(effect -> enchantment.equals(effect.getEnchantment()))
				.mapToInt(PlusEnchantmentGemEffect::getAmount)
				.sum();

		//No clamping above
		return Math.max(original+addedAmount, enchantment.getMinLevel());
	}
}