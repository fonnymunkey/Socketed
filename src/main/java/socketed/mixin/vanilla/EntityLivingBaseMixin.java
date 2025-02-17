package socketed.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import socketed.common.capabilities.effectscache.CapabilityEffectsCacheHandler;
import socketed.common.capabilities.effectscache.ICapabilityEffectsCache;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.common.socket.gem.effect.activatable.activator.DeathTotemCheckActivator;
import socketed.common.socket.gem.effect.activatable.callback.CancelEventCallback;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {
	
	@Shadow public abstract void setHealth(float health);
	
	@Shadow public abstract float getHealth();
	
	public EntityLivingBaseMixin(World worldIn) {
		super(worldIn);
	}
	
	@ModifyReturnValue(
			method = "checkTotemDeathProtection",
			at = @At("RETURN")
	)
	private boolean socketed_vanillaEntityLivingBase_checkTotemDeathProtection(boolean original, DamageSource source) {
		if(!original && !source.canHarmInCreative() && ((EntityLivingBase)(Object)this) instanceof EntityPlayer && this.getHealth() <= 0.0F) {
			ICapabilityEffectsCache cachedEffects = this.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
			if(cachedEffects == null) return original;
			
			for(GenericGemEffect effect : cachedEffects.getActiveEffects()) {
				if(effect instanceof ActivatableGemEffect) {
					ActivatableGemEffect activatableGemEffect = (ActivatableGemEffect)effect;
					if(activatableGemEffect.getActivator() instanceof DeathTotemCheckActivator) {
						DeathTotemCheckActivator activator = (DeathTotemCheckActivator)activatableGemEffect.getActivator();
						CancelEventCallback callable = new CancelEventCallback(false);
						activator.attemptDeathTotemCheckActivation(activatableGemEffect, callable, ((EntityPlayer)(Object)this));
						
						if(callable.isCancelled()) {
							//If cancelled from an effect other than UndyingTotem, atleast set health to minimum 1.0
							if(this.getHealth() <= 0.0F) this.setHealth(1.0F);
							return true;
						}
					}
				}
			}
		}
		return original;
	}
}