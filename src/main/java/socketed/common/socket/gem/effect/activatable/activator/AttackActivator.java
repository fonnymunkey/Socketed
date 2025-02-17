package socketed.common.socket.gem.effect.activatable.activator;

import com.google.gson.annotations.SerializedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import socketed.Socketed;
import socketed.common.capabilities.effectscache.CapabilityEffectsCacheHandler;
import socketed.common.capabilities.effectscache.ICapabilityEffectsCache;
import socketed.common.capabilities.socketable.CapabilitySocketableHandler;
import socketed.common.capabilities.socketable.ICapabilitySocketable;
import socketed.common.socket.gem.effect.GenericGemEffect;
import socketed.common.socket.gem.effect.activatable.ActivatableGemEffect;
import socketed.common.socket.gem.effect.activatable.callback.CancelEventCallback;
import socketed.common.socket.gem.effect.activatable.condition.GenericCondition;
import socketed.common.socket.gem.effect.slot.SocketedSlotTypes;

import javax.annotation.Nullable;

public abstract class AttackActivator extends GenericActivator {
	
	@SerializedName("Allows Melee")
	protected Boolean allowsMelee;
	
	@SerializedName("Allows Ranged")
	protected Boolean allowsRanged;
	
	public AttackActivator(@Nullable GenericCondition condition, boolean allowsMelee, boolean allowsRanged) {
		super(condition);
		this.allowsMelee = allowsMelee;
		this.allowsRanged = allowsRanged;
	}
	
	/**
	 * Called during LivingAttackEvent
	 * Target and attacker can not be the same entity
	 * If the callback is cancelled, cancels the LivingAttackEvent
	 * @param effect the effect parent of this activator
	 * @param callback the Cancellable callback to allow for the effect to cancel the LivingAttackEvent
	 * @param player the player that is the source of the effect
	 * @param target the target of the attack, may be the player
	 * @param attacker the attacker performing the attack, may be the player
	 * @param isMelee if the attack is a melee attack
	 * @param isRanged if the attack is a ranged attack
	 * @param directlyActivated if the source of the effect is the attacker's weapon rather than the player's effect cache
	 * @param source the damagesource of the attack for additional context
	 */
	protected abstract void attemptAttackActivation(ActivatableGemEffect effect, CancelEventCallback callback, EntityPlayer player, EntityLivingBase target, EntityLivingBase attacker, boolean isMelee, boolean isRanged, boolean directlyActivated, DamageSource source);
	
	/**
	 * AllowsMelee: Optional, default true
	 * AllowsRanged: Optional, default false
	 */
	@Override
	public boolean validate() {
		if(this.allowsMelee == null) this.allowsMelee = true;
		if(this.allowsRanged == null) this.allowsRanged = false;
		
		if(super.validate()) {
			if(!this.allowsMelee && !this.allowsRanged) Socketed.LOGGER.warn("Invalid " + this.getTypeName() + " Activator, must allow at least either melee or ranged");
			else return true;
		}
		return false;
	}
	
	@Mod.EventBusSubscriber
	public static class EventHandler {
		
		/**
		 * Event handling for AttackActivators
		 */
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void onEntityHit(LivingAttackEvent event) {
			if(event.getEntityLiving() == null) return;
			if(event.getEntityLiving().world.isRemote) return;
			if(event.getSource() == null) return;
			DamageSource source = event.getSource();
			
			//Prioritize checking the direct attacker, prevent ranged pet attacks from triggering attacker effects such as lycanites
			boolean isMelee = isDamageSourceMelee(source);
			boolean isRanged = !isMelee && isDamageSourceRanged(source);
			
			EntityLivingBase target = event.getEntityLiving();
			EntityLivingBase attacker;
			if(isMelee) attacker = (EntityLivingBase)source.getImmediateSource();
			else if(isRanged) attacker = (EntityLivingBase)source.getTrueSource();
			else return;
			
			//Dont trigger on self damage if that manages to happen
			if(target == attacker) return;
			
			//Allow for cancelling the attack from effects
			CancelEventCallback callback = new CancelEventCallback(false);
			
			//AttackedActivator handling
			if(target instanceof EntityPlayer) {
				handleAttacked(callback, (EntityPlayer)target, attacker, isMelee, isRanged, source);
			}
			
			//Cancel before offensive effects if defensive effects cancelled attack
			if(callback.isCancelled()) {
				event.setCanceled(true);
				return;
			}
			
			//AttackingActivator handling
			if(attacker instanceof EntityPlayer) {
				handleAttacking(callback, (EntityPlayer)attacker, target, isMelee, isRanged, source);
			}
			
			if(callback.isCancelled()) {
				event.setCanceled(true);
			}
		}
		
