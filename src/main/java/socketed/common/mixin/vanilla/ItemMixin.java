package socketed.common.mixin.vanilla;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.capabilities.socketable.ICapabilitySocketable;
import socketed.common.jsondata.entry.effect.AttributeGemEffect;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.effect.slot.SocketedSlotTypes;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {
	
	/**
	 * Handling for Damage/Speed/Reach Attributes for hand slots
	 * Damage/Speed/Reach when socketed on either hand need to only apply modifiers to the Mainhand EntityEquipmentSlot
	 * In order to work around Vanilla handling of attacking and for compat with mods like RLCombat
	 * RLCombat has builtin handling for applying Mainhand attributes during offhand attacks
	 * Technically makes it not possible to apply those attributes as "Mainhand Only" or "Offhand Only" but our abilities are limited for compat
	 * "Mainhand Only" or "Offhand Only" for non-Damage/Speed/Reach attribute effects or non-attribute effects is still possible
	 */
	@ModifyReturnValue(
			method = "getAttributeModifiers",
			at = @At("RETURN"),
			remap = false
	)
	private Multimap<String,AttributeModifier> socketed_vanillaItem_getAttributeModifiers(Multimap<String, AttributeModifier> original, EntityEquipmentSlot slot, ItemStack stack) {
		if(stack.isEmpty() || slot != EntityEquipmentSlot.MAINHAND) return original;
		
		ICapabilitySocketable cap = stack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
		if(cap != null) {
			List<GenericGemEffect> effects = cap.getAllActiveEffects(SocketedSlotTypes.HAND);
			for(GenericGemEffect effect : effects) {
				if(effect instanceof AttributeGemEffect) {
					AttributeGemEffect attrEffect = (AttributeGemEffect)effect;
					AttributeModifier modifier = attrEffect.getModifier();
					if(modifier == null) continue;
					
					String attribute = attrEffect.getAttribute();
					if(attribute.equals(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
						original.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), modifier);
					}
					else if(attribute.equals(SharedMonsterAttributes.ATTACK_SPEED.getName())) {
						original.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), modifier);
					}
					else if(attribute.equals(EntityPlayer.REACH_DISTANCE.getName())) {
						original.put(EntityPlayer.REACH_DISTANCE.getName(), modifier);
					}
				}
			}
		}
		return original;
	}
}