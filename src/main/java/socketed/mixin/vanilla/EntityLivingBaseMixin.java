package socketed.mixin.vanilla;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import socketed.common.capabilities.effectscache.CapabilityEffectsCacheHandler;
import socketed.common.capabilities.effectscache.ICapabilityEffectsCache;
import socketed.common.socket.gem.effect.DeathTotemGemEffect;
import socketed.common.socket.gem.effect.GenericGemEffect;

import java.util.Random;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {
	
	@Shadow public abstract Random getRNG();
	
	@Shadow public abstract void setHealth(float health);
	
	@Shadow public abstract void clearActivePotions();
	
	@Shadow public abstract void addPotionEffect(PotionEffect potioneffectIn);
	
	public EntityLivingBaseMixin(World worldIn) {
		super(worldIn);
	}
	
	@Inject(
			method = "checkTotemDeathProtection",
			at = @At("HEAD"),
			cancellable = true
	)
	private void socketed_vanillaEntityLivingBase_checkTotemDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if(((EntityLivingBase)(Object)this) instanceof EntityPlayer) {
			if(!source.canHarmInCreative()) {
				ICapabilityEffectsCache cachedEffects = this.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
				if(cachedEffects == null) return;
				
				for(GenericGemEffect effect : cachedEffects.getActiveEffects()) {
					if(effect instanceof DeathTotemGemEffect && this.getRNG().nextFloat() < ((DeathTotemGemEffect)effect).getSaveChance()) {
						this.setHealth(1.0F);
						this.clearActivePotions();
						this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
						this.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
						this.world.setEntityState(this, (byte)35);
						cir.setReturnValue(true);
					}
				}
			}
		}
	}
}