		private static void handleAttacked(CancelEventCallback callback, EntityPlayer player, EntityLivingBase attacker, boolean isMelee, boolean isRanged, DamageSource source) {
			ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
			if(cachedEffects == null) return;
			
			//Handle cached effects
			for(GenericGemEffect effect : cachedEffects.getActiveEffects()) {
				if(effect instanceof ActivatableGemEffect) {
					ActivatableGemEffect activatableGemEffect = (ActivatableGemEffect)effect;
					if(activatableGemEffect.getActivator() instanceof AttackActivator) {
						AttackActivator attackActivator = ((AttackActivator)activatableGemEffect.getActivator());
						attackActivator.attemptAttackActivation(activatableGemEffect, callback, player, player, attacker, isMelee, isRanged, false, source);
					}
				}
			}
		}
		
		private static void handleAttacking(CancelEventCallback callback, EntityPlayer player, EntityLivingBase target, boolean isMelee, boolean isRanged, DamageSource source) {
			ICapabilityEffectsCache cachedEffects = player.getCapability(CapabilityEffectsCacheHandler.CAP_EFFECTS_CACHE, null);
			if(cachedEffects == null) return;
			
			//Handle cached effects
			for(GenericGemEffect effect : cachedEffects.getActiveEffects()) {
				if(effect instanceof ActivatableGemEffect) {
					ActivatableGemEffect activatableGemEffect = (ActivatableGemEffect)effect;
					if(activatableGemEffect.getActivator() instanceof AttackActivator) {
						AttackActivator attackActivator = ((AttackActivator)activatableGemEffect.getActivator());
						attackActivator.attemptAttackActivation(activatableGemEffect, callback, player, target, player, isMelee, isRanged, false, source);
					}
				}
			}
			
			//Handle direct activation effects
			//RLCombat swaps offhand into mainhand before posting LivingAttack for offhand attacks
			ItemStack weaponStack = player.getHeldItemMainhand();
			if(weaponStack.isEmpty()) return;
			
			ICapabilitySocketable sockets = weaponStack.getCapability(CapabilitySocketableHandler.CAP_SOCKETABLE, null);
			if(sockets == null) return;
			
			//TODO: RLCombat compat to get whether the attack is being posted from mainhand or offhand for more accurate slot type check
			for(GenericGemEffect effect : sockets.getAllActiveEffects(SocketedSlotTypes.HAND)) {
				if(effect instanceof ActivatableGemEffect) {
					ActivatableGemEffect activatableGemEffect = (ActivatableGemEffect)effect;
					if(activatableGemEffect.getActivator() instanceof AttackActivator) {
						AttackActivator attackActivator = ((AttackActivator)activatableGemEffect.getActivator());
						attackActivator.attemptAttackActivation(activatableGemEffect, callback, player, target, player, isMelee, isRanged, true, source);
					}
				}
			}
		}
		
		private static boolean isDamageSourceMelee(DamageSource source) {
			return source.getImmediateSource() instanceof EntityLivingBase;
		}
		
		private static boolean isDamageSourceRanged(DamageSource source) {
			return !(source.getImmediateSource() instanceof EntityLivingBase) && source.getTrueSource() instanceof EntityLivingBase;
		}
	}
}