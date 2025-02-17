package socketed.common.socket.gem.effect.activatable.activator;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import socketed.Socketed;
import socketed.common.capabilities.effectscache.CapabilityEffectsCacheHandler;
import socketed.common.capabilities.effectscache.ICapabilityEffectsCache;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.common.socket.gem.effect.activatable.condition.GenericCondition;

import javax.annotation.Nullable;

public class PassiveActivator extends GenericActivator {
	
	public static final String TYPE_NAME = "Passive";
	
	@SerializedName("Activation Rate")
	protected final Integer activationRate;
	
	public PassiveActivator(@Nullable GenericCondition condition, int activationRate) {
		super(condition);
		this.activationRate = activationRate;
	}
	
	/**
	 * Called on LivingUpdateEvent for players
	 * No callback
	 * @param effect the effect parent of this activator
	 * @param player the player that is the source of the effect
	 */
	protected void attemptPassiveActivation(ActivatableGemEffect effect, EntityPlayer player) {
		if(player.ticksExisted % this.activationRate == 0) {
			if(this.testCondition(player, player)) {
				effect.affectTargets(null, player, player);
			}
		}
	}
	
	//TODO handle this better for activators/targets/conditions, add tooltip override option to gem for less bloat on complicated effects
	@SideOnly(Side.CLIENT)
	@Override
	public String getTooltipString() {
		return "";
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}
	
	/**
	 * ActivationRate: Required
	 */
	@Override
	public boolean validate() {
		if(super.validate()) {
			if(this.activationRate == null) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, activation rate must be defined");
			else if(this.activationRate < 1) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, activation rate must be greater than 0");
			else return true;
		}
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
					if(activatableGemEffect.getActivator() instanceof PassiveActivator) {
						PassiveActivator passiveActivator = (PassiveActivator)activatableGemEffect.getActivator();
						passiveActivator.attemptPassiveActivation(activatableGemEffect, player);
					}
				}
			}
		}
	}
}