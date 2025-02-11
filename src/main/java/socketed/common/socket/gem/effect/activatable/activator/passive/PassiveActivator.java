package socketed.common.socket.gem.effect.activatable.activator.passive;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.Socketed;
import socketed.common.capabilities.effectscache.CapabilityEffectsCacheHandler;
import socketed.common.capabilities.effectscache.ICapabilityEffectsCache;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.common.socket.gem.effect.activatable.activator.GenericActivator;

public abstract class PassiveActivator extends GenericActivator {
	
	@SerializedName("Activation Rate")
	protected final Integer activationRate;
	
	public PassiveActivator(int activationRate) {
		super();
		this.activationRate = activationRate;
	}
	
	/**
	 * @return the tick rate of calling attemptPassiveActivation
	 */
	public int getActivationRate() {
		return this.activationRate;
	}
	
	/**
	 * Called every x ticks as defined by the activation rate
	 * Only called on the server thread
	 * @param effect the effect to be performed if this activator is successful
	 * @param player the player that is the source of the effect
	 */
	public abstract void attemptPassiveActivation(ActivatableGemEffect effect, EntityPlayer player);
	
	/**
	 * ActivationRate: Required
	 */
	@Override
	public boolean validate() {
		if(this.activationRate == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, activation rate must be defined");
		else if(this.activationRate < 1) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, activation rate must be greater than 0");
		else return true;
		return false;
	}
	
	@Mod.EventBusSubscriber
	public static class EventHandler {
		
		/**
		 * Event handling for PassiveActivators
		 */
		@SubscribeEvent
		public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
			if(!(event.getEntityLiving() instanceof EntityPlayer)) return;
			if(event.getEntityLiving().world.isRemote) return;
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			
			ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
			if(cachedEffects == null) return;
			
			for(GenericGemEffect effect : cachedEffects.getActiveEffects()) {
				if(effect instanceof ActivatableGemEffect) {
					ActivatableGemEffect activatableGemEffect = (ActivatableGemEffect)effect;
					if(activatableGemEffect.getActivatorType() instanceof PassiveActivator) {
						PassiveActivator passiveActivator = (PassiveActivator)activatableGemEffect.getActivatorType();
						if(player.ticksExisted%passiveActivator.getActivationRate() == 0) {
							passiveActivator.attemptPassiveActivation(activatableGemEffect, player);
						}
					}
				}
			}
		}
	}
